package DAO.Interfaces;

import Entity.Ricetta;
import Entity.Sessione;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface SessioneDAOInterface {

    void insertRicettaToSessione(Ricetta ricetta, Sessione sessione)throws SQLException;

    Sessione createSessioneByResultSet(ResultSet rs)throws SQLException;

    ArrayList<Sessione> getSessioniByIdCorso(int idcorso) throws SQLException;
}
