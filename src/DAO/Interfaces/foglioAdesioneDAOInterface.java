package DAO.Interfaces;

import Entity.FoglioAdesione;
import Entity.SessionePresenza;

import java.sql.SQLException;
import java.util.ArrayList;

public interface foglioAdesioneDAOInterface {
    ArrayList<FoglioAdesione> getFogliAdesioneByIdSessione(int idsessione) throws SQLException;

    FoglioAdesione getFoglioAdesioneBySessioneNPath(String path, SessionePresenza sessionePresenza) throws SQLException;

    void insertFoglioDiAdesione(String pathFile, SessionePresenza sessionePresenza) throws SQLException;
}
