package GUI.Stages;

import Controller.Controller;
import DB.DBConnection;
import Entity.Corso;
import Entity.Sessione;
import Entity.SessionePresenza;
import GUI.Buttons.CircleButton;
import com.calendarfx.view.TimeField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import com.calendarfx.view.TimeField;
import java.sql.Connection;
import java.util.Calendar;

public class AggiungiSessionePage extends Stage {
    private Controller controller;
    private DBConnection dbc;
    private Connection con;
    private VBox root;
    private Scene scene;
    private Corso corso;
    private Sessione sessione;

    public AggiungiSessionePage(Controller controller) {
        this.controller = controller;
        this.dbc = controller.getDBConnection();
        con = dbc.getConnection();
        this.setRootAesthetics();
        this.setSceneAesthetics();
    }

    public void initPage(Corso corso){
        this.corso = corso;
        this.setRootFunctionalities();
    }


    private HBox createOrarioBox(){
        HBox orarioBox = new HBox(50);
        orarioBox.setAlignment(Pos.CENTER_LEFT);

        VBox orarioInizioBox = new VBox(5);
        Label metaOrarioInizio = new Label("Orario Inizio *");
        Label erroreOrarioInizio = new Label();
        erroreOrarioInizio.setStyle("-fx-text-fill: red;-fx-background-color: transparent");
        TimeField orarioInizio = new TimeField();
        orarioInizioBox.getChildren().addAll(metaOrarioInizio, orarioInizio, erroreOrarioInizio);

        VBox orarioFineBox = new VBox(5);
        Label metaOrarioFine = new Label("Orario Fine *");
        Label erroreOrarioFine = new Label();
        erroreOrarioFine.setStyle("-fx-text-fill: red;-fx-background-color: transparent");
        TimeField orarioFine = new TimeField();
        orarioFineBox.getChildren().addAll(metaOrarioFine, orarioFine, erroreOrarioFine);

        orarioBox.getChildren().addAll(orarioInizioBox, orarioFineBox);
        return orarioBox;
    }

    private HBox createDateBox(){
        HBox dateBox = new HBox(30);
        VBox dataInizioBox = new VBox(5);
        Label erroreDataInizio = new Label();
        Label metaDataInizio = new Label("Data Inizio *");
        erroreDataInizio.setStyle("-fx-text-fill: red;-fx-background-color: transparent");
        DatePicker dateInizio = new DatePicker();

        dataInizioBox.getChildren().addAll(metaDataInizio,dateInizio,erroreDataInizio);

        VBox dataFineBox = new VBox(5);
        Label erroreDataFine = new Label();
        Label metaDataFine = new Label("Data Fine *");
        erroreDataFine.setStyle("-fx-text-fill: red;-fx-background-color: transparent");
        DatePicker dateFine = new DatePicker();
        dataFineBox.getChildren().addAll(metaDataFine,dateFine,erroreDataFine);

        dateBox.getChildren().addAll(dataInizioBox,dataFineBox);
        dateBox.setAlignment(Pos.CENTER_LEFT);
        return dateBox;
    }

    private HBox createTopBox(){
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
        Region spacer1 =  new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        CircleButton closeButton = createCloseButton();
        CircleButton minimizeButton = createMinimizeButton();
        topBox.getChildren().addAll(spacer,titolo,spacer1,minimizeButton, closeButton);
        return topBox;
    }

    private void setSceneAesthetics(){
        scene = new Scene(root, 850, 650);
        scene.setFill(Color.TRANSPARENT);
        scene.setOnKeyPressed(e->{
            if(e.isControlDown() && e.getCode() == KeyCode.W){
                this.close();
            }
        });

        this.initStyle(StageStyle.TRANSPARENT);
        this.setScene(scene);

    }

    private void setRootFunctionalities(){

        Region spacer = new Region();
        spacer.setPrefHeight(40);

        root.getChildren().addAll(createTopBox(),createLinkOrLuogoBox(),spacer,createDateBox(),createOrarioBox());
    }

    private void setRootAesthetics(){
        root = new VBox(15);
        root.setPadding(new Insets(35, 20, 50, 50));
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

    private VBox createLinkOrLuogoBox(){
        VBox box = new VBox(5);
        box.setAlignment(Pos.CENTER_LEFT);
        TextField linkOrLuogoField = new TextField();
        linkOrLuogoField.setPrefWidth(200);
        linkOrLuogoField.setMaxWidth(200);
        Label errorLinkOrLuogoLabel = new Label();
        Label metaLinkOrLuogo = new Label();

       /*
        if(sessione instanceof SessionePresenza){
            metaLinkOrLuogo.setText("Luogo *");
            linkOrLuogoField.setPromptText("Inserisci luogo");
        }else{
            metaLinkOrLuogo.setText("Luogo *");
            linkOrLuogoField.setPromptText("Inserisci luogo");
        }
        */
        if(corso.getModalita_corso().getLabel().equals("Online")){
            metaLinkOrLuogo.setText("Link *");
            linkOrLuogoField.setPromptText("Inserisci Link");
        }else if(corso.getModalita_corso().getLabel().equalsIgnoreCase("presenza")){
            metaLinkOrLuogo.setText("Luogo *");
            linkOrLuogoField.setPromptText("Inserisci luogo");
        }else{
            box.getChildren().addAll(createChooseSessioneBox(metaLinkOrLuogo,linkOrLuogoField));
        }
        Region spacer = new Region();
        spacer.setPrefHeight(25);
        box.getChildren().addAll(spacer,metaLinkOrLuogo,linkOrLuogoField);
        return box;
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

        box.getChildren().addAll(presenzaBtn,onlineBtn);
        return box;
    }


    public void setCorso(Corso corso){
        this.corso = corso;
    }
    public void setSessione(Sessione sessione){
        this.sessione = sessione;
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
    private void setNotClickedButtonAesthetic(ToggleButton button){
        String base = "-fx-background-color:white;-fx-text-fill:#3a6698;-fx-border-color:#3a6698;" +
                "-fx-border-width:1.5px;-fx-border-radius:7;-fx-background-radius:7;-fx-cursor:hand;";

        button.setStyle(base);
    }

    private void setClickedButtonAesthetic(ToggleButton button){
        String selected  = "-fx-background-color:#3a6698;-fx-text-fill:white;-fx-border-color:#3a6698;" +
                "-fx-border-width:1.5px;-fx-border-radius:7;-fx-background-radius:7;-fx-cursor:hand;";

        button.setStyle(selected);
    }

}
