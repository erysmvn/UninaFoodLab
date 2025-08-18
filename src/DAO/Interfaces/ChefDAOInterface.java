package DAO.Interfaces;

import Entity.Chef;

import java.sql.SQLException;

public interface ChefDAOInterface {
    public Chef getChefByEmail(String email);
    public Chef getChefByNomeCorso(String nomeCorso);
    public Chef tryLogin(String sql) throws SQLException;
}
