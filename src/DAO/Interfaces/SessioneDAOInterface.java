package DAO.Interfaces;

import Entity.Sessione;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface SessioneDAOInterface {

    ArrayList<Sessione> getSessioniByNomeCorso(String nomeCorso)throws SQLException;

    Sessione createSessioneByResultSet(ResultSet rs)throws SQLException;
}
