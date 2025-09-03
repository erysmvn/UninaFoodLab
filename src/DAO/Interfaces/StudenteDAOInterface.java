package DAO.Interfaces;

import Entity.Corso;
import Entity.Studente;
import Exception.UserExceptions.ChangePasswordException.changePasswordException;

import java.sql.SQLException;
import java.util.ArrayList;

public interface StudenteDAOInterface {
    // Methods
    Studente login(String email, String password) throws SQLException;
    Studente register(Studente studente) throws SQLException;
    Boolean checkIfSubscribed(Studente studente, Corso corso);
    void unsubscribeToCourse(Studente studente, Corso corso);
    void subscribeToCourse(Studente studente, Corso corso);
    void changeUserPassword(String newPassword, Studente studente)throws changePasswordException, SQLException;
    void checkOldPassword(String oldPassword, Studente studente) throws changePasswordException;
    // Get methods
    ArrayList<Corso> getCorsiFromStudente(Studente studente) throws SQLException;

}
