package GUI.Pane;

import Controller.Controller;
import Entity.Corso;

import Exception.CorsoExceptions.imageNotFoundException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.io.InputStream;
import java.util.Objects;

public class CorsoPanel extends VBox {
    Controller controller;
    Corso corso;
    ImageView imageView;
    TextFlow titoloText;

    public CorsoPanel(Controller controller) {
        this.controller = controller;
        this.setCorsoPanelAesthetics();
        this.setCorsoPanelFunctionalities();
    }

    private void setCorsoPanelFunctionalities() {
        setOnMouseClicked(e -> controller.openCorsoPage(corso));
        this.setFocusTraversable(false);
    }

    private void setCorsoPanelAesthetics(){
        setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        setStyle("-fx-cursor: hand;");
        setEffect(new DropShadow(10,Color.GRAY));
        this.setCorsoPanelSize();
    }

    private void setCorsoPanelSize(){
        setSpacing(10);
        setAlignment(Pos.TOP_CENTER);
        setPrefSize(330, 355);
        setMinSize(330, 355);
        setMaxSize(330, 355);
        setPadding(new Insets(0));
    }

    public void setCorso(Corso corso) {
        this.corso = corso;
        getChildren().clear();
        imageView = createImage(corso.getImagePath());
        titoloText = createTitolo(corso.getNome());
        titoloText.setPadding(new Insets(10, 0, 0, 0));


        getChildren().addAll(imageView, titoloText);
    }

    private TextFlow createTitolo(String titolo) {
        Text titoloText = new Text(titolo);
        titoloText.setFont(Font.font("System", FontWeight.BOLD, 25));
        TextFlow titoloFlow = new TextFlow(titoloText);
        titoloText.setFill(Color.valueOf("#2F3A42"));
        titoloText.setFocusTraversable(false);

        titoloFlow.setTextAlignment(TextAlignment.CENTER);
        titoloFlow.setMaxWidth(400);
        titoloFlow.setPrefWidth(400);
        titoloFlow.setLineSpacing(2);

        return titoloFlow;
    }

    private ImageView createImage(String imagePath) {
        Image image;
        try {
            InputStream is = getClass().getResourceAsStream(imagePath);
            if (is == null)
                throw new imageNotFoundException("/Media/Background/biancoNormale.png");
            image = new Image(is);

        } catch (imageNotFoundException INFE) {
            image = new Image(Objects.requireNonNull(INFE.getMessage()));
        }

        ImageView iv = new ImageView(image);
        iv.setFitWidth(330);
        iv.setFitHeight(260);
        iv.setPreserveRatio(false);
        iv.setFocusTraversable(false);
        return iv;
    }
}



