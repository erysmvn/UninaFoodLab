package Entity;

import java.util.ArrayList;

public class Studente extends Utente {
    private String matricola;

    public Studente(String matricola, String nome_studente, String cognome, String email, String password) {
        super(nome_studente, cognome, email, password);
        this.matricola = matricola;
    }

    public String getMatricola() {
        return matricola;
    }
}
