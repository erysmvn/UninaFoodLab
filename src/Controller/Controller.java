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
import GUI.Stages.ClassDataStages.AccountPage;
import GUI.Stages.ClassDataStages.CorsoPage;
import GUI.Stages.ClassDataStages.RicettaPage;
import GUI.Stages.ClassDataStages.SessionePage;
import GUI.Stages.CreateStages.*;
import GUI.Stages.EditStages.EditPasswordPage;
import GUI.Stages.EditStages.EditCorsoPage;
import GUI.Stages.EditStages.EditSessionePage;
import javafx.application.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Map;

public class Controller {
    private HomePage homePage;
    private LoginPage loginPage;
    private AccountPage accountPage;
    private RegisterPage registerPage;
    private CreateCorsoPage createCorsoPage;
    private ElencoCorsiPanel elencoCorsiPanel;
    private EditPasswordPage modificaPasswordPage;
    private AggiungiSessionePage aggiungiSessionePage;

    private final DBConnection dbc;

    private Utente utente;

    private final ArrayList<CorsoPage> corsoPages = new ArrayList<>();
    private final ArrayList<RicettaPage> ricettaPages = new ArrayList<>();
    private final ArrayList<SessionePage> sessionePages = new ArrayList<>();
    private final ArrayList<ConfermaPartecipazionePage> confermaPartecipazionePages = new ArrayList<>();
    private final ArrayList<AggiungiRicettaPage> aggiungiRicettaPages = new ArrayList<>();
    private final ArrayList<EditCorsoPage> editCorsoPages = new ArrayList<>();
    private final ArrayList<EditSessionePage> editSessionePages = new ArrayList<>();

    private final ChefDAO chefDAO;
    private final StudenteDAO studenteDAO;
    private final CorsoDAO corsoDAO;
    private final TipologiaCorsoDAO tipologiaCorsoDAO;
    private final SessioneDAO sessioneDAO;
    private final RicettaDAO ricettaDAO;
    private final IngredienteDAO ingredienteDAO;
    private final FoglioAdesioneDAO foglioAdesioneDAO;


