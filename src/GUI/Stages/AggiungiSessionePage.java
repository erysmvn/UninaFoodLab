package GUI.Stages;

import Controller.Controller;
import Entity.*;
import GUI.Buttons.CircleButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import Exception.OrarioMassimoOttoOreException;
import Exception.*;
import javafx.util.converter.LocalTimeStringConverter;

import static java.time.Duration.between;

public class AggiungiSessionePage extends Stage {
    private Controller controller;

    private VBox root;
    private Scene scene;
    private Label numeroRicette;

    private Corso corso;
    private Sessione sessione;
    private ArrayList<Ricetta> ricette;

    private DatePicker dateInizio;
    private TextField linkOrLuogoField;

    private TextField oraInizioField;
    private TextField minutiInizioField;
    private TextField oraFineField;
    private TextField minutiFineField;

    private Label erroreOrarioInizio;
    private Label erroreOrarioFine;
    private Label erroreDataSessione;
    private Label errorLinkOrLuogoLabel;
    private Label errorInserimentoDatiLabel;
    private Label errorAlmenoUnaRicettaLabel;
    public AggiungiSessionePage(Controller controller) {
        this.controller = controller;

        this.setRootAesthetics();
        this.setSceneAesthetics();
    }

    public void initPage(Corso corso) {
        this.corso = corso;
        ricette = new ArrayList<>();
        this.setRootFunctionalities();
    }

    private HBox createOrarioBox() {
        HBox orarioBox = new HBox(50);
        orarioBox.setAlignment(Pos.CENTER_LEFT);


        VBox orarioInizioBox = new VBox(5);
        Label metaOrarioInizio = new Label("Orario Inizio *");
        erroreOrarioInizio = new Label();
        erroreOrarioInizio.setStyle("-fx-text-fill: red;-fx-background-color: transparent");

        HBox inizioFields = new HBox(5);
        oraInizioField = new TextField();
        oraInizioField.setText("12");

        oraInizioField.setPrefWidth(40);
        minutiInizioField = new TextField();
        minutiInizioField.setText("00");

        minutiInizioField.setPrefWidth(40);

        setTwoDigitNumericField(oraInizioField, 23);
        setTwoDigitNumericField(minutiInizioField, 59);

        inizioFields.getChildren().addAll(oraInizioField, new Label(":"), minutiInizioField);
        orarioInizioBox.getChildren().addAll(metaOrarioInizio, inizioFields, erroreOrarioInizio);

        VBox orarioFineBox = new VBox(5);
        Label metaOrarioFine = new Label("Orario Fine *");
        erroreOrarioFine = new Label();
        erroreOrarioFine.setStyle("-fx-text-fill: red;-fx-background-color: transparent");

        HBox fineFields = new HBox(5);
        oraFineField = new TextField();
        oraFineField.setText("12");
        oraFineField.setPrefWidth(40);
        minutiFineField = new TextField();
        minutiFineField.setPrefWidth(40);
        minutiFineField.setText("00");
        setTwoDigitNumericField(oraFineField, 23);
        setTwoDigitNumericField(minutiFineField, 59);

        fineFields.getChildren().addAll(oraFineField, new Label(":"), minutiFineField);
        orarioFineBox.getChildren().addAll(metaOrarioFine, fineFields, erroreOrarioFine);

        orarioBox.getChildren().addAll(orarioInizioBox, orarioFineBox);
        return orarioBox;
    }

