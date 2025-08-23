package Controller;

import Entity.*;
import DB.DBConnection;
import GUI.Stages.*;
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


    // GUI
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


    // Pages
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
            CorsoPage corsoPage = new CorsoPage( this);
            corsoPage.initPage(corso);
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



    // User
    public void loginMethod(String email, String password) throws SQLException{

        if (email.contains("@studenti.unina.it")) {
            StudenteDAO studenteDao = getStudenteDAO();
            utente = studenteDao.login(email, password);

        } else {
            ChefDAO chefDao = getChefDAO();
            utente = chefDao.login(email, password);
        }

        homePage.setUtente(utente);
        this.corsoPages.clear();
    }

    public void registerMethod(Utente utente) throws SQLException {

        if (utente instanceof Chef chef) {
            ChefDAO chefDao = getChefDAO();
            Chef ch = chefDao.register(chef);
            if(ch != null){
                this.utente = ch;
            }
        } else if (utente instanceof Studente studente) {
            StudenteDAO studenteDao = getStudenteDAO();
            Studente st = studenteDao.register(studente);
            if(st != null){
                this.utente = st;
            }
        }

        homePage.setUtente(utente);
        this.corsoPages.clear();

    }

    public void logOut(){
        this.utente = null;
        homePage.setLogOut();
        accountPage.close();
    }

    public void subscribeToCourse(Corso corso){
        if (utente instanceof Studente studente) {
            StudenteDAO studenteDao = new StudenteDAO(this);
            studenteDao.subscribeToCourse(studente, corso);
            studente.addCorso(corso);
        }
    }

    public void unsubscribeToCourse(Corso corso){
        if (utente instanceof Studente studente) {
            StudenteDAO studenteDao = new StudenteDAO(this);
            studenteDao.unsubscribeToCourse(studente, corso);
            studente.removeCorso(corso);
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


    // Corso
    public void getRicetteTrattate(Corso corso) {
        CorsoDAO corsoDao = new CorsoDAO(this);
        corsoDao.getRicetteTrattate(corso);
    }

    public void getChefs(Corso corso) {
        CorsoDAO corsoDao = new CorsoDAO(this);
        corsoDao.getChefs(corso);
    }



    public void endAll(){
        Platform.exit();
    }
}
