package Entity;

import java.util.ArrayList;

public class Utente {
    String nome;
    String cognome;
    String email;
    String passw;
    private ArrayList<Corso> corsi;

    public Utente(String nome, String cognome, String email, String passw) {}

    public Utente() {
        ArrayList<Corso> corsi = new ArrayList<>();
    }

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
    public void setCorsi(ArrayList<Corso> corsi) {
        this.corsi = corsi;
    }
}