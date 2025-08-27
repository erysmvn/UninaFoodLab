package DAO.Interfaces;

import Entity.Studente;
import Entity.TipologiaCorso;

import java.sql.SQLException;
import java.util.ArrayList;

public interface TipologiaCorsoDAOInterface {
    // Methods
    public TipologiaCorso addNewTipologiaCorso(String nomeTipo);

    // Get methods
    public ArrayList<TipologiaCorso> getAll();
    public TipologiaCorso getTipologiaByName(String nomeTipo);
}
