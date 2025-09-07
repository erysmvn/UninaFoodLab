package DAO.Interfaces;

import Entity.Chef;
import Entity.Corso;
import Exception.CorsoExceptions.corsiNotFoundException;

import java.sql.SQLException;
import java.util.ArrayList;

public interface CorsoDAOInterface {
    // Methods
    Corso createNewCorso(String nome, double price, int frequenza, String difficolta) throws SQLException;
    void addToCaratterizzato(int idcorso, int idtipologia) throws SQLException;
    void deleteOtherChefs(int idCorso, int idChef) throws SQLException;
    void addChefToCorso(int idCorso, Chef chef) throws SQLException;
    void setChefs(Corso corso) throws SQLException;
    void delete(Corso corso) throws SQLException;
    void update(Corso corso) throws SQLException;

    
    // Get methods

    ArrayList<Corso> getCorsiConPiuStudenti(int numeroCorsiLimite) throws SQLException;
    ArrayList<Corso> searchCorsiLikeNomeTipologia(String tipologia) throws SQLException, corsiNotFoundException;
    ArrayList<Corso> searchCorsiLikeNomeCorso(String nomeCorso) throws SQLException, corsiNotFoundException;
    ArrayList<Corso> searchCorsiLikeNomeChef(String nomeChef) throws SQLException, corsiNotFoundException;
    ArrayList<Corso> getAllCourses() throws SQLException;

    Corso getCorsoByTitle(String Title) throws SQLException;
    Corso getCorsoByIdCorso(int idcorso) throws SQLException;
    Corso getCorsoByNome(String nome) throws SQLException, corsiNotFoundException;

    void getRicetteTrattate(Corso corso) throws SQLException;
}
