package Entity;

import Entity.Enum.UnitaIngrediente;

public class IngredienteFormaRicetta {
    Ingrediente ingrediente;
    Ricetta ricetta;
    int quantita;
    UnitaIngrediente  unitaIngrediente;

    public  IngredienteFormaRicetta(Ingrediente ingrediente, Ricetta ricetta, int quantita,  UnitaIngrediente  unitaIngrediente) {
        this.ingrediente = ingrediente;
        this.ricetta = ricetta;
        this.quantita = quantita;
        this.unitaIngrediente = unitaIngrediente;
    }

    public Ingrediente getIngrediente(){
        return ingrediente;
    }
    public Ricetta getRicetta(){
        return ricetta;
    }
    public int getQuantita(){
        return quantita;
    }
    public UnitaIngrediente getUnitaIngrediente(){
        return unitaIngrediente;
    }

}
