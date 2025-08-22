package GUI.Pane;

import Controller.Controller;
import Entity.Corso;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.effect.DropShadow;

import java.util.Objects;

public class CorsoPanel extends Pane {

    Controller controller;
    Corso corso;
    VBox content;

    public CorsoPanel(Controller controller) {
        this.controller = controller;

        content = new VBox(10);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPrefSize(330, 355);

        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setBorder(new Border(new BorderStroke(Color.valueOf("#FFFFFF"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        content.setEffect(new DropShadow(10, Color.GRAY));
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
            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Media/UninaFoodLabLogo.png")));
        }

        ImageView imageView = new ImageView(image);

        // Dimensioni target = quelle del pannello
        double targetWidth = 330;
        double targetHeight = 260;

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
        imageView.setClip(clip);

        return imageView;
    }

    public void setCorso(Corso corso){
        this.corso = corso;
        ImageView imageView = createImage(corso.getImagePath());
        Label titoloLabel = createTitolo(corso.getNome());
        content.getChildren().addAll(imageView, titoloLabel);
    }

    private Label createTitolo(String titolo){
        Label titoloLabel = new Label(titolo);
        titoloLabel.setFont(Font.font("Nimbus Roman", 25));
        titoloLabel.setTextFill(Color.valueOf("#3A6698"));
        return titoloLabel;
    }
}


