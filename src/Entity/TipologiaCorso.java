package Entity;

public class TipologiaCorso {
    private int id;
    private String nome;
    private String descrizione;

    public TipologiaCorso(int id, String nome, String descrizione) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public TipologiaCorso(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }
    public String getDescrizione() {
        return descrizione;
    }
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
