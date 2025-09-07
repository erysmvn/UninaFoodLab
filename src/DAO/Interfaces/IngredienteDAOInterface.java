package DAO.Interfaces;

import Entity.Ingrediente;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IngredienteDAOInterface {
    // Methods
    void insertIngrediente(Ingrediente ing) throws SQLException;
    

    // Get methods
    ArrayList<Ingrediente> getAllIngredientes() throws SQLException;
}
