package Entity;

public class Ricetta {
    private String nome;
    private String descrizione;
    private int tempoPreparazione;
    private String autore;

    public Ricetta(String nome, String descrizione, int tempoPreparazione, String autore) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.tempoPreparazione = tempoPreparazione;
        this.autore = autore;
    }

    public String getNome() {
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
}