    private void setTwoDigitNumericField(TextField field, int maxValue) {
        field.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.matches("\\d{0,2}")) {
                field.setText(oldText);
                return;
            }
            if (!newText.isEmpty()) {
                int value = Integer.parseInt(newText);
                if (value > maxValue) {
                    field.setText(oldText);
                }
            }
        });
    }

    private HBox createDateBox() {
        HBox dateBox = new HBox(30);
        VBox dataSessioneBox = new VBox(5);
        erroreDataSessione = new Label();
        Label metaDataInizio = new Label("Data Inizio *");
        erroreDataSessione.setStyle("-fx-text-fill: red;-fx-background-color: transparent");
        dateInizio = new DatePicker();
        dataSessioneBox.getChildren().addAll(metaDataInizio, dateInizio, erroreDataSessione);

        dateBox.getChildren().add(dataSessioneBox);
        dateBox.setAlignment(Pos.CENTER_LEFT);
        return dateBox;
    }

    private void setSceneAesthetics() {
        scene = new Scene(root, 450, 700);
        scene.setFill(Color.TRANSPARENT);
        scene.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.W) {
                this.close();
            }
        });

        scene.getStylesheets().add(
                getClass().getResource("/Media/StyleSheets/fieldsAndBoxesStyle.css").toExternalForm()
        );

        this.initStyle(StageStyle.TRANSPARENT);
        this.setScene(scene);

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

    private void setRootAesthetics() {
        root = new VBox(15);
        root.setPadding(new Insets(35, 20, 10, 20));
        root.setAlignment(Pos.TOP_LEFT);
        root.setBackground(new Background(
                new BackgroundFill(Color.WHITE, new CornerRadii(30), Insets.EMPTY)
        ));
        root.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(30),
                new BorderWidths(2)
        )));
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

    private LocalTime getLocalTimeFromFields(TextField hourField, TextField minuteField) {
        String hour = hourField.getText();
        String minute = minuteField.getText();

        if(hour.isEmpty() || minute.isEmpty()){
            hour =  "12";
            minute = "00";
        }
        String timeString = hour + ":" + minute;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTimeStringConverter converter = new LocalTimeStringConverter(formatter, null);
        return converter.fromString(timeString);
    }

    private void validConferma() throws Exception {

        erroreOrarioInizio.setText("");
        erroreOrarioFine.setText("");
        erroreDataSessione.setText("");
        errorLinkOrLuogoLabel.setText("");


        LocalTime inizio = getLocalTimeFromFields(oraInizioField,minutiInizioField);
        LocalTime fine = getLocalTimeFromFields(oraFineField,minutiFineField);
        LocalDate dataSessione = dateInizio.getValue();

        if (inizio == null)
            throw new OrarioInizioEmptyException();

        if (fine == null)
            throw new OrarioFineEmptyException();


        if (dataSessione == null)
            throw new DataSessioneEmptyException();

        if (dataSessione.isBefore(LocalDate.now()))
            throw new DataNelPassatoException();

        if (fine.isBefore(inizio))
            throw new OrarioNonValidoExceptio();

        Duration durata = between(inizio, fine);
        if (durata.toHours() > 8)
            throw new OrarioMassimoOttoOreException();

        if(durata.toHours() < 1)
            throw new SessioneAlmenoUnOraException();

        if (linkOrLuogoField.getText() == null || linkOrLuogoField.getText().trim().isEmpty())
            throw new LinkOrLuogoEmptyException();

        if(ricette.isEmpty())
            throw new AlmenoUnaRicettaException();


    }

    private void createSessione() throws SQLException {
        LocalTime inizio = getLocalTimeFromFields(oraInizioField, minutiInizioField);
        LocalTime fine = getLocalTimeFromFields(oraFineField, minutiFineField);
        LocalDate dataSessione = dateInizio.getValue();
        String linkOrLuogo = linkOrLuogoField.getText().trim();

        LocalDateTime OraInizio = LocalDateTime.of(dataSessione, inizio);

        float durata = Duration.between(inizio, fine).toHours();
        String prompt = linkOrLuogoField.getPromptText().toLowerCase();
        if (prompt.contains("link")) {
            sessione = new SessioneOnline(
                    dataSessione,
                    linkOrLuogo,
                    durata,
                    OraInizio,
                    corso
            );

        } else if (prompt.contains("luogo")) {
            sessione = new SessionePresenza(
                    dataSessione,
                    linkOrLuogo,
                    durata,
                    OraInizio,
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

    private Button createAnnulaButton(){
        Button annulaButton = new Button("Annula");
        annulaButton.setPrefSize(80, 30);

        annulaButton.setStyle("-fx-background-color: #da3d26;-fx-text-fill: white;-fx-border-radius: 7;-fx-border-width: 1;");
        annulaButton.setOnMouseEntered(e -> annulaButton.setOpacity(0.8));
        annulaButton.setOnMouseExited(e -> annulaButton.setOpacity(1.0));

        annulaButton.setOnAction(e -> this.close());

        return annulaButton;
    }

    private Button createConfermaButton() {
        Button confirmButton = new Button("Conferma");
        confirmButton.setStyle("-fx-text-fill: white;-fx-border-radius: 7;-fx-border-width: 1; -fx-background-color: #3a6698;");
        confirmButton.setOnMouseEntered(e -> confirmButton.setOpacity(0.8));
        confirmButton.setOnMouseExited(e -> confirmButton.setOpacity(1.0));

        confirmButton.setOnAction(e -> {
            erroreOrarioInizio.setText("");
            erroreOrarioFine.setText("");
            erroreDataSessione.setText("");
            errorLinkOrLuogoLabel.setText("");

            try {
                validConferma();
                createSessione();
                controller.insertRicettaToSessione(ricette,sessione);
                this.close();
            } catch (OrarioInizioEmptyException ex) {
                erroreOrarioInizio.setText("Inserire ora di inizio");
            } catch (OrarioFineEmptyException ex) {
                erroreOrarioFine.setText("Inserire ora di fine");
            } catch (DataSessioneEmptyException ex) {
                erroreDataSessione.setText("Inserire data sessione");
            } catch (DataNelPassatoException ex) {
                erroreDataSessione.setText("Data nel passato non ammessa");
            } catch (OrarioNonValidoExceptio ex) {
                erroreOrarioFine.setText("Orario fine < inizio");
            } catch (OrarioMassimoOttoOreException ex) {
                erroreOrarioFine.setText("Durata > 8 ore");
            } catch (LinkOrLuogoEmptyException ex) {
                errorLinkOrLuogoLabel.setText("Campo obbligatorio");
            } catch (SessioneAlmenoUnOraException SAOE) {
                erroreOrarioFine.setText("Durata minima 1 ora");
            } catch (AlmenoUnaRicettaException ARE) {
                errorAlmenoUnaRicettaLabel.setText("La sessione deve trattare almeno una ricetta");
            } catch (SQLException sql ) {
                errorInserimentoDatiLabel.setText("Errore inserimento dati. Riprovare più tardi");
            }catch (Exception ex) {
                System.err.println("Errore inatteso: " + ex.getMessage());
            }
        });

        confirmButton.setAlignment(Pos.CENTER);

        return confirmButton;
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


        Button aggiungiRicettaBtn = new Button("Aggiungi ricetta");
        ricettaBox.getChildren().addAll(numeroRicette, aggiungiRicettaBtn,errorAlmenoUnaRicettaLabel);

        aggiungiRicettaBtn.setStyle("-fx-text-fill: white;-fx-border-radius: 7;-fx-border-width: 1; -fx-background-color: #3a6698;");

        aggiungiRicettaBtn.setOnMouseEntered(e -> aggiungiRicettaBtn.setOpacity(0.8));
        aggiungiRicettaBtn.setOnMouseExited(e -> aggiungiRicettaBtn.setOpacity(1.0));

        aggiungiRicettaBtn.setOnAction(e -> {
            controller.openAggiungiRicettaPage();
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
