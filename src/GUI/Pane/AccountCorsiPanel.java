package GUI.Pane;

import Entity.*;
import Controller.Controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.util.ArrayList;

public class AccountCorsiPanel extends BorderPane {

    private ScrollPane scrollPane;
    private VBox corsiBox;
    private HBox bottomBar;
    private Utente utente;
    private Controller controller;

    public AccountCorsiPanel(Controller controller) {
        this.controller = controller;

        corsiBox = new VBox(10);
        corsiBox.setPadding(new Insets(10, 60, 10, 60));
        corsiBox.setSpacing(20); // simile a Vgap

        ScrollPane scrollPane = new ScrollPane(corsiBox);
        scrollPane.setPadding(new Insets(10));
        scrollPane.setBackground(null);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);

        this.setCenter(scrollPane);

        this.setVisible(false);
        this.setManaged(false);
    }

    public void initPanel(Utente utente){
        this.utente = utente;
        ArrayList<Corso> corsiUtente = utente.getCorsi();
        if (!corsiUtente.isEmpty()) {
            for (Corso corso : corsiUtente) {
                ElencoCorsiPanel corsoPan = new ElencoCorsiPanel(controller);
                corsoPan.setCorso(corso);
                corsiBox.getChildren().add(corsoPan);
            }
        } else {
            Label noCorsiLabel = new Label();
            noCorsiLabel.setText("Non sei iscritto a nessun corso");
            noCorsiLabel.setStyle("-fx-text-fill: red;");
            noCorsiLabel.setPadding(new Insets(10, 10, 10, 10));
            noCorsiLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

            corsiBox.getChildren().add(noCorsiLabel);
        }

        bottomBar = createBottomBar(utente);
        this.setBottom(bottomBar);
        BorderPane.setMargin(bottomBar, new Insets(10));

    }


    private HBox createBottomBar(Utente utente) {
        HBox bottomBar = new HBox(10);
        bottomBar.setPadding(new Insets(10));
        bottomBar.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(1, 0, 0, 0)
        )));
        bottomBar.setAlignment(Pos.CENTER);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        if (utente instanceof Studente) {
            Button iscrivitiButton = createIscrivitiButton();
            iscrivitiButton.setOnAction(e -> {
                this.setVisible(false); // come chiudo la finestra qui??????
                this.setManaged(false);
            });
            bottomBar.getChildren().add(iscrivitiButton);
        } else if (utente instanceof Chef) {
            Button aggiungiButton = createAggiungiButton();
            bottomBar.getChildren().add(aggiungiButton);
        }
        return bottomBar;
    }

    private Button createIscrivitiButton() {
        Button iscrivitiButton = new Button("Iscriviti ad un corso");
        iscrivitiButton.setStyle("-fx-background-color: #3A6698; -fx-text-fill: WHITE;");
        this.setFocusPropreties(iscrivitiButton);
        this.setOnMouseTraverse(iscrivitiButton);
        iscrivitiButton.setOnAction(e -> {

        });

        return iscrivitiButton;
    }

    private Button createAggiungiButton() {
        Button aggiungiButton = new Button("Aggiungi");
        aggiungiButton.setStyle("-fx-background-color: #3A6698; -fx-text-fill: WHITE;");
        this.setOnMouseTraverse(aggiungiButton);
        this.setFocusPropreties(aggiungiButton);
        return aggiungiButton;
    }

    private void setOnMouseTraverse(Button button) {
        button.setOnMouseEntered(e -> {
                    button.setStyle("-fx-background-color: WHITE;-fx-text-fill: \"#3A6698\";");
                    button.setBorder(new Border(new BorderStroke(
                            Color.valueOf("#3A6698"),
                            BorderStrokeStyle.SOLID,
                            CornerRadii.EMPTY,
                            new BorderWidths(0, 0, 1, 0)
                    )));
                }
        );
        button.setOnMouseExited(e -> {
                    button.setStyle("-fx-background-color: \"#3A6698\";-fx-text-fill: WHITE;");
                }
        );
    }

    private void setFocusPropreties(Button button) {
        button.setFocusTraversable(true);
        button.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                button.setStyle("-fx-background-color: WHITE;-fx-text-fill: \"#3A6698\";");
                button.setBorder(new Border(new BorderStroke(
                        Color.valueOf("#3A6698"),
                        BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        new BorderWidths(0, 0, 1, 0)
                )));
            } else {
                button.setStyle("-fx-background-color: \"#3A6698\";-fx-text-fill: WHITE;");
            }

        });

    }
}
