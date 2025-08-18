package GUI.Pane;

import Controller.Controller;
import javafx.geometry.*;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.effect.DropShadow;

import java.util.Objects;

public class CorsoPanel extends Pane {

    Controller controller;
    String titolo;
    String imagePath;

    public CorsoPanel(String imagePath, String titolo, Controller controller) {
        this.imagePath = imagePath;
        this.titolo = titolo;
        this.controller = controller;

        VBox content = new VBox(10);
        content.setAlignment(Pos.TOP_CENTER);
        content.setPrefSize(330, 355);
        content.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        content.setBorder(new Border(new BorderStroke(Color.valueOf("#3A6698"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        content.setEffect(new DropShadow(10, Color.GRAY));

        ImageView imageView = createImage(imagePath);
        Label titoloLabel = createTitolo(titolo);
        content.getChildren().addAll(imageView, titoloLabel);

        this.getChildren().addAll(content);
        this.setOnMouseClicked(e -> controller.openCorsoPage(titolo, imagePath, controller));
    }

    private ImageView createImage(String imagePath){
        Image corsoImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        ImageView imageVw = new ImageView(corsoImage);
        imageVw.setFitHeight(260);
        imageVw.setFitWidth(330);
        return imageVw;
    }

    private Label createTitolo(String titolo){
        Label titoloLabel = new Label(titolo);
        titoloLabel.setFont(Font.font("Nimbus Roman", 25));
        titoloLabel.setTextFill(Color.valueOf("#3A6698"));
        return titoloLabel;
    }
}


