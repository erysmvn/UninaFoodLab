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
    Connection con;

    // Constructors
    public StudenteDAO(DBConnection dbc) {
        this.dbc = dbc;
        con = dbc.getConnection();
    }

    // Methods
    @Override
    public Studente login(String email, String password)
            throws emailNotFoundException, passwordErrataException, SQLException {

        email = email.trim();

        String sql = "SELECT * FROM studente WHERE email = ? AND passw = md5(?)";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, email);
        pstmt.setString(2, password);

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            Studente studente = new Studente(
                    rs.getString("matricola"),
                    rs.getString("nome_stud"),
                    rs.getString("cognome"),
                    rs.getString("email"),
                    rs.getString("passw")
            );
            studente.setCorsi(getCorsiFromStudente(studente));
            return studente;
        } else {
            if (existingEmail(email)) {
                throw new passwordErrataException();
            } else {
                throw new emailNotFoundException();
            }
        }
        
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
    public void checkOldPassword(String oldPassword, Studente studente) throws SQLException, changePasswordException {
        String sql = "SELECT 1 FROM Studente WHERE passw = md5(?) AND matricola = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, oldPassword);
        ps.setString(2, studente.getMatricola());

        ResultSet rs = ps.executeQuery();
        if (!rs.next()) {
            throw new oldPasswordErrorException();
        }
    }

    @Override
    public void changeUserPassword(String newPassword, Studente studente) throws changePasswordException, SQLException {

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
    public void subscribeToCourse(Studente studente, Corso corso) throws SQLException {
        String sql = "INSERT INTO segue (matricola, idcorso) VALUES (?, ?)";

        PreparedStatement stmt = con.prepareStatement(sql);

        stmt.setString(1, studente.getMatricola());
        stmt.setInt(2, corso.getIdCorso());

        stmt.executeUpdate();
    }

    @Override
    public void unsubscribeToCourse(Studente studente, Corso corso) throws SQLException {
        String sql = "DELETE FROM segue WHERE matricola = ? AND idcorso = ?";

        PreparedStatement stmt = con.prepareStatement(sql);
            
        stmt.setString(1, studente.getMatricola());
        stmt.setInt(2, corso.getIdCorso());
            
        stmt.executeUpdate();
    }

    @Override
    public Boolean checkIfSubscribed(Studente studente, Corso corso) throws SQLException {
        String sql = "SELECT COUNT(*) FROM segue WHERE matricola = ? AND idcorso = ?";
        PreparedStatement stmt = con.prepareStatement(sql);

        stmt.setString(1, studente.getMatricola());
        stmt.setInt(2, corso.getIdCorso());

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int count = rs.getInt(1);
            return count > 0;
        }
        return false;
    }

    // Get methods
    @Override
    public ArrayList<Corso> getCorsiFromStudente(Studente studente) throws SQLException {
        ArrayList<Corso> corsi = new ArrayList<>();

        String sql = "SELECT DISTINCT c.idcorso " +
                "FROM corso c NATURAL JOIN studente s NATURAL JOIN segue " +
                "WHERE Matricola = ? ";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, studente.getMatricola());
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            CorsoDAO corsoDAO = new CorsoDAO(dbc);
            Corso corso = corsoDAO.getCorsoByIdCorso(rs.getInt("idcorso"));
            corsi.add(corso);
        }

        return corsi;
    }

    private boolean existingEmail(String email) throws SQLException {
        String sql = "Select 1 from studente where email = ?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, email);

        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }

    public Studente getStudenteByMatricola(String matricola) throws SQLException {
        System.out.println(matricola);
        String sql = "SELECT * FROM studente WHERE Matricola = ?";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1, matricola);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return getStudenteByRs(rs);
        }
        throw new SQLException();
    }

    public Studente getStudenteByRs(ResultSet rs) throws SQLException {
        if (rs.next()) {
            return new Studente(
                    rs.getString("matricola"),
                    rs.getString("nome_stud"),
                    rs.getString("cognome"),
                    rs.getString("email"),
                    rs.getString("passw")
            );
        }
        throw new SQLException();
    }
}
