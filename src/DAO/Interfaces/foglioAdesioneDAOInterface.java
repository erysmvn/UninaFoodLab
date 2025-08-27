package DAO.Interfaces;

import Entity.SessionePresenza;

public interface foglioAdesioneDAOInterface {
    public void insertFoglioDiAdesione(String pathFile, SessionePresenza sessionePresenza);
}
