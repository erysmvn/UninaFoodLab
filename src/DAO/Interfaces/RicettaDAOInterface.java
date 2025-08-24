package DAO.Interfaces;

import Entity.Ingrediente;
import Entity.Ricetta;

public interface RicettaDAOInterface {
    // Methods

    // Get methods
    public void getIngredienti(Ricetta ricetta);
    public String getQuantitaIngrediente(Ricetta ricetta, Ingrediente ingrediente);
    public void getAllergeniRicetta(Ricetta ricetta);
}
