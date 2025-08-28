package DAO.Interfaces;

import Entity.SessionePresenza;
import Exception.FoglioAdesioneException;

import java.sql.SQLException;

public interface foglioAdesioneDAOInterface {
    public void insertFoglioDiAdesione(String pathFile, SessionePresenza sessionePresenza)throws SQLException;
}
