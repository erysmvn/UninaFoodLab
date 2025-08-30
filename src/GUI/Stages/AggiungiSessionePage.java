package GUI.Stages;

import Controller.Controller;
import DAO.SessioneDAO;
import DB.DBConnection;
import Entity.*;
import GUI.Buttons.CircleButton;
import com.calendarfx.view.TimeField;
import com.sun.javafx.scene.layout.region.Margins;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.Connection;
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
    private DBConnection dbc;
    private Connection con;
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

    public AggiungiSessionePage(Controller controller) {
        this.controller = controller;
        this.dbc = controller.getDBConnection();
        con = dbc.getConnection();
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
        scene = new Scene(root, 850, 650);
        scene.setFill(Color.TRANSPARENT);
        scene.setOnKeyPressed(e -> {
            if (e.isControlDown() && e.getCode() == KeyCode.W) {
                this.close();
            }
        });

        this.initStyle(StageStyle.TRANSPARENT);
        this.setScene(scene);

    }

    private void setRootFunctionalities() {

        Region spacer = new Region();
        spacer.setPrefHeight(40);

        root.getChildren().addAll(
                createTopBox(),
                createLinkOrLuogoBox(),
                spacer,
                createDateBox(),
                createOrarioBox(),
                createRicettaBox(),
                createConfermaButton()
        );
    }

    private void setRootAesthetics() {
        root = new VBox(15);
        root.setPadding(new Insets(35, 20, 20, 20));
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
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER_LEFT);

        linkOrLuogoField = new TextField();
        linkOrLuogoField.setPrefWidth(200);
        linkOrLuogoField.setMaxWidth(200);

        errorLinkOrLuogoLabel = new Label();
        errorLinkOrLuogoLabel.setStyle("-fx-text-fill: red;-fx-background-color: transparent");
        Label metaLinkOrLuogo = new Label();

        if (corso.getModalita_corso() == null
                || corso.getModalita_corso().getLabel().equalsIgnoreCase("Online e in presenza")) {
            box.getChildren().addAll(createChooseSessioneBox(metaLinkOrLuogo, linkOrLuogoField));
        } else if (corso.getModalita_corso().getLabel().equalsIgnoreCase("online")) {
            metaLinkOrLuogo.setText("Link *");
            linkOrLuogoField.setPromptText("Inserisci Link");
        } else if (corso.getModalita_corso().getLabel().equalsIgnoreCase("presenza")) {
            metaLinkOrLuogo.setText("Luogo *");
            linkOrLuogoField.setPromptText("Inserisci luogo");
        }
        Region spacer = new Region();
        spacer.setPrefHeight(25);
        box.getChildren().addAll(spacer, metaLinkOrLuogo, linkOrLuogoField,errorLinkOrLuogoLabel);
        return box;
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

        //todo metodo controller che inserisce la sessione e poi serve l'id così costruiamo la sessione con l'id
        this.setSessione(sessione);
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
                erroreOrarioFine.setText("La sessione deve trattare almeno una ricetta");
            } catch (SQLException sql ) {
                erroreOrarioFine.setText("Errore inserimento dati. Riprovare più tardi");
                sql.printStackTrace();
            }catch (Exception ex) {
                System.err.println("Errore inatteso: " + ex.getMessage());
            }
        });

        confirmButton.setAlignment(Pos.CENTER);

        return confirmButton;
    }


    private HBox createChooseSessioneBox(Label metaLinkOrLuogo, TextField linkOrLuogoField) {
        HBox box = new HBox(10);
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
        topBox.setAlignment(Pos.TOP_RIGHT);
        topBox.setSpacing(10);
        Label titolo = new Label("Nuova Sessione !");

        Font robotoFont = Font.loadFont(
                getClass().getResourceAsStream("/Media/Fonts/Roboto.ttf"),
                40
        );

        titolo.setFont(robotoFont);
        titolo.setStyle("-fx-font-weight: bold; -fx-text-fill: #3A6698;-fx-alignment: CENTER;-fx-background-color: transparent;");

        Region spacer = new Region();
        Region spacer1 = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        CircleButton closeButton = createCloseButton();
        CircleButton minimizeButton = createMinimizeButton();
        topBox.getChildren().addAll(spacer, titolo, spacer1, minimizeButton, closeButton);
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
        VBox container = new VBox(5);
        container.setAlignment(Pos.TOP_LEFT);
        numeroRicette = new Label("Ricette aggiunte: " + ricette.size());
        numeroRicette.setStyle("-fx-text-fill: lightgray;");
        Button aggiungiRicettaBtn = new Button("Aggiungi ricetta");
        container.getChildren().addAll(numeroRicette, aggiungiRicettaBtn);
        aggiungiRicettaBtn.setStyle("-fx-text-fill: white;-fx-border-radius: 7;-fx-border-width: 1; -fx-background-color: #3a6698;");
        aggiungiRicettaBtn.setOnMouseEntered(e -> aggiungiRicettaBtn.setOpacity(0.8));
        aggiungiRicettaBtn.setOnMouseExited(e -> aggiungiRicettaBtn.setOpacity(1.0));
        aggiungiRicettaBtn.setOnAction(e -> {
            controller.openAggiungiRicettaPage();
        });

        return container;
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


    public static class DataNelPassatoException extends Exception {
        public DataNelPassatoException() {
            super("La data non può essere nel passato");
        }
    }
}
