package DAO.Interfaces;

import Entity.FoglioAdesione;
import Entity.SessionePresenza;
import Exception.FoglioAdesioneException;

import java.sql.SQLException;
import java.util.ArrayList;

public interface foglioAdesioneDAOInterface {
    ArrayList<FoglioAdesione> getFogliAdesioneByIdSessione(int idsessione);

    FoglioAdesione getFoglioAdesioneBySessioneNPath(String path, SessionePresenza sessionePresenza);

    void insertFoglioDiAdesione(String pathFile, SessionePresenza sessionePresenza)throws SQLException;
}
