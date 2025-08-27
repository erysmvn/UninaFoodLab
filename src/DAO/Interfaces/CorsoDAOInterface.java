package DAO.Interfaces;

import Entity.Corso;

public interface CorsoDAOInterface {
    // Methods
    public Corso createNewCorso(String nome, double price, int frequenza, String difficolta);
    public void addToCaratterizzato(int idcorso, int idtipologia);

    // Get methods
    public Corso getCorsoByTitle(String Title);
}
