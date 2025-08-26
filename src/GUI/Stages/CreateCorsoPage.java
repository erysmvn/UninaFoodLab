package GUI.Stages;

import Controller.Controller;
import Entity.Chef;
import Entity.Corso;
import Entity.TipologiaCorso;
import Entity.Utente;
import Exception.CorsoExceptions.CreateCorsoException.*;
import GUI.Buttons.CircleButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private Chef chef;
    private ArrayList<Chef> chefs;
    private ArrayList<TipologiaCorso> tipologie;

    VBox root;
    HBox functionalityButtons;
    HBox container;
    VBox fieldsBox;
    VBox uploadPhotoBox;
    VBox confermaButtonBox;

    TextField corsoName;
    TextField corsoPrice;

    ChoiceBox<String> corsoDifficulty;
    ChoiceBox<String> corsoFrequency;

    ComboBox<String> corsoType;

    ListView<String>  corsoChefsList;

    Label nameError;
    Label priceError;
    Label typeError;
    Label difficultyError;
    Label frequencyError;
    Label chefsError;


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

    public void getChef(Utente utente){
        if (utente instanceof Chef) {
            this.chef = (Chef) utente;
        } else {
            // TODO exception
        }
    }

    private void setRootStyle(){
        root.setPadding(new Insets(20, 20, 20, 20));
        root.setSpacing(40);
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

        fieldsBox.getChildren().addAll(createNomeBox(), createPriceBox(), createTypeBox(),  createFreqBox(), createDifficoltaBox());
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
        corsoPrice.setPromptText("Costo corso");
        corsoPrice.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));

        Label euroLabel = new Label("€");
        euroLabel.setStyle("-fx-font-weight: bold; -fx-padding: 0 0 0 5;");

        HBox priceField = new HBox(corsoPrice, euroLabel);
        priceField.setSpacing(5);

        priceError = new Label("");
        priceError.setTextFill(Color.RED);
        return new VBox(5, priceLabel, priceField, priceError);
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
        corsoType = new ComboBox<>();
        corsoType.getItems().add("Seleziona tipologia");
        corsoType.setValue("Seleziona tipologia");
        tipologie = controller.getAllTipologie();
        for (TipologiaCorso t : tipologie) {
            corsoType.getItems().add(t.getNome());
        }
        corsoType.getItems().add("Nuova tipologia");
        corsoType.setOnAction(e -> {
            if ("Nuova tipologia".equals(corsoType.getValue())) {
                corsoType.setEditable(true);
                corsoType.getEditor().clear();
                corsoType.getEditor().requestFocus();
            } else {
                corsoType.setEditable(false);
            }
        });

        typeError = new Label("");
        typeError.setTextFill(Color.RED);
        return new VBox(5, typeLabel, corsoType, typeError);
    }

    private VBox createChefsBox() {
        Label chefsLabel = new Label("Chefs: ");
        ArrayList<Chef> chefs = controller.getAllChefs();
        corsoChefsList = new ListView<>();
        for (Chef chef : chefs) {
            corsoChefsList.getItems().add(chef.getNome() + " " + chef.getCognome());
        }
        corsoChefsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        chefsError = new Label("");
        chefsError.setTextFill(Color.RED);

        return new VBox(5, chefsLabel, corsoChefsList, chefsError);
    }

    private VBox createFreqBox() {
        Label frequencyLabel = new Label("Frequenza settimanale: *");
        corsoFrequency = new ChoiceBox<>();
        corsoFrequency.getItems().addAll("Seleziona frequenza",
                "1 sessione a settimana", "2 sessioni a settimana", "3 sessioni a settimana", "4 sessioni a settimana", "5 sessioni a settimana",
                "6 sessioni a settimana", "7 sessioni a settimana");
        corsoFrequency.setValue("Seleziona frequenza");
        frequencyError = new Label("");
        frequencyError.setTextFill(Color.RED);
        return new VBox(5, frequencyLabel, corsoFrequency, frequencyError);
    }

    private void createUploadPhotoBox() {
        uploadPhotoBox = new VBox(15);
        uploadPhotoBox.getChildren().addAll(createUploadPhotoButtonBox(), createChefsBox());
    }

    private VBox createUploadPhotoButtonBox() {
        VBox photobox = new VBox(10);
        photobox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        StackPane photoPane = new StackPane();
        photoPane.setPrefSize(200, 200);
        photoPane.setStyle(
                "-fx-border-color: gray; " +
                        "-fx-border-width: 2; " +
                        "-fx-border-radius: 20; " +
                        "-fx-background-color: #f0f0f0; " +
                        "-fx-background-radius: 20;"
        );
        photoPane.getChildren().add(imageView);

        Label uploadLabel = new Label("Clicca per caricare foto");
        uploadLabel.setStyle("-fx-text-fill: #666;");
        photoPane.getChildren().add(uploadLabel);

        photoPane.setOnMouseClicked(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Seleziona un'immagine");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Immagini", "*.png", "*.jpg", "*.jpeg")
            );

            File file = fileChooser.showOpenDialog(this);
            if (file != null) {
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
                uploadLabel.setVisible(false); // nasconde la label quando c'è l'immagine
            }
        });

        photobox.getChildren().add(photoPane);
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
           try {
               validate();
           } catch (priceCorsoNotFoundException PCNF) {
               corsoPrice.setStyle("-fx-border-color: red;");
               priceError.setText("Inserire il costo del corso");
           } catch (nameCorsoNotFoundException NCNFE) {
               corsoName.setStyle("-fx-border-color: red;");
               nameError.setText("Inserire il nome del corso");
           } catch (typeCorsoNotFoundException TCCFE) {
               corsoType.setStyle("-fx-border-color: red;");
               typeError.setText("Inserire una tipologia del corso");
           } catch (frequencyCorsoNotFoundException FCCFE) {
               corsoFrequency.setStyle("-fx-border-color: red;");
               frequencyError.setText("Inserire una frequenza per il corso");
           } catch (difficultyCorsoNotFoundException DCCFE) {
               corsoDifficulty.setStyle("-fx-border-color: red;");
               difficultyError.setText("Inserire la difficoltà del corso");
           } catch (createCorsoErrorException CRCE) {
               corsoPrice.setStyle("-fx-border-color: red;");
               priceError.setText("Inserire il costo del corso");

               corsoName.setStyle("-fx-border-color: red;");
               nameError.setText("Inserire il nome del corso");

               corsoType.setStyle("-fx-border-color: red;");
               typeError.setText("Inserire una tipologia del corso");

               corsoFrequency.setStyle("-fx-border-color: red;");
               frequencyError.setText("Inserire una frequenza per il corso");

               corsoDifficulty.setStyle("-fx-border-color: red;");
               difficultyError.setText("Inserire la difficoltà del corso");
           }
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

    private void validate() throws createCorsoErrorException {
        if (corsoPrice.getText().isEmpty()) {
            throw new priceCorsoNotFoundException();
        } else {
            corsoPrice.setStyle(null);
            priceError.setText("");
        }

        if (corsoName.getText().isEmpty()) {
            throw new nameCorsoNotFoundException();
        } else {
            corsoName.setStyle(null);
            nameError.setText("");
        }

        if (corsoType.getValue().equals("Seleziona tipologia")) { // or getSelectionModel().getSelectedItem()
            throw new typeCorsoNotFoundException();
        } else {
            corsoType.setStyle(null);
            typeError.setText("");
        }

        if (corsoFrequency.getValue().equals("Seleziona frequenza")) {
            throw new frequencyCorsoNotFoundException();
        } else {
            corsoFrequency.setStyle(null);
            frequencyError.setText("");
        }

        if (corsoDifficulty.getValue().equals("Seleziona difficoltà")) {
            throw new difficultyCorsoNotFoundException();
        } else {
            corsoDifficulty.setStyle(null);
            difficultyError.setText("");
        }

        if (corsoName.getText().isEmpty() && corsoPrice.getText().isEmpty()
                && corsoType.getValue().equals("Seleziona tipologia")
                && corsoFrequency.getValue().equals("Seleziona frequenza") && corsoDifficulty.getValue().equals("Seleziona difficoltà")) {
            throw new createCorsoErrorException();
        }
    }
}
