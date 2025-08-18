package GUI.Stages;

import Entity.*;

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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.SimpleDateFormat;
import javafx.scene.shape.Rectangle;
import javafx.stage.StageStyle;

public class CorsoPage extends Stage {

    private VBox vbox;

    public CorsoPage(Corso corso, Chef chef) {

        this.initStyle(StageStyle.TRANSPARENT);
        vbox = new VBox();
        vbox.setBackground(new Background(new BackgroundFill(
                Color.WHITE,
                new CornerRadii(30),
                Insets.EMPTY
        )));

        vbox.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(30),
                new BorderWidths(2)
        )));

        vbox.setSpacing(10);
        vbox.setPadding(new Insets(15));
        vbox.setAlignment(Pos.TOP_CENTER);
        Rectangle clip = new Rectangle();
        clip.setWidth(vbox.getWidth());
        clip.setHeight(vbox.getHeight());
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        addImageCorso(corso.getImagePath());

        Label nomeCorso = new Label(corso.getNome());
        nomeCorso.setFont(Font.font("Nimbus Roman",40));
        nomeCorso.setTextFill(Color.valueOf("#3A6698"));
        vbox.getChildren().add(nomeCorso);

        Label nomeChef = new Label("Chef: "+chef.getNome() + " "+chef.getCognome());
        vbox.getChildren().add(nomeChef);

        Label modalita = new Label("Modalità: " + corso.getModalita_corso().getLabel());
        vbox.getChildren().add(modalita);


        Label difficolta = new Label("Difficoltà: " + corso.getDifficolta().toString());
        vbox.getChildren().add(difficolta);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Label date = new Label("Periodo: " + sdf.format(corso.getDataInizio()) + " - " + sdf.format(corso.getDataFine()));
        vbox.getChildren().add(date);

        Label ore = new Label("Ore totali: " + corso.getOreTotali() + ", Frequenza settimanale: " + corso.getFrequenzaSettimanale());
        vbox.getChildren().add(ore);

        Label costo = new Label("Costo: €" + corso.getCosto());
        vbox.getChildren().add(costo);
        Text descrizione = new Text(corso.getDesc_corso());
        descrizione.setWrappingWidth(400);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(descrizione);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefViewportHeight(50);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        scrollPane.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(0.5)
        )));
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        vbox.getChildren().add(scrollPane);

        Button closeButtone = createCloseButton();
        vbox.getChildren().add(closeButtone);
        VBox.setMargin(closeButtone, new Insets(30, 0, 0, 0));

        vbox.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            clip.setWidth(newBounds.getWidth());
            clip.setHeight(newBounds.getHeight());
        });

        vbox.setClip(clip);

        Scene scene = new Scene(vbox, 450, 600);
        scene.setFill(Color.TRANSPARENT);

        this.setScene(scene);

    }

    public void addImageCorso(String imagePath) {
        ImageView imageView;
        try {
            Image image = new Image(imagePath, 400, 200, true, true);
            imageView = new ImageView(image);
        } catch (Exception e) {
            imageView = new ImageView();
        }

        vbox.getChildren().add(imageView);
    }


    private Button createCloseButton() {
        Button closeButton = new Button();
        closeButton.setPrefWidth(100);
        closeButton.setPrefHeight(30);
        closeButton.setText("Chiudi");

        closeButton.setStyle("-fx-background-color: \"#da3d26\"; -fx-text-fill: white;");
        closeButton.setOnAction(e -> this.close());
        return closeButton;
    }


}
