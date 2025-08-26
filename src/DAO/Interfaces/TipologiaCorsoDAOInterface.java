package DAO.Interfaces;

import Entity.Studente;
import Entity.TipologiaCorso;

import java.sql.SQLException;
import java.util.ArrayList;

public interface TipologiaCorsoDAOInterface {
    // Methods

    // Get methods
    public ArrayList<TipologiaCorso> getAll();
}
