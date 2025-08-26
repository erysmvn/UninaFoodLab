package GUI.Stages;

import Controller.Controller;
import DAO.ChefDAO;
import DAO.CorsoDAO;
import Entity.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class SessionePage extends Stage {
    private Controller controller;
    private Sessione sessione;

    private VBox root;
    private VBox infoBox;
    private HBox topHbox;
    private VBox footerVbox;

    public SessionePage(Controller controller) {
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
        topHbox.setPadding(new Insets(50, 0, 10, 0));
        topHbox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        infoBox = new VBox(10);
        infoBox.setAlignment(Pos.TOP_LEFT);
        infoBox.setPadding(new Insets(0,0,0,30));

        footerVbox = new VBox(15);
        footerVbox.setAlignment(Pos.BOTTOM_CENTER);
        footerVbox.setSpacing(20);
        footerVbox.setPadding(new Insets(0, 0, 50, 0));
        footerVbox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
/*
        Rectangle clip = new Rectangle();
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        root.setClip(clip);
        root.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            clip.setWidth(newBounds.getWidth());
            clip.setHeight(newBounds.getHeight());
        });

        Platform.runLater(clip::requestFocus);
*/

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(topHbox, infoBox, spacer, footerVbox);

        Scene scene = new Scene(root, 800, 600);
        scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
    }

    public void initPage(Sessione sessione) {
        this.sessione = sessione;

        Corso corso = sessione.getCorso();
        ArrayList<Chef> chefs = corso.getChefs();

        topHbox.getChildren().clear();
        infoBox.getChildren().clear();
        footerVbox.getChildren().clear();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Data
        Label titoloLabel = new Label(corso.getNome() +" - "+ sessione.getData().format(dateFormatter));
        titoloLabel.setFont(Font.font("System", FontWeight.BOLD, 36));
        titoloLabel.setTextFill(Color.valueOf("#3A6698"));
        topHbox.getChildren().add(titoloLabel);

        // Orario
        Text orarioMeta = new Text("Orario: ");
        orarioMeta.setFont(Font.font("System", FontWeight.BOLD, 18));
        Text orarioVal = new Text(sessione.getOra().format(timeFormatter));
        orarioVal.setFont(Font.font(18));
        infoBox.getChildren().add(new TextFlow(orarioMeta, orarioVal));

// Durata
        Text durataMeta = new Text("Durata: ");
        durataMeta.setFont(Font.font("System", FontWeight.BOLD, 18));
        Text durataVal = new Text(sessione.getDurata() + " ore");
        durataVal.setFont(Font.font(18));
        infoBox.getChildren().add(new TextFlow(durataMeta, durataVal));

// Difficoltà
        Text diffMeta = new Text("Difficoltà: ");
        diffMeta.setFont(Font.font("System", FontWeight.BOLD, 18));
        Text diffVal = new Text(corso.getDifficolta().toString());
        diffVal.setFont(Font.font(18));
        infoBox.getChildren().add(new TextFlow(diffMeta, diffVal));

// Modalità e info
        if (sessione instanceof SessionePresenza sp) {
            Text luogoMeta = new Text("Luogo: ");
            luogoMeta.setFont(Font.font("System", FontWeight.BOLD, 18));
            Text luogoVal = new Text(sp.getLuogo());
            luogoVal.setFont(Font.font(18));
            infoBox.getChildren().add(new TextFlow(luogoMeta, luogoVal));
        } else if (sessione instanceof SessioneOnline so) {
            Text linkMeta = new Text("Link incontro: ");
            linkMeta.setFont(Font.font("System", FontWeight.BOLD, 18));
            Hyperlink linkVal = new Hyperlink(so.getLinkIncontro());
            linkVal.setFont(Font.font(18));
            linkVal.setTextFill(Color.BLUE);
            infoBox.getChildren().add(new TextFlow(linkMeta, linkVal));
        }


        for(Chef ch: chefs){
            Label chefLabel = new Label("Chef: " + ch.getNome()+" "+ ch.getCognome());
            chefLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
            infoBox.getChildren().add(chefLabel);
       }

        Region spacer = new Region();
        spacer.setPrefHeight(15);
        // Ricette trattate
        Label ricetteLabel = new Label("Ricette trattate:");
        ricetteLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        infoBox.getChildren().addAll(spacer,ricetteLabel);

        for (Ricetta r : sessione.getRicette()) {

            Label rLabel = new Label("\u2022 " + r.getNome());
            rLabel.setFont(Font.font(17));
            rLabel.setTextFill(Color.valueOf("#000000"));
            rLabel.setCursor(Cursor.HAND);
            rLabel.setOnMouseClicked(e -> controller.openRicettaPage(r));
            infoBox.getChildren().add(rLabel);
        }

        // Pulsante chiudi
        Button closeButton = new Button("Chiudi");
        closeButton.setPrefSize(100, 30);
        styleButton(closeButton, Color.valueOf("#da3d26"));
        closeButton.setOnAction(e -> this.close());
        footerVbox.getChildren().add(closeButton);
    }

    private void styleButton(Button button, Color color) {
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setBackground(new Background(new BackgroundFill(color, new CornerRadii(8), Insets.EMPTY)));
        button.setCursor(Cursor.HAND);

        button.setOnMouseEntered(e -> button.setOpacity(0.8));
        button.setOnMouseExited(e -> button.setOpacity(1.0));
    }
}
