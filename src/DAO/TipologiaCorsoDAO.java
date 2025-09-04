package DAO;

import Controller.Controller;
import DAO.Interfaces.TipologiaCorsoDAOInterface;
import DB.DBConnection;
import Entity.TipologiaCorso;

import java.sql.*;
import java.util.ArrayList;

public class TipologiaCorsoDAO implements TipologiaCorsoDAOInterface {
    DBConnection dbc;

    Connection con;
    Controller controller;

    // Constructors
    public TipologiaCorsoDAO(Controller controller) {
        this.dbc = controller.getDBConnection();
        con = dbc.getConnection();
        this.controller = controller;
    }

    // Methods
    @Override
    // TODO posso chiamare questa funzione invece di fare get tipo by nome?
    public TipologiaCorso addNewTipologiaCorso(String nomeTipo) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM tipologiacorso WHERE nome_tipo = ?";
        String insertSql = "INSERT INTO tipologiacorso (nome_tipo) VALUES (?)";

        PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setString(1, nomeTipo);

            ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    int count = rs.getInt(1);
                    if (count > 0) {
                        return getTipologiaByName(nomeTipo);
                    }
                }

            PreparedStatement insertStmt = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1, nomeTipo);
                int rowsInserted = insertStmt.executeUpdate();

                if (rowsInserted == 0) {
                    throw new SQLException("Nessuna riga inserita per la nuova tipologia corso.");
                }

                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        int id = generatedKeys.getInt(1);
                        return new TipologiaCorso(id, nomeTipo);
                    } else {
                        throw new SQLException("Creazione tipologiaCorso fallita, nessun ID generato.");
                    }
    }


    // Get Methods
    @Override
    public TipologiaCorso getTipologiaByName(String nomeTipo) throws SQLException {
        String sql = "SELECT * FROM tipologiacorso WHERE nome_tipo = ?";

        PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nomeTipo);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("idtipologiacorso");
                    String nome = rs.getString("nome_tipo");
                    return new TipologiaCorso(id, nome);
                }
            }
        return null;
    }

    @Override
    public ArrayList<TipologiaCorso> getAll() throws SQLException {
        ArrayList<TipologiaCorso> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipologiacorso";

        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                TipologiaCorso tipologiaCorso = new TipologiaCorso(
                        rs.getInt("idtipologiacorso"),
                        rs.getString("nome_tipo"),
                        rs.getString("descrizione")
                );
                lista.add(tipologiaCorso);
            }
        return lista;
    }

}
