package GUI.Stages.CreateStages;

import Controller.Controller;
import Entity.Chef;
import Entity.Corso;
import Entity.TipologiaCorso;
import Exception.CorsoExceptions.CreateCorsoException.*;
import Exception.CorsoExceptions.CreateCorsoException.AddChefToNewCorsoException.*;
import GUI.Buttons.CircleButton;
import GUI.Buttons.MyButton;
import GUI.Stages.MyStage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;

public class CreateCorsoPage extends MyStage {
    private Controller controller;
    private Chef chef;
    private ArrayList<TipologiaCorso> tipologie;
    private File selectedPhotoFile;

    VBox root;
    VBox fieldsBox;
    VBox uploadPhotoBox;
    VBox confermaButtonBox;
    HBox functionalityButtons;
    HBox titleBox;
    HBox container;

    TextField corsoName;
    TextField corsoPrice;
    TextField nameChef;
    TextField surnameChef;
    TextField emailChef;

    ChoiceBox<String> corsoDifficulty;
    ChoiceBox<String> corsoFrequency;

    ComboBox<String> corsoType;

    ArrayList<Chef> chefAggiunti;

    Label nameError;
    Label priceError;
    Label typeError;
    Label difficultyError;
    Label frequencyError;
    Label nameChefError;
    Label surnameChefError;
    Label emailChefError;


    public CreateCorsoPage(Controller controller, Chef chef) {
        super(850, 650, RootType.VBOX);
        this.chef = chef;
        chefAggiunti = new ArrayList<>();
        chefAggiunti.add(this.chef); //TODO CON IL CONTROLLER (CONTROLLER HA L'UTENTE LOGGATO

        this.controller = controller;
        root = getRootVBox();
        setRootElements();

        this.addStylesheet("/Media/StyleSheets/fieldsAndBoxesStyle.css");
    }

    private void setRootElements(){
        createFunctionalityButtonsBox();
        createTitleBox();
        createContainerBox();
        createConfermaButtonsBox();

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Region bottomSpacer = new Region();
        VBox.setVgrow(bottomSpacer, Priority.ALWAYS);

        Region topSpacer = new Region();
        VBox.setVgrow(topSpacer, Priority.ALWAYS);

        root.getChildren().addAll(functionalityButtons, titleBox, topSpacer, container, spacer, confermaButtonBox, bottomSpacer);
    }

    private void createTitleBox() {
        titleBox = new HBox();
        titleBox.setAlignment(Pos.TOP_CENTER);
        Label title = new Label("Aggiungi un nuovo corso !");
        title.setTextAlignment(TextAlignment.CENTER);
        title.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-font-size: 28;" +
                        "-fx-text-fill: #3A6698;"
        );

