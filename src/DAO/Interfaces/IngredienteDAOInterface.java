package DAO.Interfaces;

import Entity.Ingrediente;

import java.sql.SQLException;
import java.util.ArrayList;

public interface IngredienteDAOInterface {
    ArrayList<Ingrediente> getAllIngredientes() throws SQLException;

    void insertIngrediente(Ingrediente ing)throws SQLException;
}