    public Controller(){
        dbc = new DBConnection();
        dbc.DBConnect();
        
        chefDAO = new ChefDAO(dbc);
        studenteDAO = new StudenteDAO(dbc);
        corsoDAO = new CorsoDAO(dbc);
        tipologiaCorsoDAO = new TipologiaCorsoDAO(dbc);
        sessioneDAO = new SessioneDAO(dbc);
        ricettaDAO = new RicettaDAO(dbc);
        ingredienteDAO = new IngredienteDAO(dbc);
        foglioAdesioneDAO = new FoglioAdesioneDAO(dbc);
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
            corsoPage.setOnCloseRequest(_ -> corsoPages.remove(corsoPage));

            corsoPage.show();
        }
    }


    private CorsoPage isCorsoPageAlreadyOpened(Corso c) {
        for(CorsoPage cp : corsoPages){
            if(cp.getCorso().getIdCorso() == c.getIdCorso())
                return cp;
        }
        return null;
    }

    private RicettaPage isRicettaPageAlreadyOpened(Ricetta ricetta) {
        for (RicettaPage rp : ricettaPages) {
            if (rp.getRicetta().getIdRicetta() == ricetta.getIdRicetta())
                return rp;
        }
        return null;
    }

    private EditCorsoPage isEditCorsoPageAlreadyOpened(Corso c) {
        for(EditCorsoPage edcp : editCorsoPages){
            if(edcp.getCorso().getIdCorso() == c.getIdCorso())
                return edcp;
        }
        return null;
    }

    private EditSessionePage isEditSessionePageAlreadyOpened(Sessione s) {
        for(EditSessionePage esp : editSessionePages){
            if(esp.getSessione().getIdSessione() == s.getIdSessione())
                return esp;
        }
        return null;
    }

    private AggiungiRicettaPage isAggiungiRicettaPageAlreadyOpened(Stage caller) {
        for (AggiungiRicettaPage arp : aggiungiRicettaPages) {
            if (arp.getCaller().equals(caller))
                return arp;
        }
        return null;
    }

    private ConfermaPartecipazionePage isConfermaPartecipazionePageAlreadyOpened(SessionePresenza sessionePresenza) {
        for (ConfermaPartecipazionePage cpp : confermaPartecipazionePages) {
            if (cpp.getSessionePresenza().getIdSessione() == sessionePresenza.getIdSessione())
                return cpp;
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
            editCorsoPage.setOnCloseRequest(e -> editCorsoPages.remove(editCorsoPage));

            editCorsoPage.show();
        }
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
            editSessionePage.setOnCloseRequest(_ -> editSessionePages.remove(editSessionePage));

            editSessionePage.show();
        }
    }

    public void openConfermaPartecipazionePage(SessionePresenza sessionePresenza){

        ConfermaPartecipazionePage existingPage = isConfermaPartecipazionePageAlreadyOpened(sessionePresenza);

        if (existingPage != null) {
            if (existingPage.isShowing()) {
                existingPage.toFront();
            } else {
                existingPage.show();
            }
        } else {
            ConfermaPartecipazionePage confermaPartecipazionePage = new ConfermaPartecipazionePage(this);
            confermaPartecipazionePage.setSessionePresenza(sessionePresenza);
            confermaPartecipazionePages.add(confermaPartecipazionePage);
            confermaPartecipazionePage.setOnCloseRequest(e -> confermaPartecipazionePages.remove(confermaPartecipazionePage));
            confermaPartecipazionePage.show();
        }
    }

    public void openSessionePage(Sessione sessione){

        SessionePage sessionePage = new SessionePage(this);
        sessionePage.initPage(sessione);
        sessionePages.add(sessionePage);
        sessionePage.show();
    }

    public void openRicettaPage(Ricetta ricetta){
        RicettaPage existingPage = isRicettaPageAlreadyOpened(ricetta);

        if (existingPage != null) {
            if (existingPage.isShowing()) {
                existingPage.toFront();
            } else {
                existingPage.show();
            }
        } else {
            RicettaPage ricettaPage = new RicettaPage(this);
            ricettaPage.initPage(ricetta);
            ricettaPages.add(ricettaPage);

            ricettaPage.setOnCloseRequest(e -> ricettaPages.remove(ricettaPage));

            ricettaPage.show();
        }
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
            modificaPasswordPage = new EditPasswordPage(this);
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

        for(AggiungiRicettaPage aggiungiRicettaPage: aggiungiRicettaPages){
            aggiungiRicettaPage.close();
        }
        aggiungiRicettaPages.clear();

        aggiungiSessionePage.show();
    }

    public void openAggiungiRicettaPage(Stage caller){

        AggiungiRicettaPage existingPage = isAggiungiRicettaPageAlreadyOpened(caller);

        if (existingPage != null) {
            if (existingPage.isShowing()) {
                existingPage.toFront();
            } else {
                existingPage.show();
            }
        } else {
            AggiungiRicettaPage aggiungiRicettaPage = new AggiungiRicettaPage(this, caller);
            aggiungiRicettaPages.add(aggiungiRicettaPage);
            aggiungiRicettaPage.setOnCloseRequest(e -> aggiungiRicettaPages.remove(aggiungiRicettaPage));
            aggiungiRicettaPage.show();
        }
    }

    public void updateRicetteAggiunte(Ricetta ricetta, Stage caller) {
        if(caller instanceof AggiungiSessionePage)
            aggiungiSessionePage.updateRicetteAggiunte(ricetta);
        else if(caller instanceof EditSessionePage)
            ((EditSessionePage) caller).updateRicetteAggiunte(ricetta);
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

    public void removeRicetteToSessione(ArrayList<Ricetta> ricette, Sessione sessione) throws SQLException {
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

    public void refreshCalendario() {
        accountPage.getCalendarioPanel().initCalendario(utente);
    }

    public void refreshAccountPage() {
        if(accountPage != null)
            accountPage.close();
        accountPage = new AccountPage(this);
        accountPage.initPage(utente);
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



    // User
    public void loginMethod(String email, String password) throws emailNotFoundException, passwordErrataException, SQLException{

        if (email.contains("@studenti.unina.it")) {
            utente = studenteDAO.login(email, password);
        } else {
            utente = chefDAO.login(email, password);
        }
        homePage.setUtente(utente);
        for(CorsoPage corsoPage:corsoPages){
            corsoPage.close();
        }
        this.corsoPages.clear();

    }

    public void registerMethod(Utente utente) throws SQLException {

        if (utente instanceof Chef chef) {
            Chef ch = chefDAO.register(chef);

            if(ch != null){
                this.utente = ch;
            }
        } else if (utente instanceof Studente studente) {
            Studente st = studenteDAO.register(studente);

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

    public void checkOldPassword(String oldPassword) throws SQLException, oldPasswordErrorException {
        if (utente instanceof Studente studente) {
            studenteDAO.checkOldPassword(oldPassword, studente);
        } else if (utente instanceof Chef chef) {
            chefDAO.checkOldPassword(oldPassword, chef);
        }
    }

    public void changeUserPassword(String newPassword) throws changePasswordException,SQLException {
        if (utente instanceof Studente studente) {
            studenteDAO.changeUserPassword(newPassword, studente);
        } else if (utente instanceof Chef chef) {
            chefDAO.changeUserPassword(newPassword, chef);
        }
    }

    public void subscribeToCourse(Corso corso) throws SQLException {
        if (utente instanceof Studente studente) {
            studenteDAO.subscribeToCourse(studente, corso);
            studente.addCorso(corso);
        }
    }

    public void unsubscribeToCourse(Corso corso) throws SQLException {
        if (utente instanceof Studente studente) {
            studenteDAO.unsubscribeToCourse(studente, corso);
            studente.removeCorso(corso);
            this.removeCorsoPage(corso);
        }
    }

    public Boolean alreadySubscribed(Corso corso) throws SQLException {
        if (utente instanceof Studente studente) {
            return studenteDAO.checkIfSubscribed(studente, corso);
        } else {
            return false;
        }
    }

    public Map<String,Object> getReportMensile(YearMonth mese)throws SQLException{
        return  chefDAO.getReportMensile((Chef)utente, mese);
    }

    // Corso
    public ArrayList<Corso> searchCorsiByTipologia(String tipologia)throws corsiNotFoundException,SQLException{
        return corsoDAO.searchCorsiLikeNomeTipologia(tipologia);
    }

    public ArrayList<Corso> searchCorsiByChef(String nomeChef)throws corsiNotFoundException,SQLException {
        return corsoDAO.searchCorsiLikeNomeChef(nomeChef);
    }

    public ArrayList<Corso> getMostFollowedCourses(int limit) throws SQLException, corsiNotFoundException {
        return corsoDAO.getCorsiConPiuStudenti(limit);
    }

    public ArrayList<Corso> searchCorsiLikeString(String nomeCorso) throws SQLException, corsiNotFoundException {
        return corsoDAO.searchCorsiLikeNomeCorso(nomeCorso);
    }

    public ArrayList<Corso> getAllCourses() throws SQLException, corsiNotFoundException {
        return corsoDAO.getAllCourses();
    }

    public Corso getCorsoByNome(String nome) throws SQLException, corsiNotFoundException {
        return corsoDAO.getCorsoByNome(nome);
    }

    public Corso getCorsoByIdCorso(int id) throws SQLException, corsiNotFoundException {
        return corsoDAO.getCorsoByIdCorso(id);
    }

    public Corso createNewCorso(String nomeCorso, double prezzo, int frequenza, String difficolta, TipologiaCorso tipologia, ArrayList<Chef> chefs) throws SQLException {
        Corso newCorso = corsoDAO.createNewCorso(nomeCorso, prezzo, frequenza, difficolta);
        if (newCorso != null) {
            addChefsToCorso(newCorso.getIdCorso(), chefs);
            addToCaratterizzato(newCorso.getIdCorso(), tipologia.getId());
        } else {
            throw new createCorsoErrorException();
        }
        return newCorso;
    }

    public ArrayList<Chef> getChefsByIdCorso(int idcorso) throws SQLException {
        Corso corso = corsoDAO.getCorsoByIdCorso(idcorso);
        return corso.getChefs();
    }

    public Chef getChefDaAggiungereToNuovoCorso(String nome, String cognome, String email) throws SQLException {
        return chefDAO.getChefDaAggiungereToNuovoCorso(nome, cognome, email);
    }

    public void getRicetteTrattate(Corso corso) throws SQLException {
        corsoDAO.getRicetteTrattate(corso);
    }

    public void setChefs(Corso corso) throws SQLException {
        corsoDAO.setChefs(corso);
    }

    public void deleteCorso(Corso corso) throws SQLException {
        corsoDAO.delete(corso);
    }

    public void updateCorso(Corso corso) throws SQLException {
        corsoDAO.update(corso);
        Chef myChef = (Chef) utente;
        corsoDAO.deleteOtherChefs(corso.getIdCorso(), myChef.getIdchef());
        addChefsToCorso(corso.getIdCorso(), corso.getChefs());
    }

    public void addChefsToCorso(int idCorso, ArrayList<Chef> chefs) throws SQLException {
        for (Chef chef : chefs) {
            corsoDAO.addChefToCorso(idCorso, chef);
        }
    }

    public void addToCaratterizzato(int idcorso, int idtipologia) throws SQLException {
        corsoDAO.addToCaratterizzato(idcorso, idtipologia);
    }


    // Sessione
    public void insertSessione(Sessione sessione) throws SQLException {
        sessioneDAO.insertSessione(sessione);
    }

    public void insertRicetteToSessione(ArrayList<Ricetta> ricette,Sessione sessione) throws SQLException{
        for(Ricetta ricetta : ricette){
            inserisciIngredientiToRicetta(ricetta);
            sessioneDAO.insertRicettaToSessione(ricetta,sessione);
        }
    }

    public FoglioAdesione getFoglioAdesioneBySessioneNPath(String filePath, SessionePresenza sessionePresenza) throws SQLException {
        return foglioAdesioneDAO.getFoglioAdesioneBySessioneNPath(filePath,sessionePresenza, (Studente) utente);
    }

    public void insertFoglioAdesione(String filePath, SessionePresenza sessionePresenza) throws SQLException{
        foglioAdesioneDAO.insertFoglioDiAdesione(filePath, sessionePresenza, (Studente) utente);
    }

    public void updateSessione(Sessione sessione) throws SQLException {
        sessioneDAO.update(sessione);
    }

    public void deleteSessione(Sessione sessione) throws SQLException {
        sessioneDAO.delete(sessione);
    }


    // Ricetta
    public ArrayList<Ingrediente> getAllIngredienti() throws SQLException {
        return ingredienteDAO.getAllIngredientes();
    }

    public void getIngredientiRicetta(Ricetta Ricetta) throws SQLException {
        ricettaDAO.getIngredienti(Ricetta);
    }

    public String getQuantitaIngrediente(Ricetta Ricetta, Ingrediente Ingrediente) throws SQLException {
        return ricettaDAO.getQuantitaIngrediente(Ricetta, Ingrediente);
    }

    public void getAllergeniRicetta(Ricetta Ricetta) throws SQLException {
        ricettaDAO.getAllergeniRicetta(Ricetta);
    }

    public void insertIngredienti(Ingrediente ingrediente)throws SQLException{
        ingredienteDAO.insertIngrediente(ingrediente);
    }

    public void inserisciIngredientiToRicetta(Ricetta ricetta)throws SQLException {
        ricettaDAO.insertRicetta(ricetta);
        ricettaDAO.inserisciIngredientiToRicetta(ricetta);
    }


    // TipologiaCorso
    public ArrayList<TipologiaCorso> getAllTipologie() throws SQLException {
        return tipologiaCorsoDAO.getAll();
    }

    public TipologiaCorso getOrAddTipologiaCorso(String nomeTipo) throws SQLException {
        return tipologiaCorsoDAO.addNewTipologiaCorso(nomeTipo);
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
