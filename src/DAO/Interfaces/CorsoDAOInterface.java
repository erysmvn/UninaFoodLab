package DAO.Interfaces;

import Entity.Chef;
import Entity.Corso;
import Exception.CorsoExceptions.corsiNotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public interface CorsoDAOInterface {
    // Methods
    Corso createNewCorso(String nome, double price, int frequenza, String difficolta);
    void addToCaratterizzato(int idcorso, int idtipologia);
    void addChefToCorso(int idCorso, Chef chef);
    void delete(Corso corso);
    void setChefs(Corso corso);
    void update(Corso corso);
    void prepareChefs(int idCorso, int idChef);

    // Get methods
    Corso getCorsoByResultSetWithOutSessioni(ResultSet rs)throws corsiNotFoundException, SQLException;
    ArrayList<Corso> getCorsiConPiuStudenti(int numeroCorsi);
    Corso getCorsoByTitle(String Title);
    ArrayList<Corso> getAllCourses();
    void getRicetteTrattate(Corso corso);
    Corso getCorsoByIdCorso(int idcorso);
    ArrayList<Corso> searchCorsiLikeString(String nomeCorso) throws corsiNotFoundException, SQLException;
    ArrayList<Corso> searchCorsiByTipologia(String tipologia)throws corsiNotFoundException, SQLException;
    ArrayList<Corso> searchCorsiByChef(String nomeChef)throws corsiNotFoundException,SQLException;
    Corso getCorsoByNome(String nome) throws corsiNotFoundException, SQLException;

}
