package GUI.Stages;

import Controller.Controller;
import Entity.*;
import Entity.Enum.Difficolta;
import Exception.CorsoExceptions.CreateCorsoException.AddChefToNewCorsoException.*;
import Exception.CorsoExceptions.CreateCorsoException.createCorsoErrorException;
import Exception.CorsoExceptions.CreateCorsoException.nameAlreadyTakenException;
import Exception.CorsoExceptions.CreateCorsoException.nameCorsoNotFoundException;
import Exception.CorsoExceptions.CreateCorsoException.priceCorsoNotFoundException;
import Exception.CorsoExceptions.imageNotFoundException;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.InputStream;
import java.util.ArrayList;

public class EditCorsoPage extends Stage {

    private VBox vbox;
    private HBox topHbox;
    private HBox bottomHbox;
    private VBox footerVbox;
    private Rectangle clip;
    private Controller controller;
    private Corso corso;

    private TextField nomeField;
    private ComboBox<String> difficoltaBox;
    private Spinner<Integer> freqSettimanaleSpinner;
    private TextField costoField;

    private Label nameError;
    private Label priceError;

    ArrayList<Chef> chefDelCorso;

    TextField nameChef;
    TextField surnameChef;
    TextField emailChef;

    Label nameChefError;
    Label surnameChefError;
    Label emailChefError;

    VBox listaChef;


