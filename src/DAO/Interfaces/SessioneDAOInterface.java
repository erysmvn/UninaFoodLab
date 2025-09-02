package DAO.Interfaces;

import Entity.Ricetta;
import Entity.Sessione;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface SessioneDAOInterface {

    void removeRicetta(Ricetta ricetta, Sessione sessione)throws SQLException;

    void insertRicettaToSessione(Ricetta ricetta, Sessione sessione)throws SQLException;

    Sessione createSessioneByResultSet(ResultSet rs)throws SQLException;

    ArrayList<Sessione> getSessioniByIdCorso(int idcorso) throws SQLException;

    void update(Sessione sessione) throws SQLException;

    void delete(Sessione sessione) throws SQLException;
}
