package Entity;

import java.util.ArrayList;

public class Utente {
    protected String nome;
    protected String cognome;
    protected String email;
    protected String passw;
    private ArrayList<Corso> corsi;

    public Utente(String nome, String cognome, String email, String passw) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.passw = passw;
        this.corsi = new ArrayList<>();
    }

    // Getter
    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public String getPassw() {
        return passw;
    }

    public ArrayList<Corso> getCorsi() {
        return corsi;
    }

    // Setter
    public void setCorsi(ArrayList<Corso> corsi) {
        this.corsi = corsi;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

    public void addCorso(Corso corso) {
        corsi.add(corso);
    }

    public void removeCorso(Corso corso) {
        corsi.remove(corso);
    }
}