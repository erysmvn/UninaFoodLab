package GUI.Pane;

import Entity.*;
import Controller.Controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

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
        corsiBox.setSpacing(20);

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
        showCorsi();

        bottomBar = createBottomBar(utente);
        this.setBottom(bottomBar);
        BorderPane.setMargin(bottomBar, new Insets(10));
    }

    public void showCorsi(){
        corsiBox.getChildren().clear();
        ArrayList<Corso> corsiUtente = utente.getCorsi();
        if (!corsiUtente.isEmpty() && corsiUtente != null) {
            for (Corso corso : corsiUtente) {
                ElencoCorsiPanel corsoPan = new ElencoCorsiPanel(this, controller);
                corsoPan.setCorso(corso);
                corsiBox.getChildren().add(corsoPan);
            }
        } else {
            VBox emptyMessageBox;
            if (utente instanceof Studente studente) {
                emptyMessageBox = createEmptyCoursesMessage(
                        "Non sei iscritto a nessun corso",
                        "/Media/Icons/notFoundIcon.png"
                );
            } else if (utente instanceof Chef chef) {
                emptyMessageBox = createEmptyCoursesMessage(
                        "Non stai tenendo nessun corso",
                        "/Media/Icons/notFoundIcon.png"
                );
            } else {
                emptyMessageBox = createEmptyCoursesMessage(
                        "Nessun corso disponibile",
                        null
                );
            }
            corsiBox.getChildren().add(emptyMessageBox);
        }
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
        this.setButtonPropreties(iscrivitiButton);
        iscrivitiButton.setOnAction(e -> {
            Stage stage = (Stage) this.getScene().getWindow();
            stage.close();
        });

        return iscrivitiButton;
    }

    private Button createAggiungiButton() {
        Button aggiungiButton = new Button("Aggiungi un nuovo corso");
        this.setButtonPropreties(aggiungiButton);

        return aggiungiButton;
    }

    private void setButtonPropreties(Button button) {
        button.setStyle("-fx-background-color: #3A6698; -fx-text-fill: WHITE;");
        button.setPadding(new Insets(10, 10, 10, 10));
        this.setOnMouseTraverse(button);
        this.setFocusPropreties(button);
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

    private VBox createEmptyCoursesMessage(String message, String iconPath) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        Label label = new Label(message);
        label.setTextFill(Color.valueOf("#2F3A42"));
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(50, 10, 10, 10));
        Font robotoFont = Font.loadFont(
                getClass().getResourceAsStream("/Media/Fonts/Roboto.ttf"),
                45
        );
        label.setFont(robotoFont);

        ImageView iconView = new ImageView(iconPath);
        if (iconPath != null) {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath)));
            iconView = new ImageView(icon);
            iconView.setFitWidth(60);
            iconView.setFitHeight(60);
            iconView.setPreserveRatio(true);
        }

        if (iconView != null) {
            box.getChildren().addAll(label, iconView);
        } else {
            box.getChildren().add(label);
        }

        return box;
    }
}