    public EditCorsoPage(Controller controller){
        this.controller = controller;

        this.initStyle(StageStyle.TRANSPARENT);

        vbox = new VBox(25);
        vbox.setPadding(new Insets(25));
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(30), Insets.EMPTY)));
        vbox.setBorder(new Border(new BorderStroke(Color.valueOf("#3A6698"), BorderStrokeStyle.SOLID, new CornerRadii(30), new BorderWidths(2))));

        topHbox = new HBox(15);
        topHbox.setPadding(new Insets(50, 0, 10, 0));
        topHbox.setAlignment(Pos.TOP_CENTER);
        topHbox.setSpacing(40);

        bottomHbox = new HBox(15);
        bottomHbox.setAlignment(Pos.TOP_LEFT);

        footerVbox = new VBox(15);
        footerVbox.setAlignment(Pos.BOTTOM_CENTER);
        footerVbox.setSpacing(20);

        clip = new Rectangle();
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        vbox.setClip(clip);
        vbox.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            clip.setWidth(newBounds.getWidth());
            clip.setHeight(newBounds.getHeight());
        });

        moveFocusToNotShowBottonClicked();

        Region spacer = new Region();
        Region bottomspacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox.setVgrow(bottomspacer, Priority.ALWAYS);

        vbox.getChildren().addAll(topHbox, bottomHbox, spacer, footerVbox, bottomspacer);

        Scene scene = new Scene(vbox, 900, 750);
        scene.setFill(Color.TRANSPARENT);


        scene.getStylesheets().add(
                getClass().getResource("/Media/StyleSheets/fieldsAndBoxesStyle.css").toExternalForm()
        );

        this.initStyle(StageStyle.TRANSPARENT);
        this.setScene(scene);

        this.setScene(scene);
    }

    private void moveFocusToNotShowBottonClicked(){
        Platform.runLater(clip::requestFocus);
    }

    public void initPage(Corso corso){
        this.corso = corso;
        chefDelCorso = controller.getChefsByIdCorso(corso.getIdCorso());

        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.TOP_RIGHT);

        VBox imgBox = new VBox(10);
        infoBox.setAlignment(Pos.TOP_LEFT);

        addImageCorso(corso.getImagePath(), imgBox);

        this.buildInfoBox(infoBox);

        VBox chefBox = createChefsBox();

        Button saveButton = createSaveButton();
        footerVbox.getChildren().add(saveButton);

        Button closeButton = createCloseButton();
        footerVbox.getChildren().add(closeButton);

        Region spacer1 = new Region();
        Region spacer2 = new Region();
        Region spacer3 = new Region();

        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        HBox.setHgrow(spacer3, Priority.ALWAYS);

        topHbox.getChildren().addAll(spacer1, imgBox, spacer2, infoBox, spacer3);
        bottomHbox.getChildren().add(chefBox);
    }

    public Corso getCorso() {
        return corso;
    }

    private void addImageCorso(String imagePath, VBox imgBox) {
        ImageView imageView;
        Image image;
        try {
            InputStream is = getClass().getResourceAsStream(imagePath);
            if (is == null)
                throw new imageNotFoundException("/Media/Background/biancoNormale.png");
            image = new Image(is);
            imageView = new ImageView(image);
        }catch (imageNotFoundException IMNF) {
            imagePath = IMNF.getMessage();
            image = new Image(imagePath);
            imageView = new ImageView(image);
        }
        setImageShape(imageView,image);
        imgBox.getChildren().add(imageView);
    }

    private void setImageShape(ImageView imageView, Image image) {
        double size = 250;
        double imgWidth = image.getWidth();
        double imgHeight = image.getHeight();
        double x = 0, y = 0, width = imgWidth, height = imgHeight;

        if (imgWidth > imgHeight) {
            width = imgHeight;
            x = (imgWidth - imgHeight) / 2;
        } else if (imgHeight > imgWidth) {
            height = imgWidth;
            y = (imgHeight - imgWidth) / 2;
        }
        imageView.setViewport(new javafx.geometry.Rectangle2D(x, y, width, height));
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        imageView.setPreserveRatio(false);

        Rectangle clip = new Rectangle(size, size);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        imageView.setClip(clip);
    }

    private void buildInfoBox(VBox infoBox) {
        nomeField = new TextField(corso.getNome());
        nomeField.setFont(Font.font(40));
        nomeField.setId("nomeCorsoField");
        nomeField.setPrefWidth(400);
        nameError = new Label("");
        nameError.setTextFill(Color.RED);
        infoBox.getChildren().addAll(nomeField, nameError);

        difficoltaBox = new ComboBox<>();
        difficoltaBox.getItems().addAll("Base", "Intermedio", "Avanzato");
        difficoltaBox.setValue(corso.getDifficolta().toString());
        infoBox.getChildren().add(labeledField("Difficoltà:", difficoltaBox));

        // TODO invece di get frequenza, calcolare la freq massima delle sessioni (algoritmo in doc bdd)
        freqSettimanaleSpinner = new Spinner<>(corso.getFrequenzaSettimanale(), 7, corso.getFrequenzaSettimanale(), 1);
        Label freqLabelValue = new Label();
        freqLabelValue.setFont(Font.font(14));
        freqLabelValue.setText(corso.getFrequenzaSettimanale() == 1 ?
                "lezione a settimana" : "lezioni a settimana");

        freqSettimanaleSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            freqLabelValue.setText(newVal == 1 ?
                    "lezione a settimana" : "lezioni a settimana");
        });

        HBox freqBox = new HBox(10, freqSettimanaleSpinner, freqLabelValue);
        freqBox.setAlignment(Pos.CENTER_LEFT);

        Label freqWarning = new Label("*La frequenza settimanale non può essere minore di quella attuale.");
        freqWarning.setFont(Font.font("System", FontPosture.ITALIC, 13));
        freqWarning.setTextFill(Color.RED);

        VBox freqContainer = new VBox(5, labeledField("Frequenza:", freqBox), freqWarning);
        freqContainer.setAlignment(Pos.CENTER_LEFT);

        infoBox.getChildren().add(freqContainer);

        costoField = new TextField(String.valueOf(corso.getCosto()));
        costoField.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,3}(\\.\\d{0,2})?")) {
                return change;
            }
            return null;
        }));
        priceError = new Label("");
        priceError.setTextFill(Color.RED);
        infoBox.getChildren().addAll(labeledField("Costo:", costoField), priceError);
    }

    private VBox createChefsBox() {
        VBox chefsBox = new VBox(5);
        vbox.setAlignment(Pos.TOP_LEFT);
        chefsBox.setAlignment(Pos.TOP_LEFT);
        Label titolo = new Label("Chef del corso:");
        titolo.setFont(Font.font("System", FontWeight.BOLD, 28));

        HBox titoloBox = new HBox(15);

        listaChef = new VBox(5);

        Button addChefButton = new Button("Aggiungi altro chef");
        styleButton(addChefButton, Color.valueOf("#3A6698"));
        addChefButton.setMinWidth(200);
        addChefButton.setMaxWidth(200);
        addChefButton.setMaxHeight(100);

        addChefButton.setOnAction(e -> {
            addChefToCourse(() -> {
                validateChef();
                aggiornaListaChef();
            });
        });

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        titoloBox.getChildren().addAll(titolo, spacer1, addChefButton);

        ScrollPane scrollPane = new ScrollPane(listaChef);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

//        scrollPane.setStyle("-fx-background: white; -fx-background-color: white;");
//        scrollPane.getViewport().setStyle("-fx-background: white; -fx-background-color: white;");

        scrollPane.setPrefHeight(150);
        scrollPane.setMaxHeight(150);

        chefsBox.getChildren().addAll(titoloBox, scrollPane);

        aggiornaListaChef();

        return chefsBox;
    }

    private void aggiornaListaChef() {
        listaChef.getChildren().clear();
        Chef myChef = (Chef) controller.getUtente();
        for (Chef chef : chefDelCorso) {
            if (chef.getIdchef() != myChef.getIdchef()) {
                HBox riga = new HBox(10);
                Label nome = new Label(chef.getNome() + " " + chef.getCognome());
                nome.setFont(Font.font("System", 18));

                Label removeLabel = new Label("✖");
                removeLabel.setFont(Font.font("System", 18));
                removeLabel.setTextFill(Color.RED);
                removeLabel.setStyle("-fx-font-weight: bold; -fx-cursor: hand;");

                removeLabel.setOnMouseClicked(e -> {
                    chefDelCorso.remove(chef);
                    aggiornaListaChef();
                });

                riga.getChildren().addAll(nome, removeLabel);
                listaChef.getChildren().add(riga);
            } else {
                HBox riga = new HBox(10);
                Label nome = new Label(chef.getNome() + " " + chef.getCognome());
                nome.setFont(Font.font("System", 18));

                riga.getChildren().addAll(nome);
                listaChef.getChildren().add(riga);
            }
        }
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

        Button yesButton = new Button("Conferma");
        Button noButton = new Button("Annulla");

        styleButton(yesButton, Color.valueOf("#3A6698"));
        styleButton(noButton, Color.valueOf("#da3d26"));

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
                    chefDelCorso.add(newChef);
                    aggiornaListaChef();
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

    private void validate() throws createCorsoErrorException {
        if (nomeField.getText().isEmpty()) {
            throw new nameCorsoNotFoundException();
        } else {
            nomeField.setStyle(null);
            nameError.setText("");
        }

        Corso corsoTrovato = controller.getCorsoByNome(nomeField.getText());
        if (corsoTrovato != null && corsoTrovato.getIdCorso() != this.corso.getIdCorso()) {
            throw new nameAlreadyTakenException();
        } else {
            nomeField.setStyle(null);
            nameError.setText("");
        }

        if (costoField.getText().isEmpty()) {
            throw new priceCorsoNotFoundException();
        } else {
            costoField.setStyle(null);
            priceError.setText("");
        }
    }

    private HBox labeledField(String label, javafx.scene.Node field) {
        Label l = new Label(label);
        l.setFont(Font.font(16));
        HBox box = new HBox(10, l, field);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    private Button createSaveButton() {
        Button saveButton = new Button("Salva");
        styleButton(saveButton, Color.valueOf("#3a6698"));
        saveButton.setOnAction(event -> {
            try {
                validate();

                corso.setNome(nomeField.getText());

                float costo = Float.parseFloat(costoField.getText());
                corso.setCosto(costo);
                corso.setDifficolta(Difficolta.valueOf(difficoltaBox.getValue()));
                corso.setFrequenzaSettimanale(freqSettimanaleSpinner.getValue());
                corso.setChefs(chefDelCorso);

                controller.updateCorso(corso);

                controller.refreshCorsi();

                this.close();

            } catch (nameCorsoNotFoundException NCNFE) {
                nomeField.setStyle("-fx-border-color: red;");
                nameError.setText("Inserire nome corso");
            } catch (nameAlreadyTakenException NATE) {
                nomeField.setStyle("-fx-border-color: red;");
                nameError.setText("Corso già esistente");
            } catch (priceCorsoNotFoundException PRCEFNFE) {
                costoField.setStyle("-fx-border-color: red;");
                priceError.setText("Inserire costo corso");
            }
        });
        return saveButton;
    }

    private Button createCloseButton() {
        Button closeButton = new Button("Chiudi");
        styleButton(closeButton, Color.valueOf("#da3d26"));
        closeButton.setOnAction(e -> this.close());
        return closeButton;
    }

    private void styleButton(Button button, Color color) {
        button.setPrefSize(100, 30);
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setBackground(new Background(new BackgroundFill(color, new CornerRadii(8), Insets.EMPTY)));
        button.setCursor(Cursor.HAND);
        button.setOnMouseEntered(e -> button.setOpacity(0.8));
        button.setOnMouseExited(e -> button.setOpacity(1.0));
    }
}