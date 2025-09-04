package GUI.Stages;

import Controller.Controller;
import Entity.Corso;
import Entity.Ingrediente;
import Entity.Ricetta;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class RicettaPage extends Stage {

    private VBox vbox;
    private HBox topHbox;
    private HBox bottomHbox;
    private VBox footerVbox;

    private Controller controller;
    private Ricetta ricetta;

    public RicettaPage(Controller controller){
        this.controller = controller;
        this.initStyle(StageStyle.TRANSPARENT);

        vbox = new VBox(15);
        vbox.setPadding(new Insets(50, 0, 0, 0));
        vbox.setPadding(new Insets(15));
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(30), Insets.EMPTY)));
        vbox.setBorder(new Border(new BorderStroke(Color.valueOf("#3A6698"), BorderStrokeStyle.SOLID, new CornerRadii(30), new BorderWidths(2))));

        topHbox = new HBox(15);
        topHbox.setPadding(new Insets(50, 0, 10, 0));
        topHbox.setAlignment(Pos.TOP_CENTER);
        topHbox.setSpacing(40);
        topHbox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(30), Insets.EMPTY)));

        bottomHbox = new HBox(15);
        bottomHbox.setPadding(new Insets(0, 0, 0, 0));
        bottomHbox.setAlignment(Pos.TOP_CENTER);
        bottomHbox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(30), Insets.EMPTY)));

        footerVbox = new VBox(15);
        footerVbox.setPadding(new Insets(0, 0, 50, 0));
        footerVbox.setAlignment(Pos.BOTTOM_CENTER);
        footerVbox.setSpacing(20);
        footerVbox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(30), Insets.EMPTY)));

        Rectangle clip = new Rectangle();
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        vbox.setClip(clip);
        vbox.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            clip.setWidth(newBounds.getWidth());
            clip.setHeight(newBounds.getHeight());
        });

        Platform.runLater(() -> clip.requestFocus()); // sposta il focus in modo da non selezionare il primo pulsante automaticamente

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        vbox.getChildren().addAll(topHbox, bottomHbox, spacer, footerVbox);

        Scene scene = new Scene(vbox, 900, 750);
        scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
    }

    public void initPage(Ricetta ricetta){
        this.ricetta = ricetta;


        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.TOP_LEFT);

        this.buildInfoBox(infoBox);

       // controller.getIngredientiRicetta(ricetta);

        Button closeButton = createCloseButton();
        footerVbox.getChildren().add(closeButton);
        HBox.setMargin(closeButton, new Insets(0, 0, 10, 0));

        VBox descBox = new VBox();
        descBox.setAlignment(Pos.TOP_LEFT);

        this.buildDescBox(descBox);

        Region spacer1 = new Region();
        spacer1.setMinWidth(20);
        spacer1.setPrefWidth(30);
        spacer1.setMaxWidth(50);

        Region spacer2 = new Region();
        Region spacer3 = new Region();

        HBox.setHgrow(spacer2, Priority.ALWAYS);
        HBox.setHgrow(spacer3, Priority.ALWAYS);

        Region spacer4 = new Region();
        spacer4.setMinWidth(20);
        spacer4.setPrefWidth(30);
        spacer4.setMaxWidth(50);

        Region spacer5 = new Region();
        spacer5.setMinWidth(20);
        spacer5.setPrefWidth(30);
        spacer5.setMaxWidth(100);

        HBox.setHgrow(spacer5, Priority.ALWAYS);

        topHbox.getChildren().addAll(spacer1, infoBox, spacer2, spacer3);
        bottomHbox.getChildren().addAll(spacer4, descBox, spacer5);
    }


    public Ricetta getRicetta() {
        return ricetta;
    }

    private Button createCloseButton() {
        Button closeButton = new Button("Chiudi");
        closeButton.setPrefSize(100, 30);
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

    private void buildInfoBox(VBox infoBox) {
        Label nomeRicetta = new Label(ricetta.getNome());
        nomeRicetta.setFont(Font.font(40));
        nomeRicetta.setTextFill(Color.valueOf("#3A6698"));
        nomeRicetta.setStyle("-fx-font-weight: bold;");

        nomeRicetta.setMaxWidth(800);
        nomeRicetta.setWrapText(true);
        infoBox.getChildren().add(nomeRicetta);

        try {
            controller.getIngredientiRicetta(ricetta);
            controller.getAllergeniRicetta(ricetta);
        } catch (SQLException sqle) {
            // TODO dialog
            sqle.printStackTrace();
        }

        Text allergeniLabel = new Text("Allergeni: ");
        allergeniLabel.setStyle("-fx-font-weight: bold;");
        Text allergeniValue = new Text(ricetta.getAllergeniRicettaString());
        CorsoPage.setAndAddFont(infoBox, allergeniLabel, allergeniValue);

        Text tempoLabel = new Text("Tempo di preparazione: ");
        tempoLabel.setStyle("-fx-font-weight: bold;");
        Text tempoValue = new Text(String.valueOf(ricetta.getTempoPreparazione()) + " minuti");
        CorsoPage.setAndAddFont(infoBox, tempoLabel, tempoValue);

        Text descrizioneLabel = new Text("Descrizione: ");
        descrizioneLabel.setStyle("-fx-font-weight: bold;");
        Text descrizioneValue = new Text(ricetta.getDescrizione());
        CorsoPage.setAndAddFont(infoBox, descrizioneLabel, descrizioneValue);

        if (ricetta.getAutore() != null) {
            Text autoreLabel = new Text("Autore: ");
            autoreLabel.setStyle("-fx-font-weight: bold;");
            Text autoreValue = new Text(ricetta.getAutore());
            CorsoPage.setAndAddFont(infoBox, autoreLabel, autoreValue);
        }
    }

    private void buildDescBox(VBox descBox) {
        Label ricetteTrattate = new Label("Ingredienti: ");
        ricetteTrattate.setFont(Font.font(30));;
        ricetteTrattate.setTextFill(Color.valueOf("#000000"));
        ricetteTrattate.setStyle("-fx-font-weight: bold;");
        ricetteTrattate.setAlignment(Pos.CENTER_LEFT);
        descBox.getChildren().add(ricetteTrattate);
        descBox.setMargin(ricetteTrattate, new Insets(0, 500, 10, 0));

        String quantitaIngrediente = "";
        try {
            for (Ingrediente ingrediente : ricetta.getIngredienti()) {
                quantitaIngrediente = controller.getQuantitaIngrediente(ricetta, ingrediente);

                Text nomeText = new Text("   \u2022 " + ingrediente.getNome() + ": ");
                nomeText.setFont(Font.font("System", FontWeight.BOLD, 17));
                nomeText.setFill(Color.BLACK);

                Text quantitaText = new Text(quantitaIngrediente);
                quantitaText.setFont(Font.font("System", FontPosture.ITALIC, 17));
                quantitaText.setFill(Color.BLACK);

                TextFlow ricettaFlow = new TextFlow(nomeText, quantitaText);
                ricettaFlow.setTextAlignment(TextAlignment.LEFT);

                descBox.getChildren().add(ricettaFlow);
            }
        } catch (SQLException sqle) {
            // TODO dialog
            sqle.printStackTrace();
        }
    }
}
