package GUI.Pane;

import Controller.Controller;
import Entity.Chef;
import Entity.Corso;
import Entity.Studente;
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

import java.util.Objects;
import java.util.Optional;

public class ElencoCorsiPanel extends Pane {

        Controller controller;
        Corso corso;
        HBox content;
        AccountCorsiPanel parent;

    public ElencoCorsiPanel(AccountCorsiPanel parent, Controller controller) {
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
            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
        } catch (Exception e) {
            System.out.println("Immagine non trovata: " + imagePath + " -> uso fallback.");
            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Media/Logos/UninaFoodLabLogo.png")));
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
        Button unsubscribeButton = createUnsubscribeButton();

        Button addSessionButton = createAddSessionButton();

        VBox immagineBox  = new VBox(imageView);
        immagineBox.setAlignment(Pos.CENTER);
        immagineBox.setPrefWidth(160);

        VBox infoBox = new VBox(10, titoloLabel, chefsLabel);
        infoBox.setAlignment(Pos.CENTER_LEFT);
        infoBox.setPrefWidth(700);
        infoBox.setMaxWidth(Double.MAX_VALUE);

        VBox buttonBox = new VBox(10, unsubscribeButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setMaxWidth(Double.MAX_VALUE);

        content.getChildren().clear();
        if (controller.getUtente() instanceof Chef) {
            content.getChildren().addAll(immagineBox, infoBox, addSessionButton, buttonBox);
        } else {
            content.getChildren().addAll(immagineBox, infoBox, buttonBox);
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

    private Label creaChefs(String chefs){
        Label chefsLabel = new Label(chefs);
        Font robotoFont = Font.loadFont(
                getClass().getResourceAsStream("/Media/Fonts/Roboto.ttf"),
                12
        );
        chefsLabel.setFont(robotoFont);
        chefsLabel.setTextFill(Color.valueOf("#000000"));
        return chefsLabel;
    }

    private Button createAddSessionButton(){
        Button addSessionButton = new Button("Aggiungi sessione");
        addSessionButton.setTextFill(Color.WHITE);
        addSessionButton.setStyle("-fx-background-color: \"#3A6698\";");

        addSessionButton.setOnAction(event -> {
            // TODO addSessionePage
        });

        this.setOnMouseTraverseBlue(addSessionButton);
        this.setFocusPropretiesBlue(addSessionButton);
        return addSessionButton;
    }

    private Button createUnsubscribeButton(){
        Button unsubscribeButton = new Button("x");
        unsubscribeButton.setTextFill(Color.WHITE);
        unsubscribeButton.setStyle("-fx-background-color: \"#da3d26\";");

        unsubscribeButton.setOnAction(event -> {
            if (controller.getUtente() instanceof Studente studente) {
                showConfirmPanel("Sei sicuro di voler annullare l'iscrizione al corso?", () -> {
                    controller.unsubscribeToCourse(corso);
                });
            } else if (controller.getUtente() instanceof Chef chef) {
                showConfirmPanel("Sei sicuro di voler eliminare il corso?", () -> {
                    controller.deleteCorso(corso);
                });
            }
        });

        this.setOnMouseTraverse(unsubscribeButton);
        this.setFocusPropreties(unsubscribeButton);
        return unsubscribeButton;
    }

    private void showConfirmPanel(String message, Runnable onConfirm) {
        Stage confirmStage = new Stage();
        confirmStage.initModality(Modality.APPLICATION_MODAL);
        confirmStage.initStyle(StageStyle.TRANSPARENT);

        // Layout principale
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

        Button yesButton = new Button("Si");
        Button noButton = new Button("No");

        styleConfirmButton(yesButton, Color.valueOf("#3A6698"));
        styleConfirmButton(noButton, Color.valueOf("#da3d26"));

        HBox buttons = new HBox(15, yesButton, noButton);
        buttons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(label, buttons);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        confirmStage.setScene(scene);

        yesButton.setOnAction(e -> {
            onConfirm.run(); // esegue l'azione passata
            controller.refreshCorsi(parent);
            confirmStage.close();
        });

        noButton.setOnAction(e -> confirmStage.close());

        confirmStage.showAndWait();
    }

    // Stile pulsante coerente con il resto della UI
    private void styleConfirmButton(Button button, Color color) {
        button.setPrefSize(80, 30);
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setBackground(new Background(new BackgroundFill(color, new CornerRadii(8), Insets.EMPTY)));
        button.setCursor(Cursor.HAND);

        button.setOnMouseEntered(e -> button.setOpacity(0.8));
        button.setOnMouseExited(e -> button.setOpacity(1.0));
    }


    private void setOnMouseTraverseBlue(Button button) {
        button.setOnMouseEntered(e -> {
                    button.setStyle("-fx-background-color: #325A8C; -fx-cursor: hand;");
                }
        );
        button.setOnMouseExited(e -> {
                    button.setStyle("-fx-background-color: \"#3A6698\";-fx-text-fill: WHITE;");
                }
        );
    }

    private void setFocusPropretiesBlue(Button button) {
        button.setFocusTraversable(true);
        button.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                button.setStyle("-fx-background-color: #325A8C; -fx-cursor: hand;");
            } else {
                button.setStyle("-fx-background-color: \"#3A6698\";-fx-text-fill: WHITE; -fx-cursor: hand;");
            }
        });
    }

    private void setOnMouseTraverse(Button button) {
        button.setOnMouseEntered(e -> {
                    button.setStyle("-fx-background-color: #ca331d; -fx-cursor: hand;");
                }
        );
        button.setOnMouseExited(e -> {
                    button.setStyle("-fx-background-color: \"#da3d26\";-fx-text-fill: WHITE;");
                }
        );
    }

    private void setFocusPropreties(Button button) {
        button.setFocusTraversable(true);
        button.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                button.setStyle("-fx-background-color: #ca331d; -fx-cursor: hand;");
            } else {
                button.setStyle("-fx-background-color: \"#da3d26\";-fx-text-fill: WHITE; -fx-cursor: hand;");
            }
        });
    }
}
