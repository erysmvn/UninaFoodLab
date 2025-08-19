package Controller;

import Entity.*;
import DB.DBConnection;
import GUI.Stages.*;
import GUI.Pane.*;
import DAO.*;

import javafx.application.*;
import java.sql.SQLException;

public class Controller {
    private HomePage homePage;
    private LoginPage loginPage;
    private AccountPage accountPage;
    private RegisterPage registerPage;
    private DBConnection dbc;
    private Utente utente = null;

    public Controller(){
        dbc = new DBConnection();
        dbc.DBConnect();
    }

    public DBConnection getDBConnection(){
        return dbc;
    }

    public void setHomePage(HomePage homePage) {
        this.homePage = homePage;
    }

    public HomePage getHomePage() {
        return homePage;
    }

    public boolean isHomePageChef() {
        return homePage.isChef();
    }

    public boolean isAlreadyLoggedIn() {
        return homePage.isLoggedIn();
    }

    public void tryLoginChef(String sql) throws SQLException{
        ChefDAO chefDao = new ChefDAO(this);
        Chef chef = chefDao.tryLogin(sql);
        if(chef != null)
            homePage.becomeHomePageChef(chef);

    }

    public void tryRegisterChef(Chef chef) throws SQLException{
        ChefDAO chefDao = new ChefDAO(this);
        Chef ch = chefDao.tryRegister(chef);
        if(ch != null)
            homePage.becomeHomePageChef(ch);
    }

    public void tryLoginStudente(String sql) throws SQLException{
        StudenteDAO studenteDao = new StudenteDAO(this);
        Studente studente = studenteDao.tryLogin(sql);
        if(studente != null)
            homePage.becomeHomePageStudente(studente);
    }

    public void tryRegisterStudente(Studente studente) throws SQLException{
        StudenteDAO studenteDao = new StudenteDAO(this);
        Studente stud = studenteDao.tryRegister(studente);
        if(stud != null)
            homePage.becomeHomePageStudente(stud);
    }

    public void logOut(){
        this.utente = null;
        homePage.setLogOut();
        accountPage.close();
    }


    public void openLoginPage(){
        if (loginPage == null || !loginPage.isShowing()){
            if( registerPage != null ){
                registerPage.close();
            }
            loginPage = new LoginPage(this);
            loginPage.show();
        } else {
            loginPage.toFront();
        }
    }

    public void openCorsoPage(String Title, String imagePath, Controller controller){

        CorsoDAO corsoDao = new CorsoDAO(this);
        Corso corso = corsoDao.getCorsoByTitle(Title);
        corso.setImagePath(imagePath);
        ChefDAO chefDao = new ChefDAO( this);
      
        Chef chef = chefDao.getChefByNomeCorso(corso.getNome());
        CorsoPage corsoPage = new CorsoPage(corso, chef, this);

        corsoPage.show();
    }

    public void openAccountPage(Utente utente) {
        if(accountPage == null || !accountPage.isShowing()) {
            accountPage = new AccountPage(utente,this);
            accountPage.show();
        }else{
            accountPage.toFront();
        }
    }

    public void openRegisterPage() {
        if(registerPage == null || !registerPage.isShowing()) {
            registerPage = new RegisterPage(this);
            registerPage.show();
        }else{
            registerPage.toFront();
        }
    }


    public void endAll(){
        Platform.exit();
    }
}
