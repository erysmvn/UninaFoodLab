package GUI.Stages;

import Controller.Controller;
import Entity.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javafx.scene.shape.Rectangle;
import javafx.stage.StageStyle;

public class CorsoPage extends Stage {

    private VBox vbox;
    Controller controller;

    public CorsoPage(Corso corso, Chef chef, Controller controller) {
        this.controller = controller;

        DecimalFormat df = new DecimalFormat("#.##"); // removes ".0"

        this.initStyle(StageStyle.TRANSPARENT);
        vbox = new VBox();
        vbox.setBackground(new Background(new BackgroundFill(
                Color.WHITE,
                new CornerRadii(30),
                Insets.EMPTY
        )));



        vbox.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(30),
                new BorderWidths(2)
        )));

        vbox.setSpacing(10);
        vbox.setPadding(new Insets(15));
        vbox.setAlignment(Pos.TOP_CENTER);
        Rectangle clip = new Rectangle();
        clip.setWidth(vbox.getWidth());
        clip.setHeight(vbox.getHeight());
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        addImageCorso(corso.getImagePath());

        Label nomeCorso = new Label(corso.getNome());
        nomeCorso.setFont(Font.font("Nimbus Roman",40));
        nomeCorso.setTextFill(Color.valueOf("#3A6698"));
        vbox.getChildren().add(nomeCorso);

        Label nomeChef = new Label("Chef: "+chef.getNome() + " "+chef.getCognome());
        vbox.getChildren().add(nomeChef);

        Label modalita = new Label("Modalità: " + corso.getModalita_corso().getLabel());
        vbox.getChildren().add(modalita);


        Label difficolta = new Label("Difficoltà: " + corso.getDifficolta().toString());
        vbox.getChildren().add(difficolta);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Label date = new Label("Periodo: " + sdf.format(corso.getDataInizio()) + " - " + sdf.format(corso.getDataFine()));
        vbox.getChildren().add(date);

        Label ore = new Label("Ore totali: " + df.format(corso.getOreTotali()) + ", Frequenza: " + corso.getFrequenzaSettimanale() + " lezione a settimana");
        vbox.getChildren().add(ore);

        Label costo = new Label("Costo: " + df.format(corso.getCosto()) + " €");
        vbox.getChildren().add(costo);
        Text descrizione = new Text(corso.getDesc_corso());
        descrizione.setWrappingWidth(400);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(descrizione);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(50);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        scrollPane.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(0.5)
        )));
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        vbox.getChildren().add(scrollPane);

        if (!controller.isHomePageChef()) {
            Button subcribeButton = createSubscribeButton(controller.isAlreadyLoggedIn());
            vbox.getChildren().add(subcribeButton);
            VBox.setMargin(subcribeButton, new Insets(20, 0, 0, 0));
        }

        Button closeButtone = createCloseButton();
        vbox.getChildren().add(closeButtone);
        VBox.setMargin(closeButtone, new Insets(0, 0, 10, 0));

        vbox.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            clip.setWidth(newBounds.getWidth());
            clip.setHeight(newBounds.getHeight());
        });

        vbox.setClip(clip);

        Scene scene = new Scene(vbox, 850, 650);
        scene.setFill(Color.TRANSPARENT);
        clip.requestFocus();
        this.setScene(scene);

        clip.requestFocus();

    }

    public void addImageCorso(String imagePath) {
        ImageView imageView;
        try {
            Image image = new Image(imagePath, 400, 200, true, true);
            imageView = new ImageView(image);
        } catch (Exception e) {
            imageView = new ImageView();
        }

        vbox.getChildren().add(imageView);
    }


    private Button createSubscribeButton(boolean isLoggedIn) {
        Button subscribeButton = new Button();
        subscribeButton.setPrefWidth(100);
        subscribeButton.setPrefHeight(30);
        subscribeButton.setText("Iscriviti");
        Color color = Color.valueOf("#3a6698");
        this.setFocusPropreties(subscribeButton, color);
        this.setOnMouseTraverse(subscribeButton, color);

        subscribeButton.setStyle("-fx-background-color: \"#3a6698\"; -fx-text-fill: white;");
        subscribeButton.setOnAction(event -> {

            //TODO if login true add corso else go login
            if (!isLoggedIn) {
                controller.openLoginPage();
                this.close();
            } else {
                // TODO change iscriviti to iscritto, rendi non cliccabile e non traversable, aggiungi al db
            }

        });
        this.setFocusPropreties(subscribeButton,Color.valueOf("#3a6698"));
        this.setOnMouseTraverse(subscribeButton,Color.valueOf("#3a6698"));

        return subscribeButton;
    }

    private Button createCloseButton() {
        Button closeButton = new Button();
        closeButton.setPrefWidth(100);
        closeButton.setPrefHeight(30);
        closeButton.setText("Chiudi");

        Color color = Color.valueOf("#da3d26");
        this.setFocusPropreties(closeButton, color);
        this.setOnMouseTraverse(closeButton, color);

        closeButton.setStyle("-fx-background-color: \"#da3d26\"; -fx-text-fill: white;");
        closeButton.setOnAction(e -> this.close());
        return closeButton;
    }


  
    private void setOnMouseTraverse(Button button, Color color) {
        button.setOnMouseEntered(e -> {
                    button.setStyle("-fx-background-color: WHITE;-fx-text-fill: \""+ color +"\";");
                    button.setBorder(new Border(new BorderStroke(
                            color,
                            BorderStrokeStyle.SOLID,
                            CornerRadii.EMPTY,
                            new BorderWidths(0, 0, 1, 0)
                    )));
                }
        );
        button.setOnMouseExited(e -> {
                    button.setStyle("-fx-background-color: \""+ color +"\";-fx-text-fill: WHITE;");
                }
        );
    }

    private void setFocusPropreties(Button button, Color color) {
        button.setFocusTraversable(true);
        button.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                button.setStyle("-fx-background-color: WHITE;-fx-text-fill: \""+ color +"\";");
                button.setBorder(new Border(new BorderStroke(
                        color,
                        BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        new BorderWidths(0, 0, 1, 0)
                )));
            } else {
                button.setStyle("-fx-background-color: \""+ color +"\";-fx-text-fill: WHITE;");
            }
        });
    }
}
