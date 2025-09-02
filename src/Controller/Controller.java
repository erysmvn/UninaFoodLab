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
import javafx.scene.chart.Axis;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {
    private HomePage homePage;
    private LoginPage loginPage;
    private AccountPage accountPage;
    private RegisterPage registerPage;
    private ChangePasswordPage modificaPasswordPage;
    private CreateCorsoPage createCorsoPage;
    private AggiungiSessionePage aggiungiSessionePage;

    private ElencoCorsiPanel elencoCorsiPanel;

    private DBConnection dbc;

    private Utente utente;

    private ArrayList<CorsoPage> corsoPages = new ArrayList<>();
    private ArrayList<RicettaPage> ricettaPages = new ArrayList<>();
    private ArrayList<SessionePage> sessionePages = new ArrayList<>();
    private ArrayList<ConfermaPartecipazionePage> confermaPartecipazionePages = new ArrayList<>();
    private ArrayList<AggiungiRicettaPage> aggiungiRicettaPages = new ArrayList<>();
    private ArrayList<EditCorsoPage> editCorsoPages = new ArrayList<>();
    private ArrayList<EditSessionePage> editSessionePages = new ArrayList<>();


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
    public boolean isChef(){
        return utente instanceof Chef;
    }
    public boolean isStudent(){
        return utente instanceof Studente;
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

    public RicettaDAO getRicettaDAO(){
        return new RicettaDAO(this);
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
            if(cp.getCorso().getIdCorso() == c.getIdCorso())
                return cp;
        }
        return null;
    }

    public void openEditCorsoPage(Corso corso){
        EditCorsoPage existingPage = isEditCorsoPageAlreadyOpened(corso);

        if(existingPage != null){
            if(existingPage.isShowing()){
                existingPage.toFront();
            } else {
                existingPage.show();
            }
        } else {
            EditCorsoPage editCorsoPage = new EditCorsoPage( this);
            editCorsoPage.initPage(corso);
            editCorsoPages.add(editCorsoPage);
            editCorsoPage.setOnCloseRequest(e -> corsoPages.remove(editCorsoPage));

            editCorsoPage.show();
        }
    }

    private EditCorsoPage isEditCorsoPageAlreadyOpened(Corso c){
        for(EditCorsoPage edcp : editCorsoPages){
            if(edcp.getCorso().getIdCorso() == c.getIdCorso())
                return edcp;
        }
        return null;
    }

    public void openEditSessionePage(Sessione s, SessionePage sp){
        EditSessionePage existingPage = isEditSessionePageAlreadyOpened(s);

        if(existingPage != null){
            if(existingPage.isShowing()){
                existingPage.toFront();
            } else {
                existingPage.show();
            }
        } else {
            EditSessionePage editSessionePage = new EditSessionePage( this, sp);
            editSessionePage.initPage(s);
            editSessionePages.add(editSessionePage);
            editSessionePage.setOnCloseRequest(e -> editSessionePages.remove(editSessionePage));

            editSessionePage.show();
        }
    }

    private EditSessionePage isEditSessionePageAlreadyOpened(Sessione s) {
        for(EditSessionePage esp : editSessionePages){
            if(esp.getSessione().getIdSessione() == s.getIdSessione())
                return esp;
        }
        return null;
    }



    public void openConfermaPartecipazionePage(SessionePresenza sessionePresenza){
        for(ConfermaPartecipazionePage confPage: confermaPartecipazionePages){
            if(confPage.getSessionePresenza().equals(sessionePresenza)){
                confPage.toFront();
                return;
            }
        }

        ConfermaPartecipazionePage confermaPartecipazionePage = new ConfermaPartecipazionePage(this);
        confermaPartecipazionePage.setSessionePresenza(sessionePresenza);
        confermaPartecipazionePages.add(confermaPartecipazionePage);
        confermaPartecipazionePage.setOnCloseRequest(e->confermaPartecipazionePages.remove(confermaPartecipazionePage));
        confermaPartecipazionePage.show();
    }
    public void openSessionePage(Sessione sessione){
/*
        for(SessionePage sessionePage : sessionePages){
            if(sessionePage.getSessione().equals(sessione)){
                sessionePage.toFront();
                return;
            }
        }
*/
        SessionePage sessionePage = new SessionePage(this);
        sessionePage.initPage(sessione);
        sessionePages.add(sessionePage);
        sessionePage.setOnCloseRequest(e -> sessionePages.remove(sessionePage));
        sessionePage.show();
    }
    public void openRicettaPage(Ricetta ricetta){
        for(RicettaPage ricettaPage: ricettaPages){
            if(ricettaPage.getRicetta().equals(ricetta)){
                ricettaPage.toFront();
                return;
            }
        }

            RicettaPage ricettaPage = new RicettaPage( this);
            ricettaPage.initPage(ricetta);
            ricettaPages.add(ricettaPage);

            ricettaPage.setOnCloseRequest(e -> ricettaPages.remove(ricettaPage));
            ricettaPage.show();

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
    public void openRegisterPage(){
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
    public void openCreateCorsoPage() {
        if(createCorsoPage == null || !createCorsoPage.isShowing()) {
            if (getUtente() instanceof Chef chef) {
                createCorsoPage = new CreateCorsoPage(this, chef);
                createCorsoPage.show();
            }
        } else {
            createCorsoPage.toFront();
        }
    }
    public void openAggiungiSessionePage(Corso corso){
        if(aggiungiSessionePage != null)
            aggiungiSessionePage.close();

        aggiungiSessionePage = new AggiungiSessionePage(this);
        aggiungiSessionePage.initPage(corso);
        aggiungiSessionePage.show();
    }
    public void openAggiungiRicettaPage(Stage caller){
        //todo o si apre una sola aggiungiRicettaPage alla volta oppure si deve controllare se già aperta
        // (funzione chiamta da editSessionePage oppure aggiungiSessionePage
        AggiungiRicettaPage aggiungiRicettaPage = new AggiungiRicettaPage(this,caller);
        aggiungiRicettaPages.add(aggiungiRicettaPage);
        aggiungiRicettaPage.show();
    }
    // Exit
    public void closeApp(){
        Platform.exit();
    }
    private void closeAllPages(ArrayList<? extends Stage> pages){
        for(Stage page : pages){
            if(page != null)
                page.close();
        }
        pages.clear();
    }
    private void closeAllPages(){

        closeAllPages(corsoPages);
        closeAllPages(ricettaPages);
        closeAllPages(sessionePages);
        closeAllPages(confermaPartecipazionePages);
        closeAllPages(aggiungiRicettaPages);
        closeAllPages(editCorsoPages);
        closeAllPages(editSessionePages);

        ArrayList<Stage> singlePages = new ArrayList<>();
        singlePages.add(accountPage);
        singlePages.add(registerPage);
        singlePages.add(modificaPasswordPage);
        singlePages.add(createCorsoPage);
        singlePages.add(aggiungiSessionePage);

        closeAllPages(singlePages);
    }


    public void changeSessionePageButton(SessionePresenza sessionePresenza){
        if(sessionePresenza != null) {
            for (SessionePage sessionePage : sessionePages) {
                if (sessionePage.getSessione().equals(sessionePresenza)) {
                    sessionePage.changeUploadButton();
                }
            }
        }
    }
    public void refreshAccountPage() {
        if(accountPage != null)
            accountPage.close();

        accountPage = new AccountPage(this);
        accountPage.initPage(utente);
        
    }
    public void removeRicetteToSessione(ArrayList<Ricetta> ricette, Sessione sessione)throws SQLException {
        SessioneDAO sessioneDAO = new SessioneDAO(this);
       for(Ricetta ricetta:  ricette)
        sessioneDAO.removeRicetta(ricetta,sessione);
    }

    public void refreshCorsi(ElencoCorsiPanel elencoCorsiPanel) {
        elencoCorsiPanel.showCorsi();
        this.elencoCorsiPanel = elencoCorsiPanel;
    }
    public void refreshCorsi() {
        if (elencoCorsiPanel != null) {
            elencoCorsiPanel.showCorsi();
        }
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
        closeAllPages();
        homePage.setLogOut();
    }

    public void checkOldPassword(String oldPassword) throws oldPasswordErrorException {

        if (utente instanceof Studente studente) {
            StudenteDAO studenteDao = getStudenteDAO();
            studenteDao.checkOldPassword(oldPassword, studente);
        } else if (utente instanceof Chef chef) {
            ChefDAO chefDao = getChefDAO();
            chefDao.checkOldPassword(oldPassword, chef);
        }

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

    // Corso
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
    /*todo rimuvoere?*/public ArrayList<Corso> getCorsiByModalita(String modalita) {
        CorsoDAO corsoDAO = new CorsoDAO(this);
        return corsoDAO.getCorsiByModalita(modalita);
    }
    public ArrayList<Corso> getMostFollowedCourses(int limit) {
        CorsoDAO corsoDao = getCorsoDAO();
        return corsoDao.getCorsiConPiuStudenti(limit);
    }
    public ArrayList<Corso> searchCorsiLikeString(String nomeCorso) throws corsiNotFoundException,SQLException{
        CorsoDAO corsoDao = getCorsoDAO();
        return corsoDao.searchCorsiLikeString(nomeCorso);
    }
    public ArrayList<Corso> getAllCourses(){
        CorsoDAO corsoDao = getCorsoDAO();
        return corsoDao.getAllCourses();
    }

    public Corso getCorsoByNome(String nome){
        CorsoDAO corsoDao = getCorsoDAO();
        return corsoDao.getCorsoByNome(nome);
    }
    public Corso createNewCorso(String nomeCorso, double prezzo, int frequenza, String difficolta, TipologiaCorso tipologia, ArrayList<Chef> chefs) {
        CorsoDAO corsoDao = getCorsoDAO();
        Corso newCorso = corsoDao.createNewCorso(nomeCorso, prezzo, frequenza, difficolta);
        if (newCorso != null) {
            addChefsToCorso(newCorso.getIdCorso(), chefs);
            addToCaratterizzato(newCorso.getIdCorso(), tipologia.getId());
        } else {
            throw new createCorsoErrorException();
        }
        return newCorso;
    }

    public ArrayList<Chef> getChefsByIdCorso(int idcorso){
        CorsoDAO corsoDAO = getCorsoDAO();
        Corso corso = corsoDAO.getCorsoByIdCorso(idcorso);
        return corso.getChefs();
    }
    public Chef getChefDaAggiungereToNuovoCorso(String nome, String cognome, String email) {
        ChefDAO chefDao = getChefDAO();
        return chefDao.getChefDaAggiungereToNuovoCorso(nome, cognome, email);
    }

    public void getRicetteTrattate(Corso corso) {
        CorsoDAO corsoDao = new CorsoDAO(this);
        corsoDao.getRicetteTrattate(corso);
    }
    public void setChefs(Corso corso) {
        CorsoDAO corsoDao = new CorsoDAO(this);
        corsoDao.setChefs(corso);
    }
    public void deleteCorso(Corso corso) {
        CorsoDAO corsoDao = getCorsoDAO();
        corsoDao.delete(corso);
    }
    public void updateCorso(Corso corso) {
        CorsoDAO corsoDao = getCorsoDAO();
        corsoDao.update(corso);
        Chef myChef = (Chef) utente;
        corsoDao.prepareChefs(corso.getIdCorso(), myChef.getIdchef());
        addChefsToCorso(corso.getIdCorso(), corso.getChefs());
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

    // Sessione
    public void insertSessione(Sessione sessione) throws SQLException {
        SessioneDAO sessioneDAO = new SessioneDAO(this);
        sessioneDAO.insertSessione(sessione);
    }

    public void insertRicetteToSessione(ArrayList<Ricetta> ricette,Sessione sessione) throws SQLException{
        SessioneDAO sessioneDAO = new SessioneDAO(this);
        for(Ricetta ricetta : ricette){
            inserisciIngredientiToRicetta(ricetta);
            sessioneDAO.insertRicettaToSessione(ricetta,sessione);
        }
    }

    public FoglioAdesione getFoglioAdesioneBySessioneNPath(String filePath, SessionePresenza sessionePresenza){
        FoglioAdesioneDAO foglioDAO = new FoglioAdesioneDAO(this);
        return foglioDAO.getFoglioAdesioneBySessioneNPath(filePath,sessionePresenza);
    }

    public void insertFoglioAdesione(String filePath, SessionePresenza sessionePresenza) throws SQLException{
        FoglioAdesioneDAO foglioAdesioneDAO = new FoglioAdesioneDAO(this);
        foglioAdesioneDAO.insertFoglioDiAdesione(filePath, sessionePresenza);
    }

    public void updateSessione(Sessione sessione) throws SQLException {
        SessioneDAO sessioneDAO = new SessioneDAO(this);
        sessioneDAO.update(sessione);
    }

    public void refreshCalendario() {
        accountPage.getCalendarioPanel().initCalendario(utente);
    }

    public void deleteSessione(Sessione sessione) throws SQLException {
        SessioneDAO sessioneDAO = new SessioneDAO(this);
        sessioneDAO.delete(sessione);
    }


    // Ricetta
    public ArrayList<Ingrediente> getAllIngredienti()throws SQLException{
        IngredienteDAO ingredienteDAO = new IngredienteDAO(this);
        return ingredienteDAO.getAllIngredientes();
    }
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
    public void updateRicetteAggiunte(Ricetta ricetta,Stage caller) {

        if(caller instanceof AggiungiSessionePage)
            aggiungiSessionePage.updateRicetteAggiunte(ricetta);
        else if(caller instanceof EditSessionePage)
            ((EditSessionePage) caller).updateRicetteAggiunte(ricetta);
    }
    public void insertIngredienti(Ingrediente ingrediente)throws SQLException{
        IngredienteDAO ingredienteDao = new IngredienteDAO(this);
        ingredienteDao.insertIngrediente(ingrediente);
    }
    public void inserisciIngredientiToRicetta(Ricetta ricetta)throws SQLException {
        RicettaDAO ricettaDAO = new RicettaDAO(this);
        ricettaDAO.insertRicetta(ricetta);
        ricettaDAO.inserisciIngredientiToRicetta(ricetta);
    }

    // TipologiaCorso
    public ArrayList<TipologiaCorso> getAllTipologie() {
        TipologiaCorsoDAO tipologiaDao = new TipologiaCorsoDAO(this);
        return tipologiaDao.getAll();
    }
    public TipologiaCorso getOrAddTipologiaCorso(String nomeTipo) {
        TipologiaCorsoDAO tipologiaDao = new TipologiaCorsoDAO(this);
        return tipologiaDao.addNewTipologiaCorso(nomeTipo);
    }

    // Mail
    public void openEmail(String to, String subject, String body) throws emailClientNotFoundException, URISyntaxException, IOException {

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

    }
    private String encode(String text) {
        return text.replace(" ", "%20")
                .replace("\n", "%0A");
    }

}
