package Entity;

import Entity.Enum.UnitaIngrediente;
import javafx.beans.property.adapter.JavaBeanBooleanPropertyBuilder;

public class Ingrediente {
    private int idIngrediente;
    private String nome;
    private String allergeni;
    private String categoria;
    private int quantita;
    private UnitaIngrediente unita;

    public Ingrediente(int id, String nome, String allergeni, String categoria) {
        this.idIngrediente = id;
        this.nome = nome;
        this.allergeni = allergeni;
        this.categoria = categoria;
    }

    public Ingrediente(int idIngrediente,String nome, String allergeni, String categoria,int quantita, UnitaIngrediente unita) {
        this.idIngrediente = idIngrediente;
        this.nome = nome;
        this.allergeni = allergeni;
        this.categoria = categoria;
        this.quantita = quantita;
        this.unita = unita;
    }

    public Ingrediente(String nome, String allergeni, String categoria,int quantita, UnitaIngrediente unita) {
        this.nome = nome;
        this.allergeni = allergeni;
        this.categoria = categoria;
        this.quantita = quantita;
        this.unita = unita;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public String getNome() {
        return nome;
    }

    public String getAllergeni() {
        return allergeni;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getQuantita() {
        return quantita;
    }
    public UnitaIngrediente getUnita() {
        return unita;
    }
}
