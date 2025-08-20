package Entity;

import java.util.ArrayList;

public class Chef extends Utente {

    private ArrayList<Corso> corsi = new ArrayList<Corso>();

    public Chef(String nome_chef, String cognome, String email, String password) {
        this.nome = nome_chef;
        this.cognome = cognome;
        this.email = email;
        this.passw = password;
    }

    public void addCorso(Corso corso) {
        corsi.add(corso);
    }
    public ArrayList<Corso> getCorsi() {
        return corsi;
    }
    public void setCorsi(ArrayList<Corso> corsi) {
        this.corsi = corsi;
    }

}
