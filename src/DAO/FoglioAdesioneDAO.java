package DAO;

import Controller.Controller;
import DAO.Interfaces.foglioAdesioneDAOInterface;
import DB.DBConnection;
import Entity.FoglioAdesione;
import Entity.SessionePresenza;
import Entity.Studente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FoglioAdesioneDAO implements foglioAdesioneDAOInterface {

    Controller controller;
    DBConnection dbc;
    Connection con;

    public FoglioAdesioneDAO(Controller controller) {
        this.controller = controller;
        this.dbc = controller.getDBConnection();
        con = dbc.getConnection();
    }

    @Override
    public ArrayList<FoglioAdesione> getFogliAdesioneByIdSessione(int idsessione) throws SQLException {

        ArrayList<FoglioAdesione> fogli = new ArrayList<>();
        String sql = "select * from conferma_partecipazione where idsessione = ?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, idsessione);
        ResultSet rs = pstmt.executeQuery();

        while(rs.next()){
            fogli.add(createFoglioAdesioneByResultSet(rs));
        }

        return fogli;
    }

    @Override
    public FoglioAdesione getFoglioAdesioneBySessioneNPath(String path, SessionePresenza sessionePresenza) throws SQLException {
        String sql = "select * from conferma_partecipazione where idsessione = ? and matricola = ? and documento = ?  ";
        
        PreparedStatement pstmt = con.prepareStatement(sql);

        pstmt.setInt(1,sessionePresenza.getIdSessione());
        pstmt.setString(2,((Studente)(controller.getUtente())).getMatricola());
        pstmt.setString(3,path);
        
        ResultSet rs = pstmt.executeQuery();

        if(rs.next())
            return createFoglioAdesioneByResultSet(rs);

        return  null;
    }

    @Override
    public void insertFoglioDiAdesione(String pathFile, SessionePresenza sessionePresenza) throws SQLException {
        String sql = "INSERT INTO conferma_partecipazione (idsessione, matricola, documento) VALUES (?, ?, ?)";
        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setInt(1, sessionePresenza.getIdSessione());
        pstmt.setString(2, ((Studente)(controller.getUtente())).getMatricola());
        pstmt.setString(3, pathFile);

        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected < 0)
            throw new SQLException();
        }

    private FoglioAdesione createFoglioAdesioneByResultSet(ResultSet rs) throws SQLException {
        return new FoglioAdesione(
                rs.getInt("idsessione"),
                rs.getString("matricola"),
                rs.getString("documento")
        );
    }

}
