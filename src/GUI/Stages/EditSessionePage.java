package GUI.Stages;

import Controller.Controller;
import Entity.*;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class EditSessionePage extends Stage {
    private Controller controller;
    private Sessione sessione;

    private VBox root;
    private VBox formBox;
    private HBox topHbox;
    private VBox footerVbox;

    private DatePicker dataPicker;
    private Spinner<Integer> hourSpinner;
    private Spinner<Integer> minuteSpinner;
    private TextField durataField;
    private TextField luogoField;
    private TextField linkField;

    public EditSessionePage(Controller controller) {
        this.controller = controller;
        this.initStyle(StageStyle.TRANSPARENT);

        root = new VBox(15);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.TOP_CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(30), Insets.EMPTY)));
        root.setBorder(new Border(new BorderStroke(Color.valueOf("#3A6698"), BorderStrokeStyle.SOLID, new CornerRadii(30), new BorderWidths(2))));

        topHbox = new HBox(15);
        topHbox.setAlignment(Pos.TOP_CENTER);
        topHbox.setSpacing(20);
        topHbox.setPadding(new Insets(20, 0, 10, 0));

        formBox = new VBox(10);
        formBox.setAlignment(Pos.TOP_LEFT);
        formBox.setPadding(new Insets(0, 0, 0, 30));

        footerVbox = new VBox(10);
        footerVbox.setAlignment(Pos.BOTTOM_CENTER);
        footerVbox.setSpacing(20);
        footerVbox.setPadding(new Insets(0, 0, 20, 0));

        root.getChildren().addAll(topHbox, formBox, footerVbox);

        Scene scene = new Scene(root, 800, 600);
        scene.setFill(Color.TRANSPARENT);


        scene.getStylesheets().add(
                getClass().getResource("/Media/StyleSheets/fieldsAndBoxesStyle.css").toExternalForm()
        );

        this.initStyle(StageStyle.TRANSPARENT);
        this.setScene(scene);

        this.setScene(scene);
    }

    public void initPage(Sessione sessione) {
        this.sessione = sessione;
        Corso corso = sessione.getCorso();

        topHbox.getChildren().clear();
        formBox.getChildren().clear();
        footerVbox.getChildren().clear();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        Label titoloLabel = new Label(corso.getNome() +" - "+ sessione.getData().format(dateFormatter));
        titoloLabel.setFont(Font.font("System", FontWeight.BOLD, 36));
        titoloLabel.setTextFill(Color.valueOf("#3A6698"));
        topHbox.getChildren().add(titoloLabel);

        LocalDate oggi = LocalDate.now();
        LocalDate startOfWeek = oggi.with(java.time.DayOfWeek.MONDAY);
        LocalDate endOfWeek = oggi.with(java.time.DayOfWeek.SUNDAY);

        dataPicker = new DatePicker(sessione.getData());
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


        HBox timeBox = createTimeSpinner();
        formBox.getChildren().add(labeledNode("Ora:", timeBox));

        durataField = new TextField(String.valueOf(sessione.getDurata()));
        durataField.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,1}(\\.\\d{0,1})?")) {
                return change;
            }
            return null;
        }));
        formBox.getChildren().add(labeledNode("Durata (ore):", durataField));

        if (sessione instanceof SessionePresenza sp) {
            luogoField = new TextField(sp.getLuogo());
            formBox.getChildren().add(labeledNode("Luogo:", luogoField));
        } else if (sessione instanceof SessioneOnline so) {
            linkField = new TextField(so.getLinkIncontro());
            formBox.getChildren().add(labeledNode("Link:", linkField));
        }

        Button salvaBtn = new Button("Salva");
        styleButton(salvaBtn, Color.valueOf("#3a6698"));
        salvaBtn.setOnAction(e -> salvaModifiche());

        Button annullaBtn = new Button("Annulla");
        styleButton(annullaBtn, Color.valueOf("#da3d26"));
        annullaBtn.setOnAction(e -> this.close());

        footerVbox.getChildren().addAll(salvaBtn, annullaBtn);
    }

    private HBox labeledNode(String label, Control control) {
        Label l = new Label(label);
        l.setFont(Font.font("System", FontWeight.BOLD, 16));
        HBox box = new HBox(10, l, control);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }


    private HBox createTimeSpinner() {
        hourSpinner = new Spinner<>();
        hourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 12, sessione.getOra().getHour()));
        hourSpinner.setEditable(true);

        minuteSpinner = new Spinner<>();
        minuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, sessione.getOra().getMinute()));
        minuteSpinner.setEditable(true);

        HBox hbox = new HBox(5, hourSpinner, new Label(":"), minuteSpinner);
        hbox.setAlignment(Pos.CENTER_LEFT);
        return hbox;
    }

    private void salvaModifiche() {
        try {
            sessione.setData(dataPicker.getValue());

            LocalDate data = dataPicker.getValue();
            int h = hourSpinner.getValue();
            int m = minuteSpinner.getValue();

            LocalDateTime dateTime = LocalDateTime.of(data, LocalTime.of(h, m));

            sessione.setOra(dateTime);

            sessione.setDurata(Float.parseFloat(durataField.getText()));

            System.out.println(sessione.getOra().getHour() + ":" + sessione.getOra().getMinute());
            System.out.println(sessione.getData());
            System.out.println(sessione.getDurata());

            if (sessione instanceof SessionePresenza sp) {
                sp.setLuogo(luogoField.getText());
                System.out.println(luogoField.getText());
            } else if (sessione instanceof SessioneOnline so) {
                so.setLinkIncontro(linkField.getText());
                System.out.println(linkField.getText());
            }

            // TODO finish update dao
            controller.updateSessione(sessione);

            this.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Errore nel salvataggio: " + ex.getMessage());
            alert.show();
        }
    }

    private void styleButton(Button button, Color color) {
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setBackground(new Background(new BackgroundFill(color, new CornerRadii(8), Insets.EMPTY)));
        button.setCursor(javafx.scene.Cursor.HAND);
    }

    public Sessione getSessione() {
        return sessione;
    }

    private HBox labeledNode(String label, javafx.scene.Node node) {
        Label l = new Label(label);
        l.setFont(Font.font("System", FontWeight.BOLD, 16));
        HBox box = new HBox(10, l, node);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }
}