package DAO.Interfaces;

import Entity.Studente;

import java.sql.SQLException;

public interface StudenteDAOInterface {
    Studente tryLogin(String sql) throws SQLException;
}
