package GUI.Stages;

import Controller.Controller;
import Entity.Corso;
import Entity.Ingrediente;
import Entity.Ricetta;
import javafx.application.Platform;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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

        controller.getIngredientiRicetta(ricetta);

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
        button.setFocusTraversable(true);
        button.setStyle("-fx-background-color: \""+ color +"\";-fx-text-fill: WHITE;");

        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: WHITE;-fx-text-fill: \""+ color +"\"; -fx-cursor: hand;");
            button.setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,0,1,0))));
        });

        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: \""+ color +"\";-fx-text-fill: WHITE;");
            button.setBorder(null);
        });

        button.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                button.setStyle("-fx-background-color: WHITE;-fx-text-fill: \""+ color +"\"; -fx-cursor: hand;");
                button.setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,0,1,0))));
            } else {
                button.setStyle("-fx-background-color: \""+ color +"\";-fx-text-fill: WHITE;");
                button.setBorder(null);
            }
        });
    }

    private void buildInfoBox(VBox infoBox) {
        Label nomeRicetta = new Label(ricetta.getNome());
        nomeRicetta.setFont(Font.font(40));
        nomeRicetta.setTextFill(Color.valueOf("#3A6698"));
        nomeRicetta.setStyle("-fx-font-weight: bold;");

        nomeRicetta.setMaxWidth(800);
        nomeRicetta.setWrapText(true);
        infoBox.getChildren().add(nomeRicetta);


        controller.getIngredientiRicetta(ricetta);
        controller.getAllergeniRicetta(ricetta);

        Text allergeniLabel = new Text("Allergeni: ");
        allergeniLabel.setStyle("-fx-font-weight: bold;");
        Text allergeniValue = new Text(ricetta.getAllergeniRicettaString());
        allergeniLabel.setFont(Font.font(20));
        allergeniValue.setFont(Font.font(20));
        TextFlow allergeniRicetta = new TextFlow(allergeniLabel, allergeniValue);

        allergeniRicetta.setMaxWidth(400);
        allergeniRicetta.setPrefWidth(400);
        allergeniRicetta.setLineSpacing(2);
        infoBox.getChildren().add(allergeniRicetta);

        Text tempoLabel = new Text("Tempo di preparazione: ");
        tempoLabel.setStyle("-fx-font-weight: bold;");
        Text tempoValue = new Text(String.valueOf(ricetta.getTempoPreparazione()) + " minuti");
        tempoLabel.setFont(Font.font(20));
        tempoValue.setFont(Font.font(20));
        TextFlow tempoRicetta = new TextFlow(tempoLabel, tempoValue);

        tempoRicetta.setMaxWidth(400);
        tempoRicetta.setPrefWidth(400);
        tempoRicetta.setLineSpacing(2);
        infoBox.getChildren().add(tempoRicetta);

        Text descrizioneLabel = new Text("Descrizione: ");
        descrizioneLabel.setStyle("-fx-font-weight: bold;");
        Text descrizioneValue = new Text(ricetta.getDescrizione());
        descrizioneLabel.setFont(Font.font(20));
        descrizioneValue.setFont(Font.font(20));
        TextFlow descrizioneRicetta = new TextFlow(descrizioneLabel, descrizioneValue);

        descrizioneRicetta.setMaxWidth(400);
        descrizioneRicetta.setPrefWidth(400);
        descrizioneRicetta.setLineSpacing(2);
        infoBox.getChildren().add(descrizioneRicetta);

        if (ricetta.getAutore() != null) {
            Text autoreLabel = new Text("Autore: ");
            autoreLabel.setStyle("-fx-font-weight: bold;");
            Text autoreValue = new Text(ricetta.getAutore());
            autoreLabel.setFont(Font.font(20));
            autoreValue.setFont(Font.font(20));
            TextFlow autoreRicetta = new TextFlow(autoreLabel, autoreValue);

            autoreRicetta.setMaxWidth(400);
            autoreRicetta.setPrefWidth(400);
            autoreRicetta.setLineSpacing(2);
            infoBox.getChildren().add(autoreRicetta);
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
    }
}
