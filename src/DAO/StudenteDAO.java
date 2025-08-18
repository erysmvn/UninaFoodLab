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

    public StudenteDAO(DBConnection dbConnection, Controller controller) {
        this.dbc = dbConnection;
        con = dbc.getConnection();
        stmt = dbc.getStatement();
        this.controller = controller;
    }

    public Studente tryLogin(String sql)throws SQLException {
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

    public ArrayList<Corso> getCorsiFromStudente(Studente studente) {
        ArrayList<Corso> corsi = new ArrayList<>();
        CorsoDAO corsoDao = new CorsoDAO(dbc,controller);

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

}
