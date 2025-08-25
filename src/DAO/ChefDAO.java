package DAO;

import Controller.Controller;
import DB.DBConnection;
import Entity.*;
import Exception.UserExceptions.ChangePasswordException.changePasswordException;
import Exception.UserExceptions.LoginException.emailNotFoundException;
import Exception.UserExceptions.LoginException.passwordErrataException;

import java.sql.*;
import java.util.ArrayList;

public class ChefDAO {
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
    public Chef login(String email, String password) throws emailNotFoundException, passwordErrataException,SQLException{
        Chef chef = null;
        email = email.trim();
        String sql = "Select * from chef where email = '" + email + "' AND  passw = md5('" + password + "')";
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        if(rs.next()){
            chef = createChefByRs(rs);
        }else{

            if (existingEmail(email))
                throw new passwordErrataException();
            else
                throw new emailNotFoundException();

        }
        return chef;
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

    public Chef register(Chef chef) throws SQLException{
        String sql = "INSERT INTO chef (nome_chef, cognome, email, passw) VALUES (?, ?, ?, md5(?))";

        try {
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, chef.getNome());
            pstmt.setString(2, chef.getCognome());
            pstmt.setString(3, chef.getEmail());
            pstmt.setString(4, chef.getPassw());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted == 0) {
                Exception exc  = new Exception("No row inserted");
                throw exc;
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    chef.setIdchef(id);
                } else {
                    throw new SQLException("Creating chef failed, no ID obtained.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return chef;
    }

    public Boolean checkOldPassword(String oldPassword, Chef chef) {
        int count = 0;
        try {
            String sql = "SELECT COUNT(*) FROM chef WHERE passw = md5('" + oldPassword + "') AND idchef = '" + chef.getIdchef() + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore", e);
        }
        return count > 0;
    }

    public void changeUserPassword(String newPassword, Chef chef) throws changePasswordException, SQLException {
        String sql = "UPDATE chef SET passw = md5(?) WHERE idchef = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setInt(2, chef.getIdchef());
            int rows = ps.executeUpdate();

            if (rows > 0) {
                chef.setPassw(newPassword);
            } else {
                throw new changePasswordException();
            }

        }
    }


    // Get methods
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

    private Chef createChefByRs(ResultSet rs) throws SQLException{
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