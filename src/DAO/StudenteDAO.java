package DAO;

import DAO.Interfaces.StudenteDAOInterface;

import Entity.*;
import Controller.Controller;
import DB.DBConnection;
import Exception.UserExceptions.ChangePasswordException.changePasswordException;
import Exception.UserExceptions.ChangePasswordException.oldPasswordErrorException;
import Exception.UserExceptions.LoginException.emailNotFoundException;
import Exception.UserExceptions.LoginException.passwordErrataException;

import java.sql.*;
import java.util.ArrayList;

public class StudenteDAO implements StudenteDAOInterface {
    DBConnection dbc;
    Statement stmt;
    ResultSet rs;
    Connection con;
    Controller controller;

    // Constructors
    public StudenteDAO(Controller controller) {
        this.dbc = controller.getDBConnection();
        con = dbc.getConnection();
        stmt = dbc.getStatement();
        this.controller = controller;
    }
    // Methods
    @Override
    public Studente login(String email, String password) throws emailNotFoundException, passwordErrataException,SQLException {
        Studente studente = null;
        email = email.trim();
        String sql = "Select * from studente where email = '" + email + "' AND passw = md5('" + password + "')";
        rs = stmt.executeQuery(sql);
        if(rs.next()){
            studente = new Studente(
                    rs.getString("matricola"),
                    rs.getString("nome_stud"),
                    rs.getString("cognome"),
                    rs.getString("email"),
                    rs.getString("passw")
            );
            studente.setCorsi(getCorsiFromStudente(studente));
        }else{
            if (existingEmail(email)) {
                throw new passwordErrataException();
            }else{
                throw new emailNotFoundException();
            }
        }

        return studente;
    }

    @Override
    public Studente register(Studente studente) throws SQLException {
        String sql = "INSERT INTO studente (matricola, nome_stud, cognome, email, passw) VALUES (?, ?, ?, ?, md5(?))";

            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, studente.getMatricola());
            pstmt.setString(2, studente.getNome());
            pstmt.setString(3, studente.getCognome());
            pstmt.setString(4, studente.getEmail());
            pstmt.setString(5, studente.getPassw());

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted == 0)
                throw new SQLException();

        return studente;
    }

    @Override
    public void checkOldPassword(String oldPassword, Studente studente) throws changePasswordException{
        String sql = "SELECT 1 FROM Studente WHERE passw = md5(?) AND matricola = ?";
        System.out.println("Controllo password: " + oldPassword);

        try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, oldPassword);
                ps.setString(2, studente.getMatricola());

                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new oldPasswordErrorException();
                }
            } catch (SQLException e) {
                throw new oldPasswordErrorException();
            }
    }

    @Override
    public void changeUserPassword(String newPassword, Studente studente)throws changePasswordException, SQLException{

        String sql = "UPDATE Studente SET passw = md5(?) WHERE matricola = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, newPassword);
            ps.setString(2, studente.getMatricola());
            int rows = ps.executeUpdate();


            if (rows > 0) {
                studente.setPassw(newPassword);
            } else {
                throw new changePasswordException();
            }

    }

    @Override
    public void subscribeToCourse(Studente studente, Corso corso) {
        try {
            String sql = "INSERT INTO segue (matricola, idcorso) VALUES (?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, studente.getMatricola());
            stmt.setInt(2, corso.getIdCorso());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante l'iscrizione al corso", e);
        }
    }

    @Override
    public void unsubscribeToCourse(Studente studente, Corso corso){
        try {
            String sql = "DELETE FROM segue WHERE matricola = ? AND idcorso = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, studente.getMatricola());
            stmt.setInt(2, corso.getIdCorso());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante l'eliminazione dell'iscrizione al corso", e);
        }
    }

    @Override
    public Boolean checkIfSubscribed(Studente studente, Corso corso) {
        try {
            String sql = "SELECT COUNT(*) FROM segue WHERE matricola = ? AND idcorso = ?";
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, studente.getMatricola());
            stmt.setInt(2, corso.getIdCorso());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore durante la verifica di iscrizione al corso", e);
        }
        return false;
    }

    // Get methods
    @Override
    public ArrayList<Corso> getCorsiFromStudente(Studente studente) {
        ArrayList<Corso> corsi = new ArrayList<>();
        CorsoDAO corsoDao = new CorsoDAO(controller);

        String sql = "SELECT DISTINCT c.nome_corso " +
                "FROM corso c NATURAL JOIN studente s NATURAL JOIN segue " +
                "WHERE s.email = '" + studente.getEmail() + "'";

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

    private boolean existingEmail(String email)throws SQLException{
        String sql = "Select 1 from studente where email = '" + email + "'";
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        if(rs.next()){
            return true;
        }else {
            return false;
        }
    }
}
