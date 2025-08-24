package Entity;

public class Ingrediente {
    private int idIngrediente;
    private String nome;
    private String allergeni;
    private String categoria;

    public Ingrediente(int id, String nome, String allergeni, String categoria) {
        this.idIngrediente = id;
        this.nome = nome;
        this.allergeni = allergeni;
        this.categoria = categoria;
    }

    public Ingrediente(String nome, String allergeni, String categoria) {
        this.nome = nome;
        this.allergeni = allergeni;
        this.categoria = categoria;
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
}
