package GUI.Stages;

import Controller.Controller;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class CorsoPage extends Stage {

    private VBox vbox;
    private Controller controller;
    private Corso corso;
    private Chef chef;

    public CorsoPage(Controller controller){
        this.controller = controller;
        this.initStyle(StageStyle.TRANSPARENT);

        vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(15));
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(30), Insets.EMPTY)));
        vbox.setBorder(new Border(new BorderStroke(Color.valueOf("#3A6698"), BorderStrokeStyle.SOLID, new CornerRadii(30), new BorderWidths(2))));

        Rectangle clip = new Rectangle();
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        vbox.setClip(clip);
        vbox.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            clip.setWidth(newBounds.getWidth());
            clip.setHeight(newBounds.getHeight());
        });

        Scene scene = new Scene(vbox, 850, 650);
        scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
    }

    public void initPage(Corso corso, Chef chef){
        this.corso = corso;
        this.chef = chef;

        vbox.getChildren().clear();

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

        DecimalFormat df = new DecimalFormat("#.##");
        Label ore = new Label("Ore totali: " + df.format(corso.getOreTotali()) + ", Frequenza: " + corso.getFrequenzaSettimanale() + " lezione a settimana");
        vbox.getChildren().add(ore);

        Label costo = new Label("Costo: " + df.format(corso.getCosto()) + " €");
        vbox.getChildren().add(costo);

        Text descrizione = new Text(corso.getDesc_corso());
        descrizione.setWrappingWidth(400);
        ScrollPane scrollPane = new ScrollPane(descrizione);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        scrollPane.setBorder(new Border(new BorderStroke(Color.valueOf("#3A6698"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0.5))));
        vbox.getChildren().add(scrollPane);

        if (!controller.isHomePageChef()) {
            Button subscribeButton = createSubscribeButton(controller.isAlreadyLoggedIn());
            vbox.getChildren().add(subscribeButton);
            VBox.setMargin(subscribeButton, new Insets(20, 0, 0, 0));
        }

        Button closeButton = createCloseButton();
        vbox.getChildren().add(closeButton);
        VBox.setMargin(closeButton, new Insets(0, 0, 10, 0));
    }

    private void addImageCorso(String imagePath) {
        ImageView imageView;
        try {
            Image image = new Image(imagePath, 400, 200, true, true);
            imageView = new ImageView(image);
        } catch (Exception e) {
            imageView = new ImageView();
        }
        vbox.getChildren().add(imageView);
    }

    public Corso getCorso() {
        return corso;
    }

    private Button createSubscribeButton(boolean isLoggedIn) {
        Button subscribeButton = new Button("Iscriviti");
        subscribeButton.setPrefSize(100, 30);
        styleButton(subscribeButton, Color.valueOf("#3a6698"));

        subscribeButton.setOnAction(event -> {
            if (!isLoggedIn) {
                controller.openLoginPage();
                this.close();
            } else {
                controller.subscribeToCourse(corso);
                setIscrittoCorso(subscribeButton);
            }
        });

        if (controller.alreadySubscribed(corso)) {
            setIscrittoCorso(subscribeButton);
        }

        return subscribeButton;
    }

    private void setIscrittoCorso(Button subscribeButton) {
        subscribeButton.setText("Iscritto");
        subscribeButton.setDisable(true);
        subscribeButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
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
            button.setStyle("-fx-background-color: WHITE;-fx-text-fill: \""+ color +"\";");
            button.setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,0,1,0))));
        });

        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: \""+ color +"\";-fx-text-fill: WHITE;");
            button.setBorder(null);
        });

        button.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                button.setStyle("-fx-background-color: WHITE;-fx-text-fill: \""+ color +"\";");
                button.setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,0,1,0))));
            } else {
                button.setStyle("-fx-background-color: \""+ color +"\";-fx-text-fill: WHITE;");
                button.setBorder(null);
            }
        });
    }
}
