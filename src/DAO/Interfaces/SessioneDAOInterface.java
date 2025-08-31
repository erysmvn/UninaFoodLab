package DAO.Interfaces;

import Entity.Ingrediente;
import Entity.Ricetta;
import Entity.Sessione;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface SessioneDAOInterface {

    ArrayList<Sessione> getSessioniByNomeCorso(String nomeCorso)throws SQLException;

    void insertRicettaToSessione(Ricetta ricetta, Sessione sessione)throws SQLException;

    Sessione createSessioneByResultSet(ResultSet rs)throws SQLException;

    void update(Sessione sessione)throws SQLException;

}
