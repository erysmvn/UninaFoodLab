package Controller;

import Entity.*;
import DB.DBConnection;
import GUI.Stages.*;
import GUI.Pane.*;
import DAO.*;

import javafx.application.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {
    private HomePage homePage;
    private LoginPage loginPage;
    private AccountPage accountPage;
    private RegisterPage registerPage;

    private DBConnection dbc;

    private Utente utente;

    private ArrayList<CorsoPage> corsoPages = new ArrayList<>();

    public Controller(){
        dbc = new DBConnection();
        dbc.DBConnect();
    }

    public DBConnection getDBConnection(){
        return dbc;
    }

    public Utente getUtente(){
        return utente;
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

    public CorsoDAO getCorsoDAO(){
        return new CorsoDAO(this);
    }
    public ChefDAO getChefDAO(){
        return new ChefDAO(this);
    }
    public StudenteDAO getStudenteDAO(){
        return new StudenteDAO(this);
    }

    public void tryLoginChef(String sql) throws SQLException{
        ChefDAO chefDao = new ChefDAO(this);
        Chef chef = chefDao.tryLogin(sql);
        if(chef != null){
            homePage.becomeHomePageChef(chef);
            this.utente = chef;
        }
    }

    public void tryRegisterChef(Chef chef) throws SQLException{
        ChefDAO chefDao = new ChefDAO(this);
        Chef ch = chefDao.tryRegister(chef);
        if(ch != null) {
            homePage.becomeHomePageChef(ch);
            this.utente = ch;
        }
    }

    public void tryLoginStudente(String sql) throws SQLException{
        StudenteDAO studenteDao = new StudenteDAO(this);
        Studente studente = studenteDao.tryLogin(sql);
        if(studente != null) {
            homePage.becomeHomePageStudente(studente);
            this.utente = studente;
        }
    }

    public void tryRegisterStudente(Studente studente) throws SQLException{
        StudenteDAO studenteDao = new StudenteDAO(this);
        Studente stud = studenteDao.tryRegister(studente);
        if(stud != null){
            homePage.becomeHomePageStudente(stud);
            this.utente = stud;
        }
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

        public void openCorsoPage(Corso corso){
            CorsoPage existingPage = isCorsoPageAlreadyOpened(corso);

            if(existingPage != null){
                if(existingPage.isShowing()){
                    existingPage.toFront();
                } else {
                    existingPage.show();
                }
            } else {
                ChefDAO chefDao = new ChefDAO(this);
                Chef chef = chefDao.getChefByNomeCorso(corso.getNome());
                CorsoPage corsoPage = new CorsoPage( this);
                corsoPage.initPage(corso,chef);
                corsoPages.add(corsoPage);

                corsoPage.setOnCloseRequest(e -> corsoPages.remove(corsoPage));

                corsoPage.show();
            }
        }

    private CorsoPage isCorsoPageAlreadyOpened(Corso c){
        for(CorsoPage cp : corsoPages){
            if(cp.getCorso().getIdCorso() == c.getIdCorso()){
                return cp;
            }
        }
        return null;

    }
    public void openAccountPage(Utente utente) {
        if(accountPage == null || !accountPage.isShowing()) {
            accountPage = new AccountPage(this);
            accountPage.initPage(utente);
            accountPage.show();
        } else {
            accountPage.toFront();
        }
    }

    public void openRegisterPage() {
        if(registerPage == null || !registerPage.isShowing()) {
            registerPage = new RegisterPage(this);
            registerPage.show();
        } else {
            registerPage.toFront();
        }
    }

    public void subscribeToCourse(Corso corso){
        if (utente instanceof Studente studente) {
            StudenteDAO studenteDao = new StudenteDAO(this);
            studenteDao.subscribeToCourse(studente, corso);
            studente.addCorso(corso);
        }
    }

    public Boolean alreadySubscribed(Corso corso){
        if (utente instanceof Studente studente) {
            StudenteDAO studenteDao = new StudenteDAO(this);
            return studenteDao.checkIfSubscribed(studente, corso);
        } else {
            return false;
        }
    }


    public void endAll(){
        Platform.exit();
    }
}
