package GUI.Pane;

import Controller.Controller;
import Entity.Chef;
import Entity.Corso;
import Entity.Studente;
import Exception.CorsoExceptions.imageNotFoundException;
import GUI.Buttons.MyButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class CorsoAccountPanel extends Pane {
        Controller controller;
        Corso corso;
        HBox content;
        ElencoCorsiPanel parent;

    public CorsoAccountPanel(ElencoCorsiPanel parent, Controller controller) {
        this.parent = parent;
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
            InputStream is = getClass().getResourceAsStream(imagePath);
            if (is == null)
                throw new imageNotFoundException("/Media/Background/biancoNormale.png");

            image = new Image(is);

        } catch (imageNotFoundException INFE){
            image = new Image(INFE.getMessage());
        }

        ImageView imageView = new ImageView(image);

        double targetWidth = 150;
        double targetHeight = 150;

        double imgWidth = image.getWidth();
        double imgHeight = image.getHeight();

        double scale = Math.max(targetWidth / imgWidth, targetHeight / imgHeight);

        double newWidth = imgWidth * scale;
        double newHeight = imgHeight * scale;

        double x = (newWidth - targetWidth) / 2 / scale;
        double y = (newHeight - targetHeight) / 2 / scale;

        imageView.setViewport(new javafx.geometry.Rectangle2D(x, y, targetWidth / scale, targetHeight / scale));
        imageView.setFitWidth(targetWidth);
        imageView.setFitHeight(targetHeight);
        imageView.setPreserveRatio(false);


        Rectangle clip = new Rectangle(targetWidth, targetHeight);
        clip.setArcHeight(20);
        clip.setArcWidth(20);
        imageView.setClip(clip);

        return imageView;
    }

    public void setCorso(Corso corso) {
        this.corso = corso;

        ImageView imageView = createImage(corso.getImagePath());
        Label titoloLabel = createTitolo(corso.getNome());
        try {
            controller.setChefs(corso);
        } catch (SQLException e) {
            //todo dialog
        }

        Label chefsLabel = createChefs(corso.getStringOfChefs());
        Button unsubscribeButton = createUnsubscribeButton();
        Button addSessionButton = createAddSessionButton();
        Button updateButton = createUpdateButton();

        VBox imagineBox  = new VBox(imageView);
        imagineBox.setAlignment(Pos.CENTER);
        imagineBox.setPrefWidth(160);

        VBox infoBox = new VBox(10, titoloLabel, chefsLabel);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setPrefWidth(700);
        infoBox.setMaxWidth(Double.MAX_VALUE);

        VBox buttonBox = new VBox(10, unsubscribeButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setMaxWidth(Double.MAX_VALUE);

        content.getChildren().clear();
        if (controller.getUtente() instanceof Chef) {
            content.getChildren().addAll(imagineBox, infoBox, addSessionButton, updateButton, buttonBox);
        } else {
            content.getChildren().addAll(imagineBox, infoBox, buttonBox);
        }
        content.setAlignment(Pos.CENTER_LEFT);
    }

    private Label createTitolo(String titolo){
        Label titoloLabel = new Label(titolo);
        Font robotoFont = Font.loadFont(
                getClass().getResourceAsStream("/Media/Fonts/Roboto.ttf"),
                20
        );
        titoloLabel.setFont(robotoFont);
        titoloLabel.setTextFill(Color.valueOf("#3A6698"));
        return titoloLabel;
    }

    private Label createChefs(String chefs){
        Label chefsLabel = new Label(chefs);
        Font robotoFont = Font.loadFont(
                getClass().getResourceAsStream("/Media/Fonts/Roboto.ttf"),
                12
        );
        chefsLabel.setFont(robotoFont);
        chefsLabel.setTextFill(Color.BLACK);
        return chefsLabel;
    }

    private Button createAddSessionButton(){
        MyButton addSessionButton = new MyButton("Aggiungi sessione", MyButton.ButtonType.PRIMARY);

        addSessionButton.setPrefWidth(160);
        addSessionButton.setMinWidth(160);
        addSessionButton.setMaxWidth(160);

        addSessionButton.setOnAction(event -> {
            controller.openAggiungiSessionePage(corso);
        });

        return addSessionButton;
    }

    private Button createUnsubscribeButton(){
        MyButton unsubscribeButton = new MyButton("x", MyButton.ButtonType.SECONDARY);

        unsubscribeButton.setPrefWidth(30);
        unsubscribeButton.setMinWidth(30);
        unsubscribeButton.setMaxWidth(30);

        unsubscribeButton.setOnAction(event -> {
            if (controller.getUtente() instanceof Studente studente) {
                showConfirmPanel("Sei sicuro di voler annullare l'iscrizione al corso?", () -> {
                    try {
                        controller.unsubscribeToCourse(corso);
                    } catch (SQLException e) {
                        // TODO DIALOG
                    }
                });
            } else if (controller.getUtente() instanceof Chef chef) {
                showConfirmPanel("Sei sicuro di voler eliminare il corso?", () -> {
                    try {
                        controller.deleteCorso(corso);
                    } catch (SQLException e) {
                        // TODO Dialog
                    }
                });
            }
        });

        return unsubscribeButton;
    }

    private Button createUpdateButton() {
        MyButton updateButton = new MyButton("", MyButton.ButtonType.PRIMARY);
        updateButton.setWithIcon("/Media/Icons/editIcon.png", 16, 16);

        updateButton.setPrefWidth(30);
        updateButton.setMinWidth(30);
        updateButton.setMaxWidth(30);

        updateButton.setOnAction(event -> {
            controller.openEditCorsoPage(corso);
            controller.refreshCorsi(parent);
        });

        return updateButton;
    }

    private void showConfirmPanel(String message, Runnable onConfirm) {
        Stage confirmStage = new Stage();
        confirmStage.initModality(Modality.APPLICATION_MODAL);
        confirmStage.initStyle(StageStyle.TRANSPARENT);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(15), Insets.EMPTY)));
        root.setBorder(new Border(new BorderStroke(Color.valueOf("#3A6698"), BorderStrokeStyle.SOLID, new CornerRadii(15), new BorderWidths(2))));

        Label label = new Label(message);
        label.setFont(Font.font("System", FontWeight.BOLD, 18));
        label.setTextFill(Color.valueOf("#2F3A42"));
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setMaxWidth(300);

        MyButton yesButton = new MyButton("Yes", MyButton.ButtonType.PRIMARY);
        MyButton noButton = new MyButton("No", MyButton.ButtonType.SECONDARY);

        HBox buttons = new HBox(15, yesButton, noButton);
        buttons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(label, buttons);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        confirmStage.setScene(scene);

        yesButton.setOnAction(e -> {
            onConfirm.run();

            ArrayList<Corso> corsi = controller.getUtente().getCorsi();
            corsi.remove(corso);
            controller.refreshCorsi(parent);

            confirmStage.close();
        });

        noButton.setOnAction(e -> confirmStage.close());

        confirmStage.showAndWait();
    }
}