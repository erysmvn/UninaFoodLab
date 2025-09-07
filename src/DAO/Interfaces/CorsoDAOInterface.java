package DAO.Interfaces;

import Entity.Chef;
import Entity.Corso;
import Exception.CorsoExceptions.corsiNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CorsoDAOInterface {
    // Methods
    Corso createNewCorso(String nome, double price, int frequenza, String difficolta) throws SQLException;
    void addToCaratterizzato(int idcorso, int idtipologia) throws SQLException;
    void addChefToCorso(int idCorso, Chef chef) throws SQLException;
    void delete(Corso corso) throws SQLException;
    void setChefs(Corso corso) throws SQLException;
    void update(Corso corso) throws SQLException;
    void prepareChefs(int idCorso, int idChef) throws SQLException;

    
    // Get methods
    ArrayList<Corso> getCorsiConPiuStudenti(int numeroCorsi) throws SQLException;
    Corso getCorsoByTitle(String Title) throws SQLException;
    ArrayList<Corso> getAllCourses() throws SQLException;
    void getRicetteTrattate(Corso corso) throws SQLException;
    Corso getCorsoByIdCorso(int idcorso) throws SQLException;
    ArrayList<Corso> searchCorsiLikeString(String nomeCorso) throws SQLException, corsiNotFoundException;
    ArrayList<Corso> searchCorsiByTipologia(String tipologia) throws SQLException, corsiNotFoundException;
    ArrayList<Corso> searchCorsiByChef(String nomeChef) throws SQLException, corsiNotFoundException;
    Corso getCorsoByNome(String nome) throws SQLException, corsiNotFoundException;
}
