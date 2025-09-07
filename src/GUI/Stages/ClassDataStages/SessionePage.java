package GUI.Stages.ClassDataStages;

import Controller.Controller;
import Entity.*;
import GUI.Buttons.MyButton;
import GUI.Stages.MyStage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
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

import javafx.scene.control.ScrollPane;


public class SessionePage extends MyStage {
    private Controller controller;
    private Sessione sessione;

    private BorderPane root;
    private VBox infoBox;
    private HBox topHbox;
    private VBox footerVbox;

    Label confermarePartecipazioneLabel;

    public SessionePage(Controller controller) {
        super(800, 600, RootType.BORDERPANE);
        this.controller = controller;

        // Root BorderPane
        root = getRootBorderPane();

        // Top
        topHbox = new HBox();
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


        if (controller.isStudent())
            footerVbox.getChildren().add(createPartecipaButton());
        else {
            HBox options = new HBox(10);
            options.setAlignment(Pos.CENTER);
            options.getChildren().addAll(createEditButton(), createDeleteButton());
            footerVbox.getChildren().add(options);
        }

        try {
            controller.setChefs(corso);
        } catch (SQLException ex) {
            // TODO Dialog
        }

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


        MyButton closeButton = new MyButton("Chiudi", MyButton.ButtonType.SECONDARY);

        closeButton.setSize(250, 30);

        closeButton.setOnAction(e -> {
            this.close();
        });
        footerVbox.getChildren().add(closeButton);
    }

    private MyButton createPartecipaButton() {
        MyButton partecipaButton = new MyButton("Partecipa", MyButton.ButtonType.PRIMARY);

        partecipaButton.setWithIcon("/Media/Icons/uploadIcon.png", 20, 20);

        partecipaButton.setSize(250, 30);

        if (checkIfAlreadyAdded()) {
            partecipaButton.setDisabledStyle();
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

    private MyButton createEditButton() {
        MyButton editButton = new MyButton("Modifica", MyButton.ButtonType.PRIMARY);

        editButton.setWithIcon("/Media/Icons/editIcon.png", 20, 20);

        editButton.setSize(120, 30);

        editButton.setOnAction(event -> {
            controller.openEditSessionePage(sessione, this);
        });

        return editButton;
    }

    private MyButton createDeleteButton() {
        MyButton deleteButton = new MyButton("Elimina", MyButton.ButtonType.SECONDARY);

        deleteButton.setWithIcon("/Media/Icons/deleteIcon.png", 20, 20);

        deleteButton.setSize(120, 30);

        deleteButton.setOnAction(event -> {
            this.showConfirmPanel("Sei sicuro di voler eliminare la sessione?", () -> {
                try {
                    sessione.getCorso().deleteSessione(sessione);
                    controller.deleteSessione(sessione);
                    controller.refreshCalendario();
                } catch (SQLException e) {
                    showDialog("Errore di sistema. Riprovare più tardi");
                }
            });
        });
        return deleteButton;
    }

    public Sessione getSessione() {
        return sessione;
    }

    public void setSessione(Sessione sessione) {
        this.sessione = sessione;
    }

    public void changeUploadButton() {
        footerVbox.getChildren().set(0, createPartecipaButton());
        confermarePartecipazioneLabel.setVisible(false);
    }
}
