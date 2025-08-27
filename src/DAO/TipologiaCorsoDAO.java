package DAO;

import Controller.Controller;
import DAO.Interfaces.TipologiaCorsoDAOInterface;
import DB.DBConnection;
import Entity.TipologiaCorso;

import java.sql.*;
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
    public TipologiaCorso addNewTipologiaCorso(String nomeTipo) {
        String checkSql = "SELECT COUNT(*) FROM tipologiaCorso WHERE nome_tipo = '" + nomeTipo + "'";
        try {
            ResultSet rs = stmt.executeQuery(checkSql);
            if (rs.next()) {
                int count = rs.getInt(1);
                if (count == 0) {
                    String sql = "INSERT INTO tipologiacorso (nome_tipo) VALUES (?)";
                    try {
                        PreparedStatement pstmt = con.prepareStatement(sql);
                        pstmt.setString(1, nomeTipo);

                        int rowsInserted = pstmt.executeUpdate();
                        if (rowsInserted == 0) {
                            Exception exc  = new Exception("No row inserted");
                            throw exc;
                        }
                        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                            if (generatedKeys.next()) {
                                int id = generatedKeys.getInt(1);
                                TipologiaCorso tp = new TipologiaCorso(id, nomeTipo);
                                return tp;
                            } else {
                                throw new SQLException("Creating tipologiaCorso failed, no ID obtained.");
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                } else {
                    return getTipologiaByName(nomeTipo);
                }
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }


    // Get Methods
    public TipologiaCorso getTipologiaByName(String nomeTipo) {
        String sql = "SELECT * FROM tipologiacorso WHERE nome_tipo = ?";

        try {
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nomeTipo);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("idtipologiacorso");
                String nome = rs.getString("nome_tipo");
                return new TipologiaCorso(id, nome);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return null;
    }

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
