package GUI.Stages.CreateStages;

import Controller.Controller;
import Entity.*;
import Exception.SessioneExceptions.*;
import GUI.Buttons.CircleButton;
import GUI.Buttons.MyButton;
import GUI.Stages.MyStage;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class AggiungiSessionePage extends MyStage {
    private Controller controller;
    private VBox root;

    private Label numeroRicette;

    private Corso corso;
    private Sessione sessione;
    private ArrayList<Ricetta> ricette;

    private DatePicker dateInizio;
    private TextField linkOrLuogoField;

    private Spinner<Integer> hourSpinnerInizio;
    private Spinner<Integer> minuteSpinnerInizio;
    private Spinner<Integer> hourSpinnerFine;
    private Spinner<Integer> minuteSpinnerFine;

    private Label erroreOrario;
    private Label erroreDataSessione;
    private Label errorLinkOrLuogoLabel;
    private Label errorInserimentoDatiLabel;
    private Label errorAlmenoUnaRicettaLabel;



    public AggiungiSessionePage(Controller controller) {
        super(450, 700, RootType.VBOX);
        this.controller = controller;
        root = getRootVBox();

        addStylesheet("/Media/StyleSheets/fieldsAndBoxesStyle.css");
    }

    public void initPage(Corso corso) {
        this.corso = corso;
        ricette = new ArrayList<>();
        this.setRootFunctionalities();
    }

    private HBox createTimeSpinnerInizio() {
        hourSpinnerInizio = new Spinner<>();
        hourSpinnerInizio.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(6, 18, 12));
        hourSpinnerInizio.setEditable(true);

        minuteSpinnerInizio = new Spinner<>();
        minuteSpinnerInizio.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        minuteSpinnerInizio.setEditable(true);

        HBox hbox = new HBox(5, hourSpinnerInizio, new Label(":"), minuteSpinnerInizio);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private HBox createTimeSpinnerFine() {
        hourSpinnerFine = new Spinner<>();
        hourSpinnerFine.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(6, 20, 12));
        hourSpinnerFine.setEditable(true);

        minuteSpinnerFine = new Spinner<>();
        minuteSpinnerFine.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        minuteSpinnerFine.setEditable(true);

        HBox hbox = new HBox(5, hourSpinnerFine, new Label(":"), minuteSpinnerFine);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private HBox createOrarioBox() {
        HBox orarioBox = new HBox(50);
        orarioBox.setAlignment(Pos.CENTER_LEFT);

        Label metaOrarioInizio = new Label("Orario Inizio *");

        Label metaOrarioFine = new Label("Orario Fine *");
        erroreOrario = new Label();
        erroreOrario.setStyle("-fx-text-fill: red;-fx-background-color: transparent");

        orarioBox.getChildren().addAll(new VBox(10, new HBox(10, new VBox(metaOrarioInizio, createTimeSpinnerInizio()),
                new VBox(metaOrarioFine, createTimeSpinnerFine())), erroreOrario));
        return orarioBox;
    }

    private HBox createDateBox() {
        HBox dateBox = new HBox(30);
        VBox dataSessioneBox = new VBox(5);
        erroreDataSessione = new Label();
        Label metaDataInizio = new Label("Data Inizio *");
        erroreDataSessione.setStyle("-fx-text-fill: red;-fx-background-color: transparent");
        dateInizio = new DatePicker();
        dateInizio.setValue(LocalDate.now());
        dataSessioneBox.getChildren().addAll(metaDataInizio, dateInizio, erroreDataSessione);

        dateBox.getChildren().add(dataSessioneBox);
        dateBox.setAlignment(Pos.CENTER_LEFT);
        return dateBox;
    }

    private void setRootFunctionalities() {
        root.setAlignment(Pos.CENTER);
        root.setSpacing(30);
        root.getChildren().addAll(
                createTopBox(),
                createLinkOrLuogoBox(),
                createDateBox(),
                createOrarioBox(),
                createRicettaBox(),
                createBottomBox()
        );
    }

    private VBox createLinkOrLuogoBox() {
        VBox linkOrLuogoBox = new VBox(20);
        linkOrLuogoBox.setAlignment(Pos.CENTER_LEFT);

        VBox box = new VBox(5);

        linkOrLuogoField = new TextField();
        linkOrLuogoField.setPrefWidth(200);
        linkOrLuogoField.setMaxWidth(200);

        errorLinkOrLuogoLabel = new Label();
        errorLinkOrLuogoLabel.setStyle("-fx-text-fill: red;-fx-background-color: transparent");
        Label metaLinkOrLuogo = new Label();

        box.getChildren().addAll(metaLinkOrLuogo, linkOrLuogoField);

        linkOrLuogoBox.getChildren().addAll(
                createChooseSessioneBox(metaLinkOrLuogo,linkOrLuogoField),
                box,
                errorLinkOrLuogoLabel);

        return linkOrLuogoBox;
    }

    private LocalDateTime getLocalDateTimeFromFields(Spinner<Integer> hourSpinner, Spinner<Integer> minuteSpinner) {
        int hour = hourSpinner.getValue();
        int minute = minuteSpinner.getValue();
        LocalDate data = dateInizio.getValue();
        return LocalDateTime.of(data, LocalTime.of(hour, minute));
    }

    private void validConferma() throws Exception {

        erroreOrario.setText("");
        erroreDataSessione.setText("");
        errorLinkOrLuogoLabel.setText("");

        LocalDateTime inizio = getLocalDateTimeFromFields(hourSpinnerInizio, minuteSpinnerInizio);
        LocalDateTime fine = getLocalDateTimeFromFields(hourSpinnerFine, minuteSpinnerFine);
        LocalDate dataSessione = dateInizio.getValue();

        if (linkOrLuogoField.getText().isEmpty())
            throw new LinkOrLuogoEmptyException();

        if (dataSessione.isBefore(LocalDate.now()) || dataSessione.isEqual(LocalDate.now()))
            throw new DataNelPassatoException();

        if (fine.isBefore(inizio))
            throw new OrarioNonValidoException();

        long durataMinuti = Duration.between(inizio, fine).toMinutes();
        Float durataOre = durataMinuti / 60f;

        if (durataOre > 8)
            throw new OrarioMassimoOttoOreException();

        if(durataOre < 1)
            throw new SessioneAlmenoUnOraException();

        if(ricette.isEmpty())
            throw new AlmenoUnaRicettaException();
    }

    private void createSessione() throws SQLException {
        LocalDateTime inizio = getLocalDateTimeFromFields(hourSpinnerInizio, minuteSpinnerInizio);
        LocalDateTime fine = getLocalDateTimeFromFields(hourSpinnerFine, minuteSpinnerFine);
        LocalDate dataSessione = dateInizio.getValue();
        String linkOrLuogo = linkOrLuogoField.getText().trim();

        long durataMinuti = Duration.between(inizio, fine).toMinutes();
        Float durataOre = durataMinuti / 60f;

        String prompt = linkOrLuogoField.getPromptText().toLowerCase();
        if (prompt.contains("link")) {
            sessione = new SessioneOnline(
                    dataSessione,
                    linkOrLuogo,
                    durataOre,
                    inizio,
                    corso
            );

        } else if (prompt.contains("luogo")) {
            sessione = new SessionePresenza(
                    dataSessione,
                    linkOrLuogo,
                    durataOre,
                    inizio,
                    corso
            );
        }

        this.setSessione(sessione);
    }

    private VBox createBottomBox(){
        VBox bottomBox = new VBox(5);
        errorInserimentoDatiLabel = new Label();
        errorInserimentoDatiLabel.setStyle("-fx-text-fill: red");
        bottomBox.getChildren().addAll(errorInserimentoDatiLabel,createConfermaButton(),createAnnulaButton());
        bottomBox.setAlignment(Pos.BOTTOM_CENTER);
        return  bottomBox;
    }

    private MyButton createAnnulaButton(){
        MyButton annullaButton = new MyButton("Annulla", MyButton.ButtonType.SECONDARY);
        annullaButton.setOnAction(e -> this.close());
        return annullaButton;
    }

    private MyButton createConfermaButton() {
        MyButton confirmButton = new MyButton("Conferma", MyButton.ButtonType.PRIMARY);

        confirmButton.setOnAction(e -> {
            erroreOrario.setText("");
            erroreDataSessione.setText("");
            errorLinkOrLuogoLabel.setText("");

            try {
                validConferma();
                createSessione();
                controller.insertSessione(sessione);
                controller.insertRicetteToSessione(ricette,sessione);
                sessione.getCorso().addSessione(sessione);
                controller.refreshAccountPage();
                controller.refreshCorsi();
                this.close();
            } catch (LinkOrLuogoEmptyException ex) {
                errorLinkOrLuogoLabel.setText("Campo obbligatorio");
            } catch (DataNelPassatoException ex) {
                erroreDataSessione.setText("La sessione deve iniziare almeno domani");
            } catch (OrarioNonValidoException ex) {
                erroreOrario.setText("La sessione deve terminare dopo almeno 1 ora");
            } catch (OrarioMassimoOttoOreException | SessioneAlmenoUnOraException ex) {
                erroreOrario.setText("La sessione supera la durata max (8 ore)");
            } catch (AlmenoUnaRicettaException ARE) {
                errorAlmenoUnaRicettaLabel.setText("La sessione deve trattare almeno una ricetta");
            } catch (SQLException sql){
                if (isFrequenzaSettimanaleLimiteError(sql.getSQLState())) {
                    errorInserimentoDatiLabel.setText("Frequenza limite settimanale superata");
                } else
                     errorInserimentoDatiLabel.setText("Errore inserimento dati. Riprovare più tardi");

            } catch (Exception ex) {
                showDialog("Errore di sistema. Riprovare più tardi");
            }
        });

        confirmButton.setAlignment(Pos.CENTER);

        return confirmButton;
    }

    private boolean isFrequenzaSettimanaleLimiteError(String error){
        return error.equals("P0001");
    }

    private HBox createChooseSessioneBox(Label metaLinkOrLuogo, TextField linkOrLuogoField) {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER_LEFT);

        ToggleGroup group = new ToggleGroup();

        ToggleButton onlineBtn = new ToggleButton("Online");
        ToggleButton presenzaBtn = new ToggleButton("Presenza");

        onlineBtn.setToggleGroup(group);
        presenzaBtn.setToggleGroup(group);

        presenzaBtn.setSelected(true);
        setClickedButtonAesthetic(presenzaBtn);
        setNotClickedButtonAesthetic(onlineBtn);
        metaLinkOrLuogo.setText("Luogo *");
        linkOrLuogoField.setPromptText("Inserisci Luogo");

        onlineBtn.setOnAction(e -> {
            if (onlineBtn.isSelected()) {
                setClickedButtonAesthetic(onlineBtn);
                setNotClickedButtonAesthetic(presenzaBtn);
                metaLinkOrLuogo.setText("Link *");
                linkOrLuogoField.setPromptText("Inserisci Link");
            }
        });

        presenzaBtn.setOnAction(e -> {
            if (presenzaBtn.isSelected()) {
                setClickedButtonAesthetic(presenzaBtn);
                setNotClickedButtonAesthetic(onlineBtn);
                metaLinkOrLuogo.setText("Luogo *");
                linkOrLuogoField.setPromptText("Inserisci Luogo");
            }
        });

        box.getChildren().addAll(presenzaBtn, onlineBtn);
        return box;
    }

    public void setCorso(Corso corso) {
        this.corso = corso;
    }

    public void setSessione(Sessione sessione) {
        this.sessione = sessione;
    }

    private HBox createTopBox() {
        HBox topBox = new HBox(5);

        topBox.setSpacing(10);

        Label titolo = new Label("Nuova Sessione !");

        titolo.setStyle("-fx-font-weight: bold; -fx-text-fill: #3A6698;-fx-alignment: CENTER;-fx-background-color: transparent;-fx-font-size: 30");


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        CircleButton closeButton = createCloseButton();
        CircleButton minimizeButton = createMinimizeButton();
        topBox.getChildren().addAll(titolo, spacer, minimizeButton, closeButton);
        return topBox;
    }

    private CircleButton createMinimizeButton() {
        CircleButton minimizeButton = new CircleButton();
        minimizeButton.setToMinimizeButtonWithAction(this);
        return minimizeButton;
    }

    private CircleButton createCloseButton() {
        CircleButton minimizeButton = new CircleButton();
        minimizeButton.setToCloseButtonWithAction(this);
        return minimizeButton;
    }

    public void updateRicetteAggiunte(Ricetta ricetta) {
        ricette.add(ricetta);
        numeroRicette.setText("Ricette inserite: " + ricette.size());
    }

    private VBox createRicettaBox() {
        VBox ricettaBox = new VBox(5);
        ricettaBox.setAlignment(Pos.TOP_LEFT);

        numeroRicette = new Label("Ricette aggiunte: " + ricette.size());
        numeroRicette.setStyle("-fx-text-fill: lightgray;");

        errorAlmenoUnaRicettaLabel = new Label();
        errorAlmenoUnaRicettaLabel.setStyle("-fx-text-fill: red;");


        MyButton aggiungiRicettaBtn = new MyButton("Aggiungi ricetta", MyButton.ButtonType.PRIMARY);
        aggiungiRicettaBtn.setSize(160, 30);

        ricettaBox.getChildren().addAll(numeroRicette, aggiungiRicettaBtn,errorAlmenoUnaRicettaLabel);

        aggiungiRicettaBtn.setOnAction(e -> {
            controller.openAggiungiRicettaPage(this);
        });

        return ricettaBox;
    }

    private void setNotClickedButtonAesthetic(ToggleButton button) {
        String base = "-fx-background-color:white;-fx-text-fill:#3a6698;-fx-border-color:#3a6698;" +
                "-fx-border-width:1.5px;-fx-border-radius:7;-fx-background-radius:7;-fx-cursor:hand;";

        button.setStyle(base);
    }

    private void setClickedButtonAesthetic(ToggleButton button) {
        String selected = "-fx-background-color:#3a6698;-fx-text-fill:white;-fx-border-color:#3a6698;" +
                "-fx-border-width:1.5px;-fx-border-radius:7;-fx-background-radius:7;-fx-cursor:hand;";

        button.setStyle(selected);
    }

}
