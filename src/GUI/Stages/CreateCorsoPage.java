package GUI.Stages;

import Controller.Controller;
import Entity.Corso;
import Entity.TipologiaCorso;
import GUI.Buttons.CircleButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class CreateCorsoPage extends Stage {
    private Controller controller;
    private Corso corso;
    private ArrayList<TipologiaCorso> tipologie;

    VBox root;
    HBox functionalityButtons;
    HBox container;
    VBox fieldsBox;
    VBox uploadPhotoBox;
    VBox confermaButtonBox;

    TextField corsoName;
    TextField corsoPrice;

    ChoiceBox<String> corsoType;
    ChoiceBox<String> corsoDifficulty;
    ChoiceBox<String> corsoFrequency;

    Label nameError;
    Label priceError;
    Label typeError;
    Label difficultyError;
    Label frequencyError;


    public CreateCorsoPage(Controller controller) {
        this.controller = controller;
        root = new VBox(15);
        setRootStyle();

        Scene scene = new Scene(root, 850, 650);
        scene.setFill(Color.TRANSPARENT);
        scene.setOnKeyPressed(e->{
            if(e.isControlDown() && e.getCode()== KeyCode.W){
                this.close();
            }
        });

        this.initStyle(StageStyle.TRANSPARENT);
        this.setScene(scene);
    }

    private void setRootStyle(){
        root.setPadding(new Insets(20, 50, 50, 50));
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

        createFunctionalityButtonsBox();
        createContainerBox();
        createConfermaButtonsBox();
        root.getChildren().addAll(functionalityButtons, container, confermaButtonBox);
    }

    private void createContainerBox() {
        container = new HBox(15);
        container.setAlignment(Pos.CENTER);
        container.setSpacing(10);
        createFieldsBox();
        createUploadPhotoBox();
        container.getChildren().addAll(fieldsBox, uploadPhotoBox);
    }

    private void createFieldsBox(){
        fieldsBox = new VBox(15);

        fieldsBox.getChildren().addAll(createNomeBox(), createPriceBox(), createTypeBox(), createDifficoltaBox(), createFreqBox());
    }

    private VBox createNomeBox() {
        Label nameLabel = new Label("Nome corso: *");
        corsoName = new TextField();
        corsoName.setPromptText("Nome corso");
        nameError = new Label("");
        nameError.setTextFill(Color.RED);
        return new VBox(5, nameLabel, corsoName, nameError);
    }

    private VBox createPriceBox() {
        Label priceLabel = new Label("Costo: *");
        corsoPrice = new TextField();
        corsoPrice.setPromptText("Nome corso");
        priceError = new Label("");
        priceError.setTextFill(Color.RED);
        return new VBox(5, priceLabel, corsoPrice, priceError);
    }

    private VBox createDifficoltaBox() {
        Label difficultyLabel = new Label("Difficoltà: *");
        corsoDifficulty = new ChoiceBox<>();
        corsoDifficulty.getItems().addAll("Seleziona difficoltà", "Base", "Intermedio", "Avanzato");
        corsoDifficulty.setValue("Seleziona difficoltà");
        difficultyError = new Label("");
        difficultyError.setTextFill(Color.RED);
        return new VBox(5, difficultyLabel, corsoDifficulty, difficultyError);
    }

    private VBox createTypeBox() {
        Label typeLabel = new Label("Tipologia: *");
        corsoType = new ChoiceBox<>();
        corsoType.getItems().add("Seleziona tipologia");
        corsoType.setValue("Seleziona tipologia");
        tipologie = controller.getAllTipologie();
        for (TipologiaCorso t : tipologie) {
            corsoType.getItems().add(t.getNome());
        }
        corsoType.getItems().add("Nuova tipologia");
        typeError = new Label("");
        typeError.setTextFill(Color.RED);
        return new VBox(5, typeLabel, corsoType, typeError);
    }

    private VBox createFreqBox() {
        Label frequencyLabel = new Label("Frequenza settimanale: *");
        corsoFrequency = new ChoiceBox<>();
        corsoFrequency.getItems().addAll("Seleziona frequenza",
                "1 a settimana", "2 a settimana", "3 a settimana", "4 a settimana", "5 a settimana", "6 a settimana", "7 a settimana");
        corsoFrequency.setValue("Seleziona frequenza");
        frequencyError = new Label("");
        frequencyError.setTextFill(Color.RED);
        return new VBox(5, frequencyLabel, corsoFrequency, frequencyError);
    }

    private void createUploadPhotoBox() {
        uploadPhotoBox = new VBox(15);
        uploadPhotoBox.getChildren().add(createUploadPhotoButtonBox());
    }

    private VBox createUploadPhotoButtonBox() {
        VBox photobox = new VBox(10);
        photobox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        Button uploadButton = new Button("Carica Foto");
        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleziona un'immagine");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg")
            );

            File file = fileChooser.showOpenDialog(this);
            if (file != null) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
            }
        });

        photobox.getChildren().addAll(uploadButton, imageView);
        return photobox;
    }

    private void createFunctionalityButtonsBox() {
        functionalityButtons = new HBox(15);
        functionalityButtons.setAlignment(Pos.TOP_RIGHT);
        functionalityButtons.setSpacing(5);
        functionalityButtons.getChildren().addAll(createMinimizeButton(), createCloseButton());
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

    private Button createConfermaButton() {
        Button confermaButton = new Button();
        confermaButton.setText("Conferma");
        confermaButton.setOnAction(e -> {
           // TODO add nel DB
        });
        styleButton(confermaButton, Color.valueOf("#3A6698"));
        return confermaButton;
    }

    private void createConfermaButtonsBox() {
        confermaButtonBox = new VBox(15);
        confermaButtonBox.setAlignment(Pos.BOTTOM_CENTER);
        confermaButtonBox.setSpacing(5);
        confermaButtonBox.getChildren().add(createConfermaButton());
    }

    private void styleButton(Button button, Color color) {
        button.setPrefSize(80, 20);
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setBackground(new Background(new BackgroundFill(color, new CornerRadii(8), Insets.EMPTY)));
        button.setCursor(Cursor.HAND);

        button.setOnMouseEntered(e -> button.setOpacity(0.8));
        button.setOnMouseExited(e -> button.setOpacity(1.0));
    }
}
