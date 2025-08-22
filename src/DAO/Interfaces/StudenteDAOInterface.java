package DAO.Interfaces;

import Entity.Studente;

import java.sql.SQLException;

public interface StudenteDAOInterface {
    // Methods
    public Studente login(String email, String password) throws SQLException;
    public Studente register(Studente studente) throws SQLException;

    // Get methods
}
