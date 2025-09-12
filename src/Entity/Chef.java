package Entity;

import java.util.ArrayList;

public class Chef extends Utente {
    private int idchef;

    private ArrayList<Corso> corsi = new ArrayList<Corso>();

    public Chef(int id, String nome_chef, String cognome, String email, String password) {
        super(nome_chef, cognome, email, password);
        this.idchef = id;
    }

    public Chef(String nome_chef, String cognome, String email, String password) {
        super(nome_chef, cognome, email, password);
    }

    public int getIdchef() {
        return idchef;
    }
    public void setIdchef(int idchef) {
        this.idchef = idchef;
    }
    public void addCorso(Corso corso) {
        corsi.add(corso);
    }
    public ArrayList<Corso> getCorsi() {
        return corsi;
    }

}
