package DAO;

import Controller.Controller;
import DAO.Interfaces.TipologiaCorsoDAOInterface;
import DB.DBConnection;
import Entity.TipologiaCorso;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TipologiaCorsoDAO implements TipologiaCorsoDAOInterface {
    DBConnection dbc;
    Statement stmt;
    ResultSet rs;
    Connection con;
    Controller controller;

    // Constructors
    public TipologiaCorsoDAO(Controller controller) {
        this.dbc = controller.getDBConnection();
        con = dbc.getConnection();
        stmt = dbc.getStatement();
        this.controller = controller;
    }

    // Methods

    // Get Methods
    public ArrayList<TipologiaCorso> getAll() {
        ArrayList<TipologiaCorso> lista = new ArrayList<>();
        String sql = "SELECT * FROM TipologiaCorso";
        try {
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                TipologiaCorso tipologiaCorso = new TipologiaCorso(
                        rs.getInt("idtipologiacorso"),
                        rs.getString("Nome_tipo"),
                        rs.getString("descrizione")
                );
                lista.add(tipologiaCorso);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return lista;
    }
}
