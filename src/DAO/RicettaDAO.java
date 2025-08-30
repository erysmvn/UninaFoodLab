package DAO;

import Controller.Controller;
import DAO.Interfaces.RicettaDAOInterface;
import DB.DBConnection;
import Entity.Ingrediente;
import Entity.Ricetta;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RicettaDAO implements RicettaDAOInterface {
    DBConnection dbc;

    Connection con;
    Controller controller;

    // Constructors
    public RicettaDAO(Controller controller) {
        this.dbc = controller.getDBConnection();
        con = dbc.getConnection();
        this.controller = controller;
    }

    public void insertRicetta(Ricetta ricetta) throws SQLException {
        String sql = "INSERT INTO RICETTA(nome_ricetta, descrizione_ricetta,tempo_di_preparazione,autore) VALUES (?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1,ricetta.getNome());
        ps.setString(2,ricetta.getDescrizione());
        ps.setInt(3,ricetta.getTempoPreparazione());
        ps.setString(4,ricetta.getAutore());
        ps.executeUpdate();
    }

    public void inserisciIngredientiToRicetta(Ricetta ricetta) throws SQLException {
        this.insertRicetta(ricetta);
        String sql = "INSERT INTO FORMA (idRicetta, idIngrediente, \"unità\", Quantità) " +
                "SELECT r.idRicetta, i.idIngrediente, ?::\"unità_ingrediente\", ? " +
                "FROM Ricetta r " +
                "JOIN Ingrediente i ON i.nome_ingrediente = ? " +
                "WHERE r.nome_ricetta = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            for (Ingrediente ingrediente : ricetta.getIngredienti()) {
                pstmt.setString(1, ingrediente.getUnita().getDbValue());
                pstmt.setInt(2, ingrediente.getQuantita());
                pstmt.setString(3, ingrediente.getNome());
                pstmt.setString(4, ricetta.getNome());

                pstmt.executeUpdate();
            }
        }
    }


    // Methods
    @Override
    public Ricetta createRicettaByResulSet(ResultSet rs) throws SQLException {
        Ricetta ricetta =  new Ricetta(
                rs.getInt("idricetta"),
                rs.getString("nome_ricetta"),
                rs.getString("descrizione_ricetta"),
                rs.getInt("tempo_di_preparazione"),
                rs.getString("autore")
        );
        getIngredienti(ricetta);
        getAllergeniRicetta(ricetta);
        return ricetta;
    }

    // Get Methods
    @Override
    public void getIngredienti(Ricetta ricetta) {
        ricetta.allocaArrayIngredienti();
        Ingrediente ingrediente = null;

        String sql = "SELECT DISTINCT idIngrediente, nome_ingrediente, allergeni, categoria " +
                "FROM ingrediente " +
                "NATURAL JOIN forma " +
                "NATURAL JOIN ricetta " +
                "WHERE idricetta = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, ricetta.getIdRicetta());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ingrediente = new Ingrediente(
                            rs.getInt("idIngrediente"),
                            rs.getString("nome_ingrediente"),
                            rs.getString("allergeni"),
                            rs.getString("categoria")
                    );
                    ricetta.addIngrediente(ingrediente);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    @Override
    public ArrayList<Ricetta> getRicetteByIdSessione(int idsessione) throws SQLException{
        ArrayList<Ricetta> ricette = new ArrayList<>();
        String sql = "select * from ricetta natural join tratta natural join sessione s where idsessione = ?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, idsessione);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            ricette.add(createRicettaByResulSet(rs));
        }
        return ricette;
    }

    @Override
    public String getQuantitaIngrediente(Ricetta ricetta, Ingrediente ingrediente) {
        String sql = "SELECT quantità, unità " +
                "FROM forma " +
                "NATURAL JOIN ricetta " +
                "NATURAL JOIN ingrediente " +
                "WHERE idricetta = ? AND idingrediente = ?";

        String toReturn = "";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, ricetta.getIdRicetta());
            pstmt.setInt(2, ingrediente.getIdIngrediente());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    toReturn = rs.getString("quantità") + " " + rs.getString("unità");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        return toReturn;
    }

    @Override
    public void getAllergeniRicetta(Ricetta ricetta) {
        ricetta.allocaArrayAllergeniRicetta();
        String sql = "SELECT DISTINCT allergeni " +
                "FROM ingrediente " +
                "NATURAL JOIN forma " +
                "NATURAL JOIN ricetta " +
                "WHERE idricetta = ?";

        Set<String> allergeniSet = new HashSet<>();

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, ricetta.getIdRicetta());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String allergeniStr = rs.getString("allergeni");
                    if (allergeniStr != null && !allergeniStr.isEmpty()) {
                        String[] allergeniArray = allergeniStr.split("\\s*,\\s*");
                        for (String allergene : allergeniArray) {
                            if (!"Nessuno".equalsIgnoreCase(allergene)) {
                                allergeniSet.add(allergene);
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }

        for (String allergene : allergeniSet) {
            ricetta.addAllergeniRicetta(allergene);
        }
    }
}