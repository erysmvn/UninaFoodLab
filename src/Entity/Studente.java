package Entity;

import java.util.ArrayList;

public class Studente extends Utente {
    private String matricola;


    public Studente(String matricola, String nome_studente, String cognome, String email, String password) {
        this.matricola = matricola;
        this.nome = nome_studente;
        this.cognome = cognome;
        this.email = email;
        this.passw = password;
    }

    public String getMatricola() {
        return matricola;
    }
}
