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
import javafx.scene.text.Text;

import java.util.Objects;

public class CorsoPanel extends VBox {
    Controller controller;
    Corso corso;
    ImageView imageView;
    Text titoloText;

    public CorsoPanel(Controller controller) {
        this.controller = controller;
        setSpacing(10);
        setAlignment(Pos.TOP_CENTER);
        setPrefSize(330, 355);
        setMinSize(330, 355);
        setMaxSize(330, 355);
        setPadding(new Insets(0));
        setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        setStyle("-fx-cursor: hand;");
        setOnMouseClicked(e -> controller.openCorsoPage(corso));
        setEffect(new DropShadow(10,Color.GRAY));
        this.setFocusTraversable(false);

    }

    public void setCorso(Corso corso) {
        this.corso = corso;
        getChildren().clear();
        imageView = createImage(corso.getImagePath());
        titoloText = createTitolo(corso.getNome());

        getChildren().addAll(imageView, titoloText);
    }

    private Text createTitolo(String titolo) {
        Text titoloText = new Text(titolo);
        titoloText.setFont(Font.font("Arial", 25));
        titoloText.setFill(Color.valueOf("#2F3A42"));
        titoloText.setStyle("-fx-font-weight: bold;");

        titoloText.setFocusTraversable(false);
        return titoloText;
    }

    private ImageView createImage(String path) {
        Image img;
        try {
            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(path)));
        } catch (Exception e) {
            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Media/Logos/UninaFoodLabLogo.png")));
        }

        ImageView iv = new ImageView(img);
        iv.setFitWidth(330);
        iv.setFitHeight(260);
        iv.setPreserveRatio(false);
        iv.setFocusTraversable(false);
        return iv;
    }
}



