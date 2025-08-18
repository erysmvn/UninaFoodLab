package DAO.Interfaces;

import Entity.Studente;

import java.sql.SQLException;

public interface StudenteDAOInterface {
    public Studente tryLogin(String sql) throws SQLException;
    public Studente tryRegister(Studente studente) throws SQLException;
}
