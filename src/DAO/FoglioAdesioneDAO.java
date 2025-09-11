package DAO;

import Controller.Controller;
import DAO.Interfaces.SessioneDAOInterface;
import DAO.Interfaces.foglioAdesioneDAOInterface;
import DB.DBConnection;
import Entity.FoglioAdesione;
import Entity.SessionePresenza;
import Entity.Studente;
import Entity.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FoglioAdesioneDAO implements foglioAdesioneDAOInterface {

    DBConnection dbc;
    Connection con;

    public FoglioAdesioneDAO(DBConnection dbc) {
        this.dbc = dbc;
        con = dbc.getConnection();
    }

    @Override
    public ArrayList<FoglioAdesione> getFogliAdesioneByIdSessione(SessionePresenza sp) throws SQLException {

        ArrayList<FoglioAdesione> fogli = new ArrayList<>();
        String sql = "select * from conferma_partecipazione where idsessione = ?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, sp.getIdSessione());
        ResultSet rs = pstmt.executeQuery();

        while(rs.next()){
            fogli.add(createFoglioAdesioneByResultSet(rs, sp));
        }

        return fogli;
    }

    @Override
    public FoglioAdesione getFoglioAdesioneBySessioneNPath(String path, SessionePresenza sessionePresenza, Studente studente) throws SQLException {
        String sql = "select * from conferma_partecipazione where idsessione = ? and matricola = ? and documento = ?  ";
        
        PreparedStatement pstmt = con.prepareStatement(sql);

        pstmt.setInt(1, sessionePresenza.getIdSessione());
        pstmt.setString(2, studente.getMatricola());
        pstmt.setString(3, path);
        
        ResultSet rs = pstmt.executeQuery();

        if(rs.next()){
            return new FoglioAdesione(sessionePresenza, studente, path);
        }
            //return createFoglioAdesioneByResultSet(rs);

        return  null;
    }

    @Override
    public void insertFoglioDiAdesione(String pathFile, SessionePresenza sessionePresenza, Studente studente) throws SQLException {
        String sql = "INSERT INTO conferma_partecipazione (idsessione, matricola, documento) VALUES (?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, sessionePresenza.getIdSessione());
        pstmt.setString(2, studente.getMatricola());
        pstmt.setString(3, pathFile);

        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected < 0)
            throw new SQLException();
        }

    private FoglioAdesione createFoglioAdesioneByResultSet(ResultSet rs) throws SQLException {
        StudenteDAO studenteDAO = new StudenteDAO(dbc);
        SessioneDAO sessioneDAO = new SessioneDAO(dbc);
        return new FoglioAdesione (
                (SessionePresenza) sessioneDAO.getSessioneById(rs.getInt("idsessione")),
                studenteDAO.getStudenteByMatricola(rs.getString("matricola")),
                rs.getString("documento")
        );
    }

    private FoglioAdesione createFoglioAdesioneByResultSet(ResultSet rs, SessionePresenza sp) throws SQLException {
        StudenteDAO studenteDAO = new StudenteDAO(dbc);
        return new FoglioAdesione (
                sp,
                studenteDAO.getStudenteByMatricola(rs.getString("matricola")),
                rs.getString("documento")
        );
    }

}
