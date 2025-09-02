package Entity;

import java.util.ArrayList;

public class Ricetta {
    private int idricetta;
    private String nome;
    private String descrizione;
    private int tempoPreparazione;
    private String autore;
    private ArrayList<Ingrediente> ingredienti;

    private ArrayList<IngredienteFormaRicetta> ingredienteFormaRicetta;

    private ArrayList<String> allergeniRicetta;


    public Ricetta(int id, String nome, String descrizione, int tempoPreparazione, String autore) {
        this.idricetta = id;
        this.nome = nome;
        this.descrizione = descrizione;
        this.tempoPreparazione = tempoPreparazione;
        this.autore = autore;

        ingredienteFormaRicetta = new ArrayList<>();
    }

    public Ricetta() {
        ingredienti = new ArrayList<>();
        ingredienteFormaRicetta = new ArrayList<>();
    }


    public void setNomeRicetta(String nomeRicetta) {
        this.nome = nomeRicetta;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    public void setTempoPreparazione(int tempoPreparazione) {
        this.tempoPreparazione = tempoPreparazione;
    }
    public String getNome(){
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public int getTempoPreparazione() {
        return tempoPreparazione;
    }

    public String getAutore() {
        return autore;
    }

    public int getIdRicetta() {
        return idricetta;
    }

    public ArrayList<Ingrediente> getIngredienti() {
        return ingredienti;
    }

    public void allocaArrayIngredienti(){
        ingredienti = new ArrayList<>();
    }

    public void addIngrediente(Ingrediente ingrediente) {
        ingredienti.add(ingrediente);
    }

    public ArrayList<String> getAllergeniRicetta() {
        return allergeniRicetta;
    }

    public void allocaArrayAllergeniRicetta(){
        allergeniRicetta = new ArrayList<>();
    }

    public void addAllergeniRicetta(String newAllergene) {
        this.allergeniRicetta.add(newAllergene);
    }

    public String getAllergeniRicettaString(){
        String toreturn = "";
        for (String allergene : allergeniRicetta) {
            toreturn += allergene + ", ";
        }
        if (toreturn.length() > 2) {
            toreturn = toreturn.substring(0, toreturn.length() - 2);
        } else {
            toreturn = "Nessuno";
        }
        return toreturn;
    }


    public void setIngredienti(ArrayList<Ingrediente> ingredienti) {
        this.ingredienti = ingredienti;
    }
    public void setIngrediente(Ingrediente ingrediente) {
        this.ingredienti.add(ingrediente);
    }
    public void setIdRicetta(int id)  {
        this.idricetta = id;
    }

    public ArrayList<IngredienteFormaRicetta> getIngredienteFormaRicetta() {
        return ingredienteFormaRicetta;
    }
    public void addIngredienteFormaRicetta(IngredienteFormaRicetta ingFormaRicetta) {
        ingredienteFormaRicetta.add(ingFormaRicetta);
    }

}
