package Controller;

import Entity.*;
import DB.DBConnection;
import Exception.CorsoExceptions.CreateCorsoException.createCorsoErrorException;
import Exception.CorsoExceptions.corsiNotFoundException;
import Exception.UserExceptions.ChangePasswordException.changePasswordException;
import Exception.UserExceptions.ChangePasswordException.oldPasswordErrorException;
import Exception.UserExceptions.LoginException.emailNotFoundException;
import Exception.UserExceptions.LoginException.passwordErrataException;
import Exception.UserExceptions.SupportException.emailClientNotFoundException;
import GUI.Pane.ElencoCorsiPanel;
import GUI.Stages.*;
import DAO.*;
import javafx.application.*;

import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {
    private HomePage homePage;
    private LoginPage loginPage;
    private AccountPage accountPage;
    private RegisterPage registerPage;
    private ChangePasswordPage modificaPasswordPage;
    private CreateCorsoPage createCorsoPage;

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

    public void openCreateCorsoPage(Utente utente) {
        if(createCorsoPage == null || !createCorsoPage.isShowing()) {
            if (getUtente() instanceof Chef chef) {
                createCorsoPage = new CreateCorsoPage(this, chef);
                createCorsoPage.show();
            }
        } else {
            createCorsoPage.toFront();
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

    public void refreshCorsi(ElencoCorsiPanel elencoCorsiPanel) {
        elencoCorsiPanel.showCorsi();
    }

    private void removeCorsoPage(Corso corso){
        CorsoPage toRemove = null;
        for(CorsoPage cp : corsoPages){
            if(cp.getCorso().equals(corso))
                toRemove = cp;
        }
        if (toRemove!=null){
            toRemove.close();
            corsoPages.remove(toRemove);
        }
    }

    public void homepageToFront(){
        homePage.toFront();
        homePage.mostraTuttiCorsi();
    }



    // User
    public void loginMethod(String email, String password) throws emailNotFoundException, passwordErrataException, SQLException{

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

    public boolean checkOldPassword(String oldPassword) throws oldPasswordErrorException {
        Boolean result = false;
        if (utente instanceof Studente studente) {
            StudenteDAO studenteDao = getStudenteDAO();
            result = studenteDao.checkOldPassword(oldPassword, studente);
        } else if (utente instanceof Chef chef) {
            ChefDAO chefDao = getChefDAO();
            result = chefDao.checkOldPassword(oldPassword, chef);
        }
        if(!result){
            throw new oldPasswordErrorException();
        }
        return result;
    }

    public void changeUserPassword(String newPassword)throws changePasswordException,SQLException {
        if (utente instanceof Studente studente) {
            StudenteDAO studenteDao = getStudenteDAO();
            studenteDao.changeUserPassword(newPassword, studente);
        } else if (utente instanceof Chef chef) {
            ChefDAO chefDao = getChefDAO();
            chefDao.changeUserPassword(newPassword, chef);
        }
    }

    public ArrayList<Corso> searchCorsiByTipologia(String tipologia)throws corsiNotFoundException,SQLException{
        CorsoDAO corsoDAO = getCorsoDAO();
        ArrayList<Corso> corsi = corsoDAO.searchCorsiByTipologia(tipologia);
        return corsi;
    }

    public ArrayList<Corso> searchCorsiByChef(String nomeChef)throws corsiNotFoundException,SQLException {
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
            this.removeCorsoPage(corso);
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

    public ArrayList<Chef> getAllChefs() {
        ChefDAO chefDao = getChefDAO();
        return chefDao.getAll();
    }

    public Chef getChefDaAggiungereToNuovoCorso(String nome, String cognome, String email) {
        ChefDAO chefDao = getChefDAO();
        return chefDao.getChefDaAggiungereToNuovoCorso(nome, cognome, email);
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

    public ArrayList<Corso> searchCorsiLikeString(String nomeCorso) throws corsiNotFoundException,SQLException{
        CorsoDAO corsoDao = getCorsoDAO();
        return corsoDao.searchCorsiLikeString(nomeCorso);
    }

    public void deleteCorso(Corso corso) {
        CorsoDAO corsoDao = getCorsoDAO();
//        corsoDao.delete(corso);
    }

    public ArrayList<Corso> getAllCourses(){
        CorsoDAO corsoDao = getCorsoDAO();
        return corsoDao.getAllCourses();
    }

    public void createNewCorso(String nomeCorso, double prezzo, int frequenza, String difficolta, TipologiaCorso tipologia, ArrayList<Chef> chefs) {
        CorsoDAO corsoDao = getCorsoDAO();
        Corso newCorso = corsoDao.createNewCorso(nomeCorso, prezzo, frequenza, difficolta);
        if (newCorso != null) {
            addChefsToCorso(newCorso.getIdCorso(), chefs);
            System.out.println(newCorso.getIdCorso());
            addToCaratterizzato(newCorso.getIdCorso(), tipologia.getId());
        } else {
            throw new createCorsoErrorException();
        }
    }

    public void addChefsToCorso(int idCorso, ArrayList<Chef> chefs) {
        CorsoDAO corsoDao = getCorsoDAO();
        for (Chef chef : chefs) {
            System.out.println(chef.getIdchef());
            corsoDao.addChefToCorso(idCorso, chef);
        }
    }

    public void addToCaratterizzato(int idcorso, int idtipologia) {
        CorsoDAO corsoDao = getCorsoDAO();
        corsoDao.addToCaratterizzato(idcorso, idtipologia);
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



    // TipologiaCorso
    public ArrayList<TipologiaCorso> getAllTipologie() {
        TipologiaCorsoDAO tipologiaDao = new TipologiaCorsoDAO(this);
        return tipologiaDao.getAll();
    }

    public TipologiaCorso addNewTipologiaCorso(String nomeTipo) {
        TipologiaCorsoDAO tipologiaDao = new TipologiaCorsoDAO(this);
        return tipologiaDao.addNewTipologiaCorso(nomeTipo);
    }

    public TipologiaCorso getTipologiaByName(String nomeTipo) {
        TipologiaCorsoDAO tipologiaDao = new TipologiaCorsoDAO(this);
        return tipologiaDao.getTipologiaByName(nomeTipo);
    }


    // Mail
    public void openEmail(String to, String subject, String body) throws emailClientNotFoundException {
        try {
            String uriStr = String.format(
                    "mailto:%s?subject=%s&body=%s",
                    to,
                    encode(subject),
                    encode(body)
            );

            URI mailto = new URI(uriStr);
            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("linux")){
                new ProcessBuilder("xdg-open", mailto.toString()).start();
            }else if (os.contains("mac")) {
                new ProcessBuilder("open", mailto.toString()).start();
            }else if (os.contains("win")) {
                new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", mailto.toString()).start();
            }else{
               throw new emailClientNotFoundException("Email client non trovato. Scrivere a supportfoodlab@uninasupport.it");
            }
        } catch (emailClientNotFoundException ECN){
            throw ECN;
        }catch (Exception exc){
            throw new emailClientNotFoundException("Errore del sistema. Riprovare più tardi");
        }
    }

    private String encode(String text) {
        return text.replace(" ", "%20")
                .replace("\n", "%0A");
    }


    // Exit
    public void endAll(){
        Platform.exit();
    }
}
