package DAO.Interfaces;

import Entity.FoglioAdesione;
import Entity.SessionePresenza;
import Entity.Studente;
import Entity.Utente;

import java.sql.SQLException;
import java.util.ArrayList;

public interface foglioAdesioneDAOInterface {
    // Methods
    void insertFoglioDiAdesione(String pathFile, SessionePresenza sessionePresenza, Studente studente) throws SQLException;
    
    
    // Get methods
    ArrayList<FoglioAdesione> getFogliAdesioneByIdSessione(int idsessione) throws SQLException;
    FoglioAdesione getFoglioAdesioneBySessioneNPath(String path, SessionePresenza sessionePresenza, Studente studente) throws SQLException;
}