        titleBox.getChildren().add(title);
    }

    private void createContainerBox() {
        container = new HBox(15);
        container.setAlignment(Pos.CENTER);
        container.setSpacing(0);
        createFieldsBox();
        createUploadPhotoBox();

        Region leftSpacer = new Region();
        Region middleSpacer = new Region();
        Region rightSpacer = new Region();

        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(middleSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        container.getChildren().addAll(leftSpacer, fieldsBox, middleSpacer, uploadPhotoBox, rightSpacer);
    }

    private void createFieldsBox(){
        fieldsBox = new VBox(15);

        fieldsBox.getChildren().addAll(createNomeBox(), createPriceBox(), createFreqBox(), createTypeBox(), createDifficoltaBox());
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
            if (newText.matches("\\d{0,3}(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));

        Label euroLabel = new Label("€");
        euroLabel.setStyle("-fx-font-weight: bold; -fx-padding: 4 0 0 5;");

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
        corsoType.getItems().add("Nuova tipologia");
        try {
            tipologie = controller.getAllTipologie();
        } catch (SQLException e) {
            showDialog("Errore di sistema. Riprovare più tardi");
        }
        for (TipologiaCorso t : tipologie) {
            corsoType.getItems().add(t.getNome());
        }
        corsoType.setOnAction(e -> {
            if ("Nuova tipologia".equals(corsoType.getValue())) {
                corsoType.setEditable(true);
                corsoType.getEditor().setText("");
                corsoType.getEditor().requestFocus();
            }
        });
        typeError = new Label("");
        typeError.setTextFill(Color.RED);
        return new VBox(5, typeLabel, corsoType, typeError);
    }

    private VBox createChefsBox() {
        Label chefsCount = new Label("");
        MyButton addChefButton = new MyButton("Aggiungi altro chef", MyButton.ButtonType.PRIMARY);
        addChefButton.setSize(200, 40);

        addChefButton.setOnAction(e -> {
            addChefToCourse( () -> {
                validateChef();
                updateChefsCount(chefsCount);
            });
        });

        return new VBox(5, chefsCount, addChefButton);
    }

    private void updateChefsCount(Label chefsCount) {
        chefsCount.setText("Chef inseriti: " + chefAggiunti.size());
    }

    private void addChefToCourse( Runnable onConfirm) {
        Stage addChefToCourseStage = new Stage();

        addChefToCourseStage.initModality(Modality.APPLICATION_MODAL);
        addChefToCourseStage.initStyle(StageStyle.TRANSPARENT);

        Label titolo = new Label("Inserire credenziali");
        titolo.setStyle("-fx-font-weight: bold; -fx-padding: 10;" +
                "-fx-font-size: 30;" +
                "-fx-text-fill: #3a6698;" +
                "-fx-background-color: transparent;" +
                "-fx-font-family: System;");
        titolo.setAlignment(Pos.TOP_CENTER);

        MyButton yesButton = new MyButton("Conferma", MyButton.ButtonType.PRIMARY);
        MyButton noButton = new MyButton("Annulla", MyButton.ButtonType.SECONDARY);

        VBox buttons = new VBox(20,yesButton, noButton);
        buttons.setAlignment(Pos.CENTER);
        Label error = new Label("");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(30), Insets.EMPTY)));
        root.setBorder(new Border(new BorderStroke(Color.valueOf("#3A6698"), BorderStrokeStyle.SOLID, new CornerRadii(30), new BorderWidths(2))));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Region spacer2 = new Region();
        spacer2.setPrefHeight(20);
        root.getChildren().addAll(
                titolo,
                spacer,
                addChefNomeBox(),
                addChefCognomeBox(),
                addChefEmailBox(),
                error,
                spacer2,
                buttons
        );

        Scene scene = new Scene(root, 500, 600);
        scene.setFill(Color.TRANSPARENT);
        addChefToCourseStage.setScene(scene);

        yesButton.setOnAction(e -> {
            try {
                onConfirm.run();
                Chef newChef = controller.getChefDaAggiungereToNuovoCorso(nameChef.getText(), surnameChef.getText(), emailChef.getText());
                if (newChef != null) {
                    chefAggiunti.add(newChef);
                    addChefToCourseStage.close();
                } else {
                    error.setText("Lo chef deve essere registrato \n alla piattaforma");
                    error.setTextFill(Color.RED);
                }
            } catch (chefNameNotFoundException CNNFE) {
                nameChef.setStyle("-fx-border-color: red;");
                nameChefError.setText("Inserire nome chef");
            } catch (chefSurnameNotFoundException CSNFE) {
                surnameChef.setStyle("-fx-border-color: red;");
                surnameChefError.setText("Inserire cognome chef");
            } catch (chefEmailNotFoundException CENFE) {
                emailChef.setStyle("-fx-border-color: red;");
                emailChefError.setText("Inserire email chef");
            } catch (chefEmailNotValidException CENVE) {
                emailChef.setStyle("-fx-border-color: red;");
                emailChefError.setText("Inserire email valida");
            } catch (addChefToNewCorsoException ACTNCE) {
                nameChef.setStyle("-fx-border-color: red;");
                nameChefError.setText("Inserire nome chef");
                surnameChef.setStyle("-fx-border-color: red;");
                surnameChefError.setText("Inserire cognome chef");
                emailChef.setStyle("-fx-border-color: red;");
                emailChefError.setText("Inserire email chef");
            } catch (SQLException sqle) {
                error.setText(sqle.getMessage());
            }
        });

        noButton.setOnAction(e -> addChefToCourseStage.close());

        addChefToCourseStage.showAndWait();
    }

    private VBox addChefNomeBox() {
        Label nameChefLabel = new Label("Nome chef: *");
        nameChef = new TextField();
        nameChef.setPromptText("Nome chef");
        nameChef.setMaxWidth(330);

        nameChefError = new Label("");
        nameChefError.setTextFill(Color.RED);

        VBox nameChefBox = new VBox(5, nameChefLabel, nameChef, nameChefError);
        nameChefBox.setAlignment(Pos.TOP_LEFT);
        nameChefBox.setMaxWidth(330);
        return nameChefBox;
    }

    private VBox addChefCognomeBox() {
        Label surnameChefLabel = new Label("Cognome chef: *");
        surnameChef = new TextField();
        surnameChef.setPromptText("Cognome chef");
        surnameChef.setMaxWidth(330);

        surnameChefError = new Label("");
        surnameChefError.setTextFill(Color.RED);

        VBox surnameBox = new VBox(5, surnameChefLabel, surnameChef, surnameChefError);
        surnameBox.setAlignment(Pos.TOP_LEFT);
        surnameBox.setMaxWidth(330);
        return surnameBox;
    }

    private VBox addChefEmailBox() {
        Label emailChefLabel = new Label("Email chef: *");
        emailChef = new TextField();
        emailChef.setPromptText("Email chef");
        emailChef.setMaxWidth(330);

        emailChefError = new Label("");
        emailChefError.setTextFill(Color.RED);

        VBox emailBox = new VBox(5, emailChefLabel, emailChef, emailChefError);
        emailBox.setAlignment(Pos.TOP_LEFT);
        emailBox.setMaxWidth(330);
        return emailBox;
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

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Region spacer2 = new Region();
        VBox.setVgrow(spacer2, Priority.ALWAYS);

        uploadPhotoBox.getChildren().addAll(createUploadPhotoButtonBox(), spacer, spacer2, createChefsBox());
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
        photoPane.setStyle("-fx-background-color: transparent;" +
                "-fx-border-width: 1;" +
                "-fx-border-color: #3a6698;" +
                "-fx-border-radius: 30");

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
                uploadLabel.setVisible(false);

                selectedPhotoFile = file;
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

    private MyButton createConfermaButton() {
        MyButton confermaButton = new MyButton("Conferma", MyButton.ButtonType.PRIMARY);

        confermaButton.setOnAction(e -> {
           try {
               validateCorso();

               String nomeCorso = corsoName.getText();
               String nameForPath = nomeCorso.replaceAll("\s+", "");

               Path destDir = Paths.get("src/Media/CoursesImages");

               try {
                   if (selectedPhotoFile != null) {
                       Files.copy(selectedPhotoFile.toPath(),
                               destDir.resolve(nameForPath + ".png"),
                               StandardCopyOption.REPLACE_EXISTING);
                   }
               } catch (IOException ignore) {
                   ignore.printStackTrace();
               }

               double price = 0.0;
               if (!corsoPrice.getText().isEmpty()) {
                   price = Double.parseDouble(corsoPrice.getText());
               }

               String frequency = corsoFrequency.getValue();
               frequency = frequency.substring(0, 1);
               int freq = Integer.parseInt(frequency);

               TipologiaCorso tp = controller.getOrAddTipologiaCorso(corsoType.getValue());

               String difficolta = corsoDifficulty.getValue();

               try {
                   Corso newCorso = controller.createNewCorso(nomeCorso, price, freq, difficolta, tp, chefAggiunti);
                   controller.getUtente().getCorsi().add(newCorso);
               } catch (createCorsoErrorException | SQLException CCEE) {
                   showDialog("Errore di sistema. Riprovare più tardi");
               }

               controller.refreshCorsi();

               this.close();

           } catch (nameCorsoNotFoundException NCNFE) {
               corsoName.setStyle("-fx-border-color: red;");
               nameError.setText("Inserire il nome del corso");
           } catch (nameAlreadyTakenException NANE) {
               corsoName.setStyle("-fx-border-color: red;");
               nameError.setText("Corso già esistente");
           } catch (priceCorsoNotFoundException PCNF) {
               corsoPrice.setStyle("-fx-border-color: red;");
               priceError.setText("Inserire il costo del corso");
           } catch (frequencyCorsoNotFoundException FCCFE) {
               corsoFrequency.setStyle("-fx-border-color: red;");
               frequencyError.setText("Inserire una frequenza per il corso");
           } catch (typeCorsoNotFoundException TCCFE) {
               corsoType.setStyle("-fx-border-color: red;");
               typeError.setText("Inserire una tipologia del corso");
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
           } catch (SQLException SQLE) {
               // TODO Dialog
               SQLE.printStackTrace();
           }
        });
        return confermaButton;
    }

    private void createConfermaButtonsBox() {
        confermaButtonBox = new VBox(15);
        confermaButtonBox.setAlignment(Pos.BOTTOM_CENTER);
        confermaButtonBox.setSpacing(20);
        confermaButtonBox.getChildren().add(createConfermaButton());
    }

    private void validateCorso() throws SQLException, createCorsoErrorException {
        if (corsoName.getText().isEmpty() && corsoPrice.getText().isEmpty()
                && corsoType.getValue().equals("Seleziona tipologia")
                && corsoFrequency.getValue().equals("Seleziona frequenza") && corsoDifficulty.getValue().equals("Seleziona difficoltà")) {
            throw new createCorsoErrorException();
        }

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

        if (controller.getCorsoByNome(corsoName.getText()) != null) {
            throw new nameAlreadyTakenException();
        } else {
            corsoName.setStyle(null);
            nameError.setText("");
        }

        if (corsoType.getValue().equals("Seleziona tipologia")) {
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
    }

    private void validateChef() throws addChefToNewCorsoException {
        if (nameChef.getText().isEmpty()
                && surnameChef.getText().isEmpty()
                && emailChef.getText().isEmpty()) {
            throw new addChefToNewCorsoException();
        }

        if (nameChef.getText().isEmpty()) {
            throw new chefNameNotFoundException();
        } else {
            nameChef.setStyle(null);
            nameChefError.setText("");
        }

        if (surnameChef.getText().isEmpty()) {
            throw new chefSurnameNotFoundException();
        } else {
            surnameChef.setStyle(null);
            surnameChefError.setText("");
        }

        if (emailChef.getText().isEmpty()) {
            throw new chefEmailNotFoundException();
        } else if (!emailChef.getText().contains("@") || !emailChef.getText().contains(".") ||
                emailChef.getText().lastIndexOf('.') < emailChef.getText().indexOf('@')) {
            throw new chefEmailNotValidException();
        } else {
            emailChef.setStyle(null);
            emailChefError.setText("");
        }
    }
}
