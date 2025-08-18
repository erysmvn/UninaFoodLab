package GUI.Pane;

import Entity.Utente;
import Entity.Studente;
import Entity.Chef;
import Entity.Corso;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

public class AccountCorsiPanel extends BorderPane {

    private ScrollPane scrollPane;
    private GridPane corsiGrid;
    private HBox bottomBar;

    public AccountCorsiPanel(Utente utente) {

        corsiGrid = new GridPane();
        corsiGrid.setPadding(new Insets(10, 60, 10, 60));
        corsiGrid.setHgap(120);
        corsiGrid.setVgap(20);

        ArrayList<Corso> corsiUtente = utente.getCorsi();
        int col = 0;
        int row = 0;
        if (corsiUtente.size() > 0) {
            for (Corso corso : corsiUtente) {
                CorsoPanel tempCorsoPanel = new CorsoPanel(corso.getImagePath(), corso.getNome());
                corsiGrid.add(tempCorsoPanel, col, row);
                col++;
                if (col >= 2) {
                    col = 0;
                    row++;
                }
            }
        } else {
            Label noCorsiLabel = new Label();
            noCorsiLabel.setText("Non sei iscritto a nessun corso");
            noCorsiLabel.setStyle("-fx-text-fill: red;");
            noCorsiLabel.setPadding(new Insets(10, 10, 10, 10));
            noCorsiLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

            corsiGrid.add(noCorsiLabel, 0, 0);
        }

        scrollPane = new ScrollPane(corsiGrid);
        scrollPane.setPadding(new Insets(10));
        scrollPane.setBackground(null);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToWidth(true);

        this.setCenter(scrollPane);

        bottomBar = createBottomBar(utente);
        this.setBottom(bottomBar);
        BorderPane.setMargin(bottomBar, new Insets(10));

        this.setVisible(false);
        this.setManaged(false);
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
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        if (utente instanceof Studente) {
            Button iscrivitiButton = createIscrivitiButton();
            Button disiscrivitiButton = createDisiscrivitiButton();
            bottomBar.getChildren().addAll(iscrivitiButton,spacer, disiscrivitiButton);
        } else if (utente instanceof Chef) {
            Button aggiungiButton = createAggiungiButton();
            Button eliminaButton = createEliminaButton();
            bottomBar.getChildren().addAll(aggiungiButton,spacer, eliminaButton);
        }

        return bottomBar;
    }

    private Button createIscrivitiButton() {
        Button iscrivitiButton = new Button("Iscriviti");
        iscrivitiButton.setStyle("-fx-background-color: #3A6698; -fx-text-fill: WHITE;");
        this.setFocusPropreties(iscrivitiButton);
        this.setOnMouseTraverse(iscrivitiButton);
        iscrivitiButton.setOnAction(e -> {

        });

        return iscrivitiButton;
    }

    private Button createDisiscrivitiButton() {
        Button disiscrivitiButton = new Button("Disiscriviti");
        disiscrivitiButton.setStyle("-fx-background-color: #3A6698; -fx-text-fill: WHITE;");
        this.setFocusPropreties(disiscrivitiButton);
        this.setOnMouseTraverse(disiscrivitiButton);
        return disiscrivitiButton;
    }

    private Button createAggiungiButton() {
        Button aggiungiButton = new Button("Aggiungi");
        aggiungiButton.setStyle("-fx-background-color: #3A6698; -fx-text-fill: WHITE;");
        this.setOnMouseTraverse(aggiungiButton);
        this.setFocusPropreties(aggiungiButton);
        return aggiungiButton;
    }

    private Button createEliminaButton() {
        Button eliminaButton = new Button("Elimina");
        eliminaButton.setStyle("-fx-background-color: #3A6698; -fx-text-fill: WHITE;");
        this.setFocusPropreties(eliminaButton);
        this.setOnMouseTraverse(eliminaButton);
        return eliminaButton;
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
