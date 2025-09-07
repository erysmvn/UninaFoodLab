package DAO.Interfaces;

import Entity.FoglioAdesione;
import Entity.SessionePresenza;
import Entity.Utente;

import java.sql.SQLException;
import java.util.ArrayList;

public interface foglioAdesioneDAOInterface {
    // Methods
    void insertFoglioDiAdesione(String pathFile, SessionePresenza sessionePresenza, Utente utente) throws SQLException;
    
    
    // Get methods
    ArrayList<FoglioAdesione> getFogliAdesioneByIdSessione(int idsessione) throws SQLException;
    FoglioAdesione getFoglioAdesioneBySessioneNPath(String path, SessionePresenza sessionePresenza, Utente utente) throws SQLException;
}
