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
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.net.URI;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class SessionePage extends Stage {
    private Controller controller;
    private Sessione sessione;

    private VBox root;
    private VBox infoBox;
    private HBox topHbox;
    private VBox footerVbox;

    Label confermarePartecipazioneLabel;

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

        footerVbox = new VBox(10);
        footerVbox.setAlignment(Pos.BOTTOM_CENTER);
        footerVbox.setSpacing(20);
        footerVbox.setPadding(new Insets(0, 0, 50, 0));
        footerVbox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

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

        topHbox.getChildren().clear();
        infoBox.getChildren().clear();
        footerVbox.getChildren().clear();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");


        Label titoloLabel = new Label(corso.getNome() +" - "+ sessione.getData().format(dateFormatter));
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
           if(controller.isStudent())
                footerVbox.getChildren().add(createPartecipaButton());
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
        Region spacer = new Region();
        spacer.setPrefHeight(15);

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

        Button closeButton = new Button("Chiudi");
        closeButton.setPrefSize(120, 30);
        styleButton(closeButton, Color.valueOf("#da3d26"));
        closeButton.setOnAction(e -> this.close());
        footerVbox.getChildren().add(closeButton);
    }

    private Button createPartecipaButton(){

        Image uploadImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Media/Icons/uploadIcon.png")));
        ImageView uploadView = new ImageView(uploadImage);

        uploadView.setFitHeight(20);
        uploadView.setFitWidth(20);

        Button partecipaButton = new Button("Partecipa");
        partecipaButton.setGraphic(uploadView);
        partecipaButton.setContentDisplay(ContentDisplay.LEFT);

        styleButton(partecipaButton, Color.valueOf("#3a6698"));

        partecipaButton.setPrefWidth(120);
        partecipaButton.setMinWidth(120);
        partecipaButton.setMaxWidth(120);

        partecipaButton.setPrefHeight(30);
        partecipaButton.setMinHeight(30);
        partecipaButton.setMaxHeight(30);

        if(checkIfAlreadyAdded()){
            partecipaButton.setDisable(true);
            partecipaButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
        }else{
            confermarePartecipazioneLabel.setVisible(true);
            partecipaButton.setOnAction(event -> {
                controller.openConfermaPartecipazionePage((SessionePresenza) sessione);
            });
        }
        return partecipaButton;
    }

    private boolean checkIfAlreadyAdded(){
        String matricola = ((Studente)(controller.getUtente())).getMatricola();
         return  ((SessionePresenza)sessione).checkIfAlreadyAdded(matricola);
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
    public void changeUploadButton(){
        int first = 0;
        footerVbox.getChildren().remove(first);
        footerVbox.getChildren().addFirst(createPartecipaButton());
        confermarePartecipazioneLabel.setVisible(false);
    }
}
