package DAO;

import Controller.Controller;
import DAO.Interfaces.IngredienteDAOInterface;
import DB.DBConnection;
import Entity.Ingrediente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class IngredienteDAO implements IngredienteDAOInterface {

    Controller controller;
    DBConnection dbc;
    Connection con;

    public IngredienteDAO(Controller controller){
        this.controller = controller;
        this.dbc = controller.getDBConnection();
        this.con = dbc.getConnection();
    }

    private boolean checkIfAlreadyExists(Ingrediente ingrediente) throws SQLException {
        String sql = "select idingrediente from Ingrediente where nome_ingrediente = ?";

        PreparedStatement pstmt = con.prepareStatement(sql);
        pstmt.setString(1,ingrediente.getNome());
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            int id = rs.getInt("idingrediente");
            ingrediente.setIdingrediente(id);
            return true;
        }
        return false;
    }

    @Override
    public void insertIngrediente(Ingrediente ing) throws SQLException {
        String sql = "INSERT INTO Ingrediente (nome_ingrediente, allergeni, categoria) VALUES (?, ?, ?) RETURNING idingrediente";

        PreparedStatement pstmt = con.prepareStatement(sql);

        if (!this.checkIfAlreadyExists(ing)){
            pstmt.setString(1, ing.getNome());
            pstmt.setString(2, ing.getAllergeni());
            pstmt.setString(3, ing.getCategoria());

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("idingrediente");
                ing.setIdingrediente(id);
            }
        }
    }


    @Override
    public ArrayList<Ingrediente> getAllIngredientes()throws SQLException {
        ArrayList<Ingrediente> ingredienti = new ArrayList<>();
        String sql = "SELECT * FROM ingrediente ";
        PreparedStatement pstmt = con.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            ingredienti.add(createIngredienteByResultSet(rs));
        }
        return ingredienti;
    }

    private Ingrediente createIngredienteByResultSet(ResultSet rs) throws SQLException {
        return new Ingrediente(
                rs.getInt("idingrediente"),
                rs.getString("nome_ingrediente"),
                rs.getString("allergeni"),
                rs.getString("categoria")
        );
    }

}

