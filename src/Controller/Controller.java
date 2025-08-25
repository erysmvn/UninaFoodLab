package Controller;

import Entity.*;
import DB.DBConnection;
import GUI.Pane.AccountCorsiPanel;
import GUI.Stages.*;
import DAO.*;
import javafx.application.*;

import java.awt.*;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {
    private HomePage homePage;
    private LoginPage loginPage;
    private AccountPage accountPage;
    private RegisterPage registerPage;
    private ChangePasswordPage modificaPasswordPage;

    private DBConnection dbc;

    private Utente utente;

    private ArrayList<CorsoPage> corsoPages = new ArrayList<>();
    private ArrayList<RicettaPage> ricettaPages = new ArrayList<>();

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
        return homePage.getIsChef();
    }

    public boolean isAlreadyLoggedIn() {
        return homePage.getIsLoggedIn();
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

    public void openRicettaPage(Ricetta ricetta){
        RicettaPage existingPage = isRicettaPageAlreadyOpened(ricetta);

        if(existingPage != null){
            if(existingPage.isShowing()){
                existingPage.toFront();
            } else {
                existingPage.show();
            }
        } else {
            RicettaPage ricettaPage = new RicettaPage( this);
            ricettaPage.initPage(ricetta);
            ricettaPages.add(ricettaPage);

            ricettaPage.setOnCloseRequest(e -> ricettaPages.remove(ricettaPage));

            ricettaPage.show();
        }
    }

    private RicettaPage isRicettaPageAlreadyOpened(Ricetta r){
        for(RicettaPage rp : ricettaPages){
            if(rp.getRicetta().getIdRicetta() == r.getIdRicetta()){
                return rp;
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

    public void openModificaPassword() {
        if(modificaPasswordPage == null || !modificaPasswordPage.isShowing()) {
            modificaPasswordPage = new ChangePasswordPage(this);
            modificaPasswordPage.show();
        } else {
            modificaPasswordPage.toFront();
        }
    }

    public void refreshCorsi(AccountCorsiPanel accountCorsiPanel) {
        accountCorsiPanel.showCorsi();
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


    public Boolean checkOldPassword(String oldPassword){
        Boolean result = false;
        if (utente instanceof Studente studente) {
            StudenteDAO studenteDao = getStudenteDAO();
            result = studenteDao.checkOldPassword(oldPassword, studente);
        } else if (utente instanceof Chef chef) {
            ChefDAO chefDao = getChefDAO();
            result = chefDao.checkOldPassword(oldPassword, chef);
        }
        return result;
    }

    public void changeUserPassword(String newPassword) {
        if (utente instanceof Studente studente) {
            StudenteDAO studenteDao = getStudenteDAO();
            studenteDao.changeUserPassword(newPassword, studente);
        } else if (utente instanceof Chef chef) {
            ChefDAO chefDao = getChefDAO();
            chefDao.changeUserPassword(newPassword, chef);
        }
    }

    public ArrayList<Corso> searchCorsiByTipologia(String tipologia){
        CorsoDAO corsoDAO = getCorsoDAO();
        ArrayList<Corso> corsi = corsoDAO.searchCorsiByTipologia(tipologia);
        return corsi;
    }

    public ArrayList<Corso> searchCorsiByChef(String nomeChef){
        CorsoDAO corsoDao = getCorsoDAO();
        ArrayList<Corso> corsi = corsoDao.searchCorsiByChef(nomeChef);

        return corsi;

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

    public ArrayList<Corso> getAllCourses(){
        CorsoDAO corsoDao = getCorsoDAO();
        return corsoDao.getAllCourses();
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

    public ArrayList<Corso> getMostFollowedCourses(int limit) {
        CorsoDAO corsoDao = getCorsoDAO();
        return corsoDao.getCorsiConPiuStudenti(limit);
    }

    public ArrayList<Corso> searchCorsiLikeString(String nomeCorso) {
        CorsoDAO corsoDao = getCorsoDAO();
        return corsoDao.searchCorsiLikeString(nomeCorso);
    }

    public void deleteCorso(Corso corso) {
        CorsoDAO corsoDao = getCorsoDAO();
//        corsoDao.delete(corso);
    }



    // Ricetta
    public void getIngredientiRicetta(Ricetta Ricetta) {
        RicettaDAO ricettaDao = new RicettaDAO(this);
        ricettaDao.getIngredienti(Ricetta);
    }

    public String getQuantitaIngrediente(Ricetta Ricetta, Ingrediente Ingrediente) {
        RicettaDAO ricettaDao = new RicettaDAO(this);
        String toReturn = ricettaDao.getQuantitaIngrediente(Ricetta, Ingrediente);
        return toReturn;
    }

    public void getAllergeniRicetta(Ricetta Ricetta) {
        RicettaDAO ricettaDao = new RicettaDAO(this);
        ricettaDao.getAllergeniRicetta(Ricetta);
    }



    // Mail
    public void openEmail(String to, String subject, String body) {
        try {
            String uriStr = String.format(
                    "mailto:%s?subject=%s&body=%s",
                    to,
                    encode(subject),
                    encode(body)
            );
            URI mailto = new URI(uriStr);

            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("linux")) {
                new ProcessBuilder("xdg-open", mailto.toString()).start();
                return;
            }

            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.mail(mailto);
                    return;
                } catch (Exception ignored) {
                    try {
                        desktop.browse(mailto);
                        return;
                    } catch (Exception ignored2) {}
                }
            }

            if (os.contains("mac")) {
                new ProcessBuilder("open", mailto.toString()).start();
            } else if (os.contains("win")) {
                new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", mailto.toString()).start();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String encode(String text) {
        return text.replace(" ", "%20")
                .replace("\n", "%0A");
    }



    public void endAll(){
        Platform.exit();
    }
}
