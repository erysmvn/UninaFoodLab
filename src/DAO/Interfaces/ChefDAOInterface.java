package DAO.Interfaces;

import Entity.Chef;
import Entity.Corso;
import Exception.UserExceptions.ChangePasswordException.changePasswordException;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ChefDAOInterface {
    // Methods
    Chef login(String email, String password) throws SQLException;
    Chef register(Chef chef) throws SQLException;
    void checkOldPassword(String oldPassword, Chef chef) throws SQLException, changePasswordException;
<<<<<<< HEAD
    void changeUserPassword(String newPassword, Chef chef) throws changePasswordException, SQLException;
=======

    void changeUserPassword(String newPassword, Chef chef) throws SQLException, changePasswordException;

>>>>>>> Carmine
    void setCorsiToChef(Chef chef) throws SQLException;


    // Get methods
    Chef getChefDaAggiungereToNuovoCorso(String nome, String cognome, String email) throws SQLException;


}
