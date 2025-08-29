package DAO;

import Controller.Controller;
import DAO.Interfaces.ChefDAOInterface;
import DB.DBConnection;
import Entity.*;
import Exception.UserExceptions.ChangePasswordException.changePasswordException;
import Exception.UserExceptions.ChangePasswordException.oldPasswordErrorException;
import Exception.UserExceptions.LoginException.emailNotFoundException;
import Exception.UserExceptions.LoginException.passwordErrataException;

import java.sql.*;
import java.util.ArrayList;

public class ChefDAO implements ChefDAOInterface {
    DBConnection dbc;
    Statement stmt;
    Connection con;
    Controller controller;

    // Constructors
    public ChefDAO(Controller controller) {
        this.dbc = controller.getDBConnection();
        this.con = dbc.getConnection();
        this.stmt = dbc.getStatement();
        this.controller = controller;
    }

    // Methods
    @Override
    public Chef login(String email, String password) throws emailNotFoundException, passwordErrataException,SQLException{
        Chef chef;
        email = email.trim();

        String sql = "Select * from chef where email = ? AND  passw = md5(?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, email);
        pstmt.setString(2, password);
        ResultSet rs = pstmt.executeQuery();

        if(rs.next()){
            chef = createChefByRsForSetCorsi(rs);
        }else{

            if (existingEmail(email))
                throw new passwordErrataException();
            else
                throw new emailNotFoundException();

        }
        return chef;
    }

    @Override
    public Chef register(Chef chef) throws SQLException{
        String sql = "INSERT INTO chef (nome_chef, cognome, email, passw) VALUES (?, ?, ?, md5(?))";

            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, chef.getNome());
            pstmt.setString(2, chef.getCognome());
            pstmt.setString(3, chef.getEmail());
            pstmt.setString(4, chef.getPassw());

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted == 0)
                throw new SQLException();


            ResultSet generatedKeys = pstmt.getGeneratedKeys();

                if (generatedKeys.next()){
                    int id = generatedKeys.getInt("idchef");
                    chef.setIdchef(id);
                } else {
                    throw new SQLException();
                }

        return chef;
    }

    @Override
    public void checkOldPassword(String oldPassword, Chef chef) throws changePasswordException {
        String sql = "SELECT 1 FROM chef WHERE passw = md5(?) AND idchef = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, oldPassword);
            ps.setInt(2, chef.getIdchef());

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new oldPasswordErrorException();
            }
        } catch (SQLException e) {
            throw new oldPasswordErrorException();
        }
    }

    @Override
    public void changeUserPassword(String newPassword, Chef chef) throws changePasswordException, SQLException {
        String sql = "UPDATE chef SET passw = md5(?) WHERE idchef = ?";
        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, newPassword);
        ps.setInt(2, chef.getIdchef());
        int rows = ps.executeUpdate();

        if (rows > 0) {
            chef.setPassw(newPassword);
        } else {
            throw new changePasswordException();
        }
    }

    // Get methods
    @Override
    public ArrayList<Corso> getCorsiFromChef(Chef chef){

        ArrayList<Corso> corsi = new ArrayList<>();
        CorsoDAO corsoDao = controller.getCorsoDAO();

        String sql = "SELECT DISTINCT c.nome_corso " +
                "FROM corso c NATURAL JOIN chef ch NATURAL JOIN tiene " +
                "WHERE ch.email = '" + chef.getEmail() + "'";

        try {
            Statement stmt2 = con.createStatement();
            ResultSet rs2 = stmt2.executeQuery(sql);

            while (rs2.next()) {
                Corso corso = corsoDao.getCorsoByTitle(rs2.getString("nome_corso"));
                corsi.add(corso);
            }

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return corsi;
    }

    @Override
    public Chef getChefDaAggiungereToNuovoCorso(String nome, String cognome, String email) {
        String sql = "SELECT * FROM chef WHERE UPPER(nome_chef) = UPPER(?) AND UPPER(cognome) = UPPER(?) AND UPPER(email) = UPPER(?);";
        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nome);
            pstmt.setString(2, cognome);
            pstmt.setString(3, email);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return createChefByRs(rs);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }


    private boolean existingEmail(String email) throws SQLException {
        String sql = "Select 1 from chef where email = '" + email + "'";
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }else {
            return false;
        }
    }
    private Chef createChefByRs(ResultSet rs) throws SQLException{
        Chef chef = new Chef(
                rs.getInt("idchef"),
                rs.getString("nome_chef"),
                rs.getString("cognome"),
                rs.getString("email"),
                rs.getString("passw")
        );

        return chef;
    }
    private Chef createChefByRsForSetCorsi(ResultSet rs) throws SQLException{
        Chef chef = new Chef(
                rs.getInt("idchef"),
                rs.getString("nome_chef"),
                rs.getString("cognome"),
                rs.getString("email"),
                rs.getString("passw")
        );
        chef.setCorsi(getCorsiFromChef(chef));

        return chef;
    }



}