package DAO.Interfaces;

import Entity.Ingrediente;
import Entity.Ricetta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface RicettaDAOInterface {
    // Methods
    Ricetta createRicettaByResulSet(ResultSet rs) throws SQLException;

    // Get methods
    void getIngredienti(Ricetta ricetta) throws SQLException;
    ArrayList<Ricetta> getRicetteByIdSessione(int idsessione) throws SQLException;
    String getQuantitaIngrediente(Ricetta ricetta, Ingrediente ingrediente) throws SQLException;
    void getAllergeniRicetta(Ricetta ricetta) throws SQLException;
}
