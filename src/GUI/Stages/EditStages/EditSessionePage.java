package GUI.Stages.EditStages;

import Controller.Controller;
import Entity.*;
import GUI.Buttons.MyButton;
import GUI.Stages.ClassDataStages.SessionePage;
import GUI.Stages.MyStage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;


public class EditSessionePage extends MyStage {
    private Controller controller;
    private Sessione sessione;

    private ArrayList<Ricetta> ricetteToInsert;
    private ArrayList<Ricetta> ricetteToDelete;

    private BorderPane root;
    private VBox formBox;
    private VBox ricetteList;

    private DatePicker dataPicker;
    private Spinner<Integer> hourSpinnerInizio;
    private Spinner<Integer> minuteSpinnerInizio;

    private Spinner<Integer> hourSpinnerFine;
    private Spinner<Integer> minuteSpinnerFine;

    private TextField luogoField;
    private TextField linkField;

    private Label errorAlmenoUnRicetta;
    private Label erroreInserimentoDati;

    private SessionePage parent;


    public EditSessionePage(Controller controller, SessionePage parent) {
        super(800, 600, RootType.BORDERPANE);
        this.controller = controller;
        this.parent = parent;

        root = getRootBorderPane();

        this.addStylesheet("/Media/StyleSheets/fieldsAndBoxesStyle.css");
    }

