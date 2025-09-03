package GUI.Stages;

import Controller.Controller;
import Entity.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.awt.*;
import java.net.URI;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import javafx.scene.control.ScrollPane;


public class SessionePage extends Stage {
    private Controller controller;
    private Sessione sessione;

    private BorderPane root;
    private VBox infoBox;
    private HBox topHbox;
    private VBox footerVbox;

    Label confermarePartecipazioneLabel;

    public SessionePage(Controller controller) {
        this.controller = controller;
        this.initStyle(StageStyle.TRANSPARENT);

        // Root BorderPane
        root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(30), Insets.EMPTY)));
        root.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(30),
                new BorderWidths(2)
        )));

        // Top
        topHbox = new HBox(15);
        topHbox.setAlignment(Pos.TOP_CENTER);
        topHbox.setSpacing(20);
        topHbox.setPadding(new Insets(50, 0, 10, 0));
        root.setTop(topHbox);

        // Center (info box)
        infoBox = new VBox(10);
        infoBox.setAlignment(Pos.TOP_LEFT);
        infoBox.setPadding(new Insets(0, 0, 0, 30));
        root.setCenter(infoBox);

        // Footer (bottom)
        footerVbox = new VBox(10);
        footerVbox.setAlignment(Pos.BOTTOM_CENTER);
        footerVbox.setSpacing(20);
        footerVbox.setPadding(new Insets(0, 0, 50, 0));
        root.setBottom(footerVbox);

        // Scene
        Scene scene = new Scene(root, 800, 600);
        scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
    }

    public void initPage(Sessione sessione) {
        this.sessione = sessione;

        Corso corso = sessione.getCorso();

        topHbox.getChildren().clear();
        infoBox.getChildren().clear();
        footerVbox.getChildren().clear();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Label titoloLabel = new Label(corso.getNome() + " - " + sessione.getData().format(dateFormatter));
        titoloLabel.setFont(Font.font("System", FontWeight.BOLD, 36));
        titoloLabel.setTextFill(Color.valueOf("#3A6698"));
        topHbox.getChildren().add(titoloLabel);

        Text orarioMeta = new Text("Orario: ");
        orarioMeta.setFont(Font.font("System", FontWeight.BOLD, 18));
        Text orarioVal = new Text(sessione.getOra().format(timeFormatter));
        orarioVal.setFont(Font.font(18));
        infoBox.getChildren().add(new TextFlow(orarioMeta, orarioVal));

        DecimalFormat df = new DecimalFormat("#.##");

        Text durataMeta = new Text("Durata: ");
        durataMeta.setFont(Font.font("System", FontWeight.BOLD, 18));
        Text durataVal = new Text();
        if (sessione.getDurata() >= 2) {
            durataVal.setText(df.format(sessione.getDurata()) + " ore");
        } else {
            durataVal.setText(df.format(sessione.getDurata()) + " ora");
        }
        durataVal.setFont(Font.font(18));
        infoBox.getChildren().add(new TextFlow(durataMeta, durataVal));

        Text diffMeta = new Text("Difficoltà: ");
        diffMeta.setFont(Font.font("System", FontWeight.BOLD, 18));
        Text diffVal = new Text(corso.getDifficolta().toString());
        diffVal.setFont(Font.font(18));
        infoBox.getChildren().add(new TextFlow(diffMeta, diffVal));

        confermarePartecipazioneLabel = new Label("**Per confermare la partecipazione caricare foglio di adesione");
        confermarePartecipazioneLabel.setStyle("-fx-text-fill: red;-fx-font-size: 15");
        confermarePartecipazioneLabel.setVisible(false);

        if (sessione instanceof SessionePresenza sp) {
            Text luogoMeta = new Text("Luogo: ");
            luogoMeta.setFont(Font.font("System", FontWeight.BOLD, 18));
            Text luogoVal = new Text(sp.getLuogo());
            luogoVal.setFont(Font.font(18));
            infoBox.getChildren().add(new TextFlow(luogoMeta, luogoVal));
            if (controller.isStudent())
                footerVbox.getChildren().add(createPartecipaButton());
            else {
                HBox options = new HBox(10);
                options.setAlignment(Pos.CENTER);
                options.getChildren().addAll(createEditButton(), createDeleteButton());
                footerVbox.getChildren().add(options);
            }
        } else if (sessione instanceof SessioneOnline so) {
            Text linkMeta = new Text("Link incontro: ");
            linkMeta.setFont(Font.font("System", FontWeight.BOLD, 18));
            Hyperlink linkVal = new Hyperlink(so.getLinkIncontro());
            linkVal.setFont(Font.font(18));
            linkVal.setTextFill(Color.BLUE);

            linkVal.setOnAction(e -> {
                try {
                    String rawUrl = so.getLinkIncontro();
                    if (!rawUrl.startsWith("http://") && !rawUrl.startsWith("https://")) {
                        rawUrl = "https://" + rawUrl;
                    }
                    URI uri = new URI(rawUrl);
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(uri);
                    } else {
                        Runtime.getRuntime().exec(new String[]{"open", uri.toString()});
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });

            infoBox.getChildren().add(new TextFlow(linkMeta, linkVal));
        }

        controller.setChefs(corso);

        Text chefLabel = new Text("Chef: ");
        chefLabel.setStyle("-fx-font-weight: bold;");
        chefLabel.setFont(Font.font("System", FontWeight.BOLD, 18));
        Text chefValue = new Text(corso.getStringOfChefs());
        chefValue.setFont(Font.font("System", 18));
        infoBox.getChildren().add(new TextFlow(chefLabel, chefValue));

        infoBox.getChildren().add(confermarePartecipazioneLabel);

        Label ricetteLabel = new Label("Ricette trattate:");
        ricetteLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        infoBox.getChildren().add(ricetteLabel);

        VBox listaRicetteBox = new VBox(10);
        listaRicetteBox.setStyle("-fx-background-color: white ;");
        for (Ricetta r : sessione.getRicette()) {
            Label rLabel = new Label("• " + r.getNome());
            rLabel.setFont(Font.font(17));
            rLabel.setTextFill(Color.BLACK);
            rLabel.setCursor(Cursor.HAND);
            rLabel.setWrapText(true);
            rLabel.setOnMouseClicked(e -> controller.openRicettaPage(r));
            listaRicetteBox.getChildren().add(rLabel);
        }

        ScrollPane listaRicette = new ScrollPane(listaRicetteBox);
        listaRicette.setPrefWidth(250);
        listaRicette.setMaxWidth(250);
        listaRicette.setPrefHeight(120);
        listaRicette.setMinHeight(120);
        listaRicette.setMaxHeight(120);
        listaRicette.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        listaRicette.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        listaRicette.setStyle("-fx-background: white;-fx-background-color: white;");
        VBox.setVgrow(listaRicette, Priority.NEVER);
        infoBox.getChildren().add(listaRicette);

        Button closeButton = new Button("Chiudi");
        closeButton.setPrefSize(250, 30);
        styleButton(closeButton, Color.valueOf("#da3d26"));
        closeButton.setOnAction(e -> {
            this.close();
        });
        footerVbox.getChildren().add(closeButton);
    }

    private Button createPartecipaButton() {
        Image uploadImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Media/Icons/uploadIcon.png")));
        ImageView uploadView = new ImageView(uploadImage);

        uploadView.setFitHeight(20);
        uploadView.setFitWidth(20);

        Button partecipaButton = new Button("Partecipa");
        partecipaButton.setGraphic(uploadView);
        partecipaButton.setContentDisplay(ContentDisplay.LEFT);

        styleButton(partecipaButton, Color.valueOf("#3a6698"));

        partecipaButton.setPrefWidth(120);
        partecipaButton.setPrefHeight(30);

        if (checkIfAlreadyAdded()) {
            partecipaButton.setDisable(true);
            partecipaButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
        } else {
            confermarePartecipazioneLabel.setVisible(true);
            partecipaButton.setOnAction(event -> {
                controller.openConfermaPartecipazionePage((SessionePresenza) sessione);
            });
        }
        return partecipaButton;
    }

    private boolean checkIfAlreadyAdded() {
        String matricola = ((Studente) (controller.getUtente())).getMatricola();
        return ((SessionePresenza) sessione).checkIfAlreadyAdded(matricola);
    }

    private Button createEditButton() {
        Image uploadImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Media/Icons/editIcon.png")));
        ImageView uploadView = new ImageView(uploadImage);

        uploadView.setFitHeight(20);
        uploadView.setFitWidth(20);

        Button editButton = new Button("Modifica");
        editButton.setGraphic(uploadView);
        editButton.setContentDisplay(ContentDisplay.LEFT);

        styleButton(editButton, Color.valueOf("#3a6698"));

        editButton.setPrefWidth(120);
        editButton.setPrefHeight(30);

        editButton.setOnAction(event -> {
            controller.openEditSessionePage(sessione, this);
        });

        return editButton;
    }

    private Button createDeleteButton() {
        Image uploadImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Media/Icons/deleteIcon.png")));
        ImageView uploadView = new ImageView(uploadImage);

        uploadView.setFitHeight(20);
        uploadView.setFitWidth(20);

        Button deleteButton = new Button("Elimina");
        deleteButton.setGraphic(uploadView);
        deleteButton.setContentDisplay(ContentDisplay.LEFT);

        styleButton(deleteButton, Color.valueOf("#da3d26"));

        deleteButton.setPrefWidth(120);
        deleteButton.setPrefHeight(30);

        deleteButton.setOnAction(event -> {
            // TODO DELETE THIS SESSION
            showConfirmPanel("Sei sicuro di voler eliminare la sessione?", () -> {
                try {
                    controller.deleteSessione(sessione);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        });

        return deleteButton;
    }

    private void styleButton(Button button, Color color) {
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setBackground(new Background(new BackgroundFill(color, new CornerRadii(8), Insets.EMPTY)));
        button.setCursor(Cursor.HAND);
        button.setOnMouseEntered(e -> button.setOpacity(0.8));
        button.setOnMouseExited(e -> button.setOpacity(1.0));
    }

    public Sessione getSessione() {
        return sessione;
    }

    public void changeUploadButton() {
        footerVbox.getChildren().set(0, createPartecipaButton());
        confermarePartecipazioneLabel.setVisible(false);
    }

    private void showConfirmPanel(String message, Runnable onConfirm) {
        Stage confirmStage = new Stage();
        confirmStage.initModality(Modality.APPLICATION_MODAL);
        confirmStage.initStyle(StageStyle.TRANSPARENT);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(15), Insets.EMPTY)));
        root.setBorder(new Border(new BorderStroke(Color.valueOf("#3A6698"), BorderStrokeStyle.SOLID, new CornerRadii(15), new BorderWidths(2))));

        Label label = new Label(message);
        label.setFont(Font.font("System", FontWeight.BOLD, 18));
        label.setTextFill(Color.valueOf("#2F3A42"));
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setMaxWidth(300);

        Button yesButton = new Button("Si");
        Button noButton = new Button("No");

        styleButton(yesButton, Color.valueOf("#3A6698"));
        styleButton(noButton, Color.valueOf("#da3d26"));

        HBox buttons = new HBox(15, yesButton, noButton);
        buttons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(label, buttons);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        confirmStage.setScene(scene);

        yesButton.setOnAction(e -> {
            onConfirm.run();
            confirmStage.close();
        });

        noButton.setOnAction(e -> confirmStage.close());

        confirmStage.showAndWait();
    }
}
