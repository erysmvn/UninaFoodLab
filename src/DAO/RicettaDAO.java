package DAO;

import Controller.Controller;
import DB.DBConnection;
import Entity.Chef;
import Entity.Ingrediente;
import Entity.Ricetta;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class RicettaDAO {
    DBConnection dbc;
    Statement stmt;
    ResultSet rs;
    Connection con;
    Controller controller;

    // Constructors
    public RicettaDAO(Controller controller) {
        this.dbc = controller.getDBConnection();
        con = dbc.getConnection();
        stmt = dbc.getStatement();
        this.controller = controller;
    }


    // Methods


    // Get Methods
    public void getIngredienti(Ricetta ricetta){
        ricetta.allocaArrayIngredienti();
        Ingrediente ingrediente = null;
        String sql = "SELECT DISTINCT idIngrediente, nome_ingrediente, allergeni, categoria " +
                "FROM ingrediente NATURAL JOIN forma NATURAL JOIN ricetta " +
                "WHERE idricetta = " + "'" +ricetta.getIdRicetta()+"'";

        try  {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    ingrediente = new Ingrediente(
                            rs.getInt("idingrediente"),
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

    public String getQuantitaIngrediente(Ricetta ricetta, Ingrediente ingrediente) {
        ricetta.allocaArrayIngredienti();
        String sql = "SELECT quantità, unità " +
                "FROM forma NATURAL JOIN ricetta NATURAL JOIN ingrediente " +
                "WHERE idricetta = " + "'" +ricetta.getIdRicetta()+"' AND idingrediente = " + "'" +ingrediente.getIdIngrediente()+"'";
        String toReturn = "";
        try  {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    toReturn += rs.getString("quantità");
                    toReturn += " ";
                    toReturn += rs.getString("unità");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return toReturn;
    }

    public void getAllergeniRicetta(Ricetta ricetta) {
        ricetta.allocaArrayAllergeniRicetta();
        String sql = "SELECT DISTINCT allergeni " +
                "FROM ingrediente NATURAL JOIN forma NATURAL JOIN ricetta " +
                "WHERE idricetta = " + "'" +ricetta.getIdRicetta()+"'";
        Set<String> allergeniSet = new HashSet<>(); // set temporaneo
        try  {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    String allergeniStr = rs.getString("allergeni");
                    if (allergeniStr != null && !allergeniStr.isEmpty()) {
                        // Divido la stringa per virgole e rimuovo eventuali spazi
                        String[] allergeniArray = allergeniStr.split("\\s*,\\s*");
                        for (String allergene : allergeniArray) {
                            allergeniSet.add(allergene); // aggiunge solo se non c'è già
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
            if (!"Nessuno".equals(allergene)) {
                ricetta.addAllergeniRicetta(allergene);
            }
        }
    }
}
