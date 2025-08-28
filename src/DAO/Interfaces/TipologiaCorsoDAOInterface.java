package DAO.Interfaces;

import Entity.Studente;
import Entity.TipologiaCorso;

import java.sql.SQLException;
import java.util.ArrayList;

public interface TipologiaCorsoDAOInterface {
    // Methods
    TipologiaCorso addNewTipologiaCorso(String nomeTipo);

    // Get methods
    ArrayList<TipologiaCorso> getAll();
    TipologiaCorso getTipologiaByName(String nomeTipo);
}
