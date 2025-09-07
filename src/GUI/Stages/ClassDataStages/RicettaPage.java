package GUI.Stages.ClassDataStages;

import Controller.Controller;
import Entity.Ingrediente;
import Entity.Ricetta;
import GUI.Buttons.MyButton;
import GUI.Stages.MyStage;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.StageStyle;

import java.sql.SQLException;

public class RicettaPage extends MyStage {

    private VBox root;
    private HBox topHbox;
    private HBox bottomHbox;
    private VBox footerVbox;

    private Controller controller;
    private Ricetta ricetta;

    public RicettaPage(Controller controller){
        super(900, 750, RootType.VBOX);
        this.controller = controller;

        root = getRootVBox();
        root.setSpacing(15);
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.TOP_LEFT);

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

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        root.getChildren().addAll(topHbox, bottomHbox, spacer, footerVbox);
    }

    public void initPage(Ricetta ricetta){
        this.ricetta = ricetta;

        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.TOP_LEFT);

        this.buildInfoBox(infoBox);

        MyButton closeButton = createCloseButton();
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

    private MyButton createCloseButton() {
        MyButton closeButton = new MyButton("Chiudi", MyButton.ButtonType.SECONDARY);

        closeButton.setOnAction(e -> this.close());

        return closeButton;
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
            showDialog("Errore di sistema. Riprovare più tardi");
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
