package DAO.Interfaces;

import Entity.Chef;

import java.sql.SQLException;

public interface ChefDAOInterface {
    // Methods
    public Chef login(String email, String password) throws SQLException;
    public Chef register(Chef chef) throws SQLException;

    // Get methods
    public Chef getChefByEmail(String email);
    public Chef getChefByNomeCorso(String nomeCorso);
}
