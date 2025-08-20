package GUI.Pane;

import Controller.Controller;
import Entity.Corso;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.util.Objects;

public class ElencoCorsiPanel extends Pane {

        Controller controller;
        Corso corso;
        HBox content;

        public ElencoCorsiPanel(Controller controller) {
            this.controller = controller;

            content = new HBox(10);
            content.setAlignment(Pos.TOP_CENTER);
            content.setPrefSize(900, 150);

            content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
            content.setBorder(new Border(new BorderStroke(Color.valueOf("#FFFFFF"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
            content.setStyle("-fx-cursor: hand;");

            this.getChildren().addAll(content);
            this.setOnMouseClicked(e -> controller.openCorsoPage(corso));

        }

    private ImageView createImage(String imagePath) {
        Image image;
        try {
            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            System.out.println("Immagine non trovata: " + imagePath + " -> uso fallback.");
            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/UninaFoodLabLogo.png")));
        }

        ImageView imageView = new ImageView(image);

        // Dimensioni target = quelle del pannello
        double targetWidth = 150;
        double targetHeight = 150;

        // Dimensioni originali immagine
        double imgWidth = image.getWidth();
        double imgHeight = image.getHeight();

        // Rapporto scala per "cover"
        double scale = Math.max(targetWidth / imgWidth, targetHeight / imgHeight);

        double newWidth = imgWidth * scale;
        double newHeight = imgHeight * scale;

        // Calcolo offset per ritaglio centrale
        double x = (newWidth - targetWidth) / 2 / scale;
        double y = (newHeight - targetHeight) / 2 / scale;

        // Imposto viewport (ritaglio)
        imageView.setViewport(new javafx.geometry.Rectangle2D(x, y, targetWidth / scale, targetHeight / scale));

        // Fit al pannello
        imageView.setFitWidth(targetWidth);
        imageView.setFitHeight(targetHeight);
        imageView.setPreserveRatio(false);

        // Clip con bordi arrotondati
        Rectangle clip = new Rectangle(targetWidth, targetHeight);
        clip.setArcHeight(20);
        clip.setArcWidth(20);
        imageView.setClip(clip);

        return imageView;
    }

    public void setCorso(Corso corso){
        this.corso = corso;

        ImageView imageView = createImage(corso.getImagePath());
        Label titoloLabel = createTitolo(corso.getNome());
        controller.getChefs(corso);
        Label chefsLabel = creaChefs(corso.getStringOfChefs());

        // VBox immagine
        VBox immagineBox  = new VBox(imageView);
        immagineBox.setAlignment(Pos.CENTER);
        immagineBox.setPrefWidth(160); // larghezza fissa per tutte le immagini

        // VBox info corso
        VBox infoBox = new VBox(10, titoloLabel, chefsLabel);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setPrefWidth(700); // larghezza fissa o flessibile
        infoBox.setMaxWidth(Double.MAX_VALUE);

        // Imposto HBox principale
        content.getChildren().clear();
        content.getChildren().addAll(immagineBox, infoBox);
        content.setAlignment(Pos.CENTER_LEFT);
    }

    private Label createTitolo(String titolo){
        Label titoloLabel = new Label(titolo);
        titoloLabel.setFont(Font.font("Nimbus Roman", 20));
        titoloLabel.setTextFill(Color.valueOf("#3A6698"));
        return titoloLabel;
    }

    private Label creaChefs(String chefs){
        Label chefsLabel = new Label(chefs);
        chefsLabel.setFont(Font.font("Nimbus Roman", 12));
        chefsLabel.setTextFill(Color.valueOf("#000000"));
        return chefsLabel;
    }
}