    public void initPage(Sessione sessione) {
        this.sessione = sessione;
        this.ricetteToInsert = new ArrayList<>();
        this.ricetteToDelete = new ArrayList<>();
        Corso corso = sessione.getCorso();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Label titoloLabel = new Label(corso.getNome() + " - " + sessione.getData().format(dateFormatter));
        titoloLabel.setFont(Font.font("System", FontWeight.BOLD, 36));
        titoloLabel.setTextFill(Color.valueOf("#3A6698"));

        HBox topBox = new HBox(titoloLabel);
        topBox.setAlignment(Pos.TOP_CENTER);
        topBox.setPadding(new Insets(10, 0, 5, 0));
        root.setTop(topBox);

        formBox = new VBox(10);
        formBox.setAlignment(Pos.TOP_LEFT);
        formBox.setPadding(new Insets(0, 0, 0, 30));

        LocalDate oggi = LocalDate.now();
        dataPicker = new DatePicker(sessione.getData());
        LocalDate dataSessione = sessione.getData();
        LocalDate startOfWeek = dataSessione.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = dataSessione.with(java.time.DayOfWeek.SUNDAY);

        dataPicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date.isBefore(startOfWeek) || date.isAfter(endOfWeek)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: gray;");
                }
            }
        });

        if (oggi.isBefore(startOfWeek) || oggi.isAfter(endOfWeek)) {
            dataPicker.setValue(startOfWeek);
        } else {
            dataPicker.setValue(oggi);
        }

        Label freqWarning = new Label("*Per rispettare la frequenza, è possibile spostare la sessione solo nella settimana di questa.");
        freqWarning.setFont(Font.font("System", FontPosture.ITALIC, 13));
        freqWarning.setTextFill(Color.RED);

        formBox.getChildren().addAll(labeledNode("Data:", dataPicker), freqWarning);

        HBox timeBoxInizio = createTimeSpinnerInizio();
        formBox.getChildren().add(labeledNode("Ora inizio:", timeBoxInizio));

        HBox timeBoxFine = createTimeSpinnerFine();
        formBox.getChildren().add(labeledNode("Ora fine:", timeBoxFine));

        if (sessione instanceof SessionePresenza sp) {
            luogoField = new TextField(sp.getLuogo());
            formBox.getChildren().add(labeledNode("Luogo:", luogoField));
        } else if (sessione instanceof SessioneOnline so) {
            linkField = new TextField(so.getLinkIncontro());
            formBox.getChildren().add(labeledNode("Link:", linkField));
        }

        Label ricetteTrattate = new Label("Ricette trattate: ");
        ricetteTrattate.setStyle("-fx-font-weight: bold;-fx-font-size: 20px;-fx-text-fill: BLACK;-fx-alignment: CENTER_LEFT;");

        ScrollPane ricetteScroll = new ScrollPane(createRicettaList());

        ricetteScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ricetteScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        ricetteScroll.setPrefWidth(250);
        ricetteScroll.setMaxWidth(250);
        ricetteScroll.setPrefHeight(120);
        ricetteScroll.setMinHeight(120);
        ricetteScroll.setMaxHeight(120);
        setErrorAlmenoUnRicettaLabel();
        VBox.setVgrow(ricetteScroll, Priority.NEVER);
        formBox.getChildren().addAll(errorAlmenoUnRicetta, createAggiungiRicettaButton(), ricetteTrattate, ricetteScroll);
        root.setCenter(formBox);
        erroreInserimentoDati = new Label();
        erroreInserimentoDati.setStyle("-fx-text-fill: red");

        MyButton salvaBtn = new MyButton("Salva", MyButton.ButtonType.PRIMARY);
        salvaBtn.setOnAction(e -> salvaModifiche());

        MyButton annullaBtn = new MyButton("Annulla", MyButton.ButtonType.SECONDARY);
        annullaBtn.setOnAction(e -> this.close());

        VBox buttonsBox = new VBox(15, salvaBtn, annullaBtn);
        buttonsBox.setAlignment(Pos.CENTER);

        VBox footerBox = new VBox();
        footerBox.setPadding(new Insets(0, 0, 20, 0));
        footerBox.setAlignment(Pos.CENTER);
        footerBox.getChildren().addAll(
                erroreInserimentoDati,
                buttonsBox
        );
        root.setBottom(footerBox);

    }

    private HBox labeledNode(String label, javafx.scene.Node node) {
        Label l = new Label(label);
        l.setFont(Font.font("System", FontWeight.BOLD, 16));
        HBox box = new HBox(10, l, node);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private HBox createTimeSpinnerInizio() {
        hourSpinnerInizio = new Spinner<>();
        hourSpinnerInizio.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(6, 18, sessione.getOra().getHour()));
        hourSpinnerInizio.setEditable(true);

        minuteSpinnerInizio = new Spinner<>();
        minuteSpinnerInizio.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, sessione.getOra().getMinute()));
        minuteSpinnerInizio.setEditable(true);

        HBox hbox = new HBox(5, hourSpinnerInizio, new Label(":"), minuteSpinnerInizio);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private HBox createTimeSpinnerFine() {
        hourSpinnerFine = new Spinner<>();
        hourSpinnerFine.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(6, 20, sessione.getOra().getHour()));
        hourSpinnerFine.setEditable(true);

        minuteSpinnerFine = new Spinner<>();
        minuteSpinnerFine.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, sessione.getOra().getMinute()));
        minuteSpinnerFine.setEditable(true);

        HBox hbox = new HBox(5, hourSpinnerFine, new Label(":"), minuteSpinnerFine);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private MyButton createAggiungiRicettaButton() {
        MyButton aggiungiRicettaButton = new MyButton("Aggiungi ricetta", MyButton.ButtonType.PRIMARY);
        aggiungiRicettaButton.setSize(120, 30);

        aggiungiRicettaButton.setOnAction(e -> controller.openAggiungiRicettaPage(this));

        return aggiungiRicettaButton;
    }

    private void setErrorAlmenoUnRicettaLabel() {
        errorAlmenoUnRicetta = new Label();
        errorAlmenoUnRicetta.setStyle("-fx-text-fill: red");
    }

    private VBox createRicettaList() {
        ricetteList = new VBox(10);


        if (!sessione.getRicette().isEmpty()) {
            for (Ricetta ricetta : sessione.getRicette())
                ricetteList.getChildren().add(createRicettaBox(ricetta));
        } else {
            Label noRicetteTrattate = new Label("Ancora nessuna ricetta");
            noRicetteTrattate.setStyle("-fx-font-size: 23;-fx-color: BLACK;-fx-alignment: CENTER_LEFT;");
            ricetteList.getChildren().add(noRicetteTrattate);
        }

        return ricetteList;
    }

    private HBox createRicettaBox(Ricetta ricetta) {
        HBox ricettaBox = new HBox(10);

        Label ricettaLabel = new Label("• " + ricetta.getNome());
        ricettaLabel.setStyle("-fx-cursor: hand;-fx-font-size: 17;-fx-text-fill: BLACK;-fx-alignment: TOP_RIGHT;");
        ricettaLabel.setOnMouseClicked(event -> controller.openRicettaPage(ricetta));

        Label removeLabel = new Label("✖");
        removeLabel.setStyle("-fx-cursor: hand;-fx-text-fill: RED;-fx-font: System;-fx-font-size: 18");
        removeLabel.setOnMouseClicked(event -> {
                ricetteToDelete.add(ricetta);
                ricetteToInsert.remove(ricetta);
            aggiornaListaRicette();
        });

        ricettaBox.getChildren().addAll(ricettaLabel, removeLabel);
        return ricettaBox;
    }

    public void updateRicetteAggiunte(Ricetta ricetta) {
        ricetteToInsert.add(ricetta);
        aggiornaListaRicette();
    }

    private void aggiornaListaRicette() {
        ricetteList.getChildren().clear();

        for (Ricetta ricetta : sessione.getRicette()){
            if(!ricetteToDelete.contains(ricetta))
                ricetteList.getChildren().add(createRicettaBox(ricetta));
        }

        for (Ricetta ricetta : ricetteToInsert)
            ricetteList.getChildren().add(createRicettaBox(ricetta));
    }

    private void salvaModifiche() {
        try {
            sessione.setData(dataPicker.getValue());
            LocalDate data = dataPicker.getValue();

            int hInizio = hourSpinnerInizio.getValue();
            int mInizio = minuteSpinnerInizio.getValue();
            LocalDateTime dateTimeInizio = LocalDateTime.of(data, LocalTime.of(hInizio, mInizio));
            sessione.setOra(dateTimeInizio);

            int hFine = hourSpinnerFine.getValue();
            int mFine = minuteSpinnerFine.getValue();
            LocalDateTime dateTimeFine = LocalDateTime.of(data, LocalTime.of(hFine, mFine));

            long durataMinuti = Duration.between(dateTimeInizio, dateTimeFine).toMinutes();

            Float durataOre = durataMinuti / 60f;
            System.out.println("durata: " + durataOre);

            sessione.setDurata(durataOre);

            if (sessione instanceof SessionePresenza sp) {
                sp.setLuogo(luogoField.getText());
            } else if (sessione instanceof SessioneOnline so) {
                so.setLinkIncontro(linkField.getText());
            }

            for(Ricetta ricettaToDelete: ricetteToDelete)
                sessione.getRicette().remove(ricettaToDelete);

            for (Ricetta ricettaToInsert : ricetteToInsert)
                sessione.getRicette().add(ricettaToInsert);

            controller.updateSessione(sessione);
            controller.removeRicetteToSessione(ricetteToDelete,sessione);
            controller.insertRicetteToSessione(ricetteToInsert, sessione);
            controller.refreshCalendario();

            parent.close();
            this.close();
        } catch (SQLException sqlException) {
            if(sqlException.getSQLState() != null && sqlException.getSQLState().equals("23505"))
                erroreInserimentoDati.setText("Il corso ha già una sessione in data: "+dataPicker.getValue());

            sqlException.printStackTrace();

        }catch (Exception ex) {
            erroreInserimentoDati.setText("Errore inserimenti dati. Riprovare più tardi");
            ex.printStackTrace();

        }
    }

    public Sessione getSessione() {
        return sessione;
    }
}
