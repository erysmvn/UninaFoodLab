package DAO;

import DAO.Interfaces.StudenteDAOInterface;

import Entity.*;
import Controller.Controller;
import DB.DBConnection;

import java.sql.*;
import java.util.ArrayList;

public class StudenteDAO implements StudenteDAOInterface {
    DBConnection dbc;
    Statement stmt;
    ResultSet rs;
    Connection con;
    Controller controller;

    public StudenteDAO(Controller controller) {
        this.dbc = controller.getDBConnection();
        con = dbc.getConnection();
        stmt = dbc.getStatement();
        this.controller = controller;
    }

    public Studente tryLogin(String sql) throws SQLException {
        Studente studente = null;
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
            SQLException sqlException = new SQLException();
            throw sqlException;
        }

        return studente;
    }

    public Studente tryRegister(Studente studente) throws SQLException {
        String sql = "INSERT INTO studente (matricola, nome_stud, cognome, email, passw) VALUES (?, ?, ?, ?, md5(?))";

        try  {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, studente.getMatricola());
            pstmt.setString(2, studente.getNome());
            pstmt.setString(3, studente.getCognome());
            pstmt.setString(4, studente.getEmail());
            pstmt.setString(5, studente.getPassw());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                Exception exc  = new Exception("No row inserted");
                throw exc;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return studente;
    }

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
            throw new RuntimeException("Errore durante l'iscrizione al corso", e);
        }
        return false;
    }

}
