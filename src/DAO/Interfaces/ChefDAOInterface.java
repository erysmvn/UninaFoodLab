package DAO.Interfaces;

import Entity.Chef;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ChefDAOInterface {
    // Methods
    public Chef login(String email, String password) throws SQLException;
    public Chef register(Chef chef) throws SQLException;
    public void addChefToCorso(int idCorso, Chef chef);

    // Get methods
    public Chef getChefByEmail(String email);
    public Chef getChefByNomeCorso(String nomeCorso);
    public Chef getChefDaAggiungereToNuovoCorso(String nome, String cognome, String email);
    public ArrayList<Chef> getAll() throws SQLException;
}
