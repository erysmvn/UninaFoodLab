package DAO.Interfaces;

import Entity.Chef;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ChefDAOInterface {
    // Methods
    public Chef login(String email, String password) throws SQLException;
    public Chef register(Chef chef) throws SQLException;

    // Get methods
    public Chef getChefByEmail(String email);
    public Chef getChefByNomeCorso(String nomeCorso);
    public ArrayList<Chef> getAll() throws SQLException;
}
