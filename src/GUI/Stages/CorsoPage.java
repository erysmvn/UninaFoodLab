package GUI.Stages;

import Controller.Controller;
import Entity.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class CorsoPage extends Stage {

    private VBox vbox;
    private HBox topHbox;
    private HBox bottomHbox;
    private VBox footerVbox;

    private Controller controller;
    private Corso corso;

    public CorsoPage(Controller controller){
        this.controller = controller;
        this.initStyle(StageStyle.TRANSPARENT);

        vbox = new VBox(15);
        vbox.setPadding(new Insets(15));
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(30), Insets.EMPTY)));
        vbox.setBorder(new Border(new BorderStroke(Color.valueOf("#3A6698"), BorderStrokeStyle.SOLID, new CornerRadii(30), new BorderWidths(2))));

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

        Rectangle clip = new Rectangle();
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        vbox.setClip(clip);
        vbox.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> {
            clip.setWidth(newBounds.getWidth());
            clip.setHeight(newBounds.getHeight());
        });

        Platform.runLater(() -> clip.requestFocus()); // sposta il focus in modo da non selezionare il primo pulsante automaticamente

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        vbox.getChildren().addAll(topHbox, bottomHbox, spacer, footerVbox);

        Scene scene = new Scene(vbox, 900, 750);
        scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
    }

    public void initPage(Corso corso){
        this.corso = corso;


        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.TOP_RIGHT);

        VBox imgBox = new VBox(10);
        infoBox.setAlignment(Pos.TOP_LEFT);

        addImageCorso(corso.getImagePath(), imgBox);

        this.buildInfoBox(infoBox);

        controller.getRicetteTrattate(corso);

        if (!controller.isHomePageChef()) {
            Button subscribeButton = createSubscribeButton(controller.isAlreadyLoggedIn());
            footerVbox.getChildren().add(subscribeButton);
            VBox.setMargin(subscribeButton, new Insets(20, 0, 0, 0));
        }

        Button closeButton = createCloseButton();
        footerVbox.getChildren().add(closeButton);
        HBox.setMargin(closeButton, new Insets(0, 0, 10, 0));

        VBox descBox = new VBox();
        descBox.setAlignment(Pos.TOP_LEFT);

        this.buildDescBox(descBox);

        Region spacer1 = new Region();
        Region spacer2 = new Region();
        Region spacer3 = new Region();

        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        HBox.setHgrow(spacer3, Priority.ALWAYS);

        topHbox.getChildren().addAll(spacer1, imgBox, spacer2, infoBox, spacer3);
        topHbox.setMargin(imgBox, new Insets(20, 0, 0, 0));
        bottomHbox.getChildren().add(descBox);
    }

    private void addImageCorso(String imagePath, VBox imgBox) {
        ImageView imageView;
        try {
            Image image = new Image(imagePath);
            imageView = new ImageView(image);

            double size = 250;

            double imgWidth = image.getWidth();
            double imgHeight = image.getHeight();

            double x = 0, y = 0, width = imgWidth, height = imgHeight;

            if (imgWidth > imgHeight) {
                width = imgHeight;
                x = (imgWidth - imgHeight) / 2;
            } else if (imgHeight > imgWidth) {
                height = imgWidth;
                y = (imgHeight - imgWidth) / 2;
            }

            imageView.setViewport(new javafx.geometry.Rectangle2D(x, y, width, height));
            imageView.setFitWidth(size);
            imageView.setFitHeight(size);
            imageView.setPreserveRatio(false);

            Rectangle clip = new Rectangle(size, size);
            clip.setArcWidth(20);
            clip.setArcHeight(20);
            imageView.setClip(clip);

        } catch (Exception e) {
            imageView = new ImageView();
        }

        imgBox.getChildren().add(imageView);
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
        button.setPrefSize(80, 30);
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setBackground(new Background(new BackgroundFill(color, new CornerRadii(8), Insets.EMPTY)));
        button.setCursor(Cursor.HAND);

        button.setOnMouseEntered(e -> button.setOpacity(0.8));
        button.setOnMouseExited(e -> button.setOpacity(1.0));
    }

    private void buildInfoBox(VBox infoBox) {
        Text nomeCorso = new Text(corso.getNome());
        nomeCorso.setFont(Font.font(40));
        nomeCorso.setFill(Color.valueOf("#3A6698"));
        nomeCorso.setStyle("-fx-font-weight: bold;");

        TextFlow nomeCorsoFlow = new TextFlow(nomeCorso);
        nomeCorsoFlow.setMaxWidth(400);
        nomeCorsoFlow.setPrefWidth(400);
        nomeCorsoFlow.setTextAlignment(TextAlignment.LEFT);
        infoBox.getChildren().add(nomeCorsoFlow);


        controller.getChefs(corso); // TODO potrebbe essere una buona idea rivedere come funziona sto coso

        Text chefLabel = new Text("Chef: ");
        chefLabel.setStyle("-fx-font-weight: bold;");
        Text chefValue = new Text(corso.getStringOfChefs());
        chefLabel.setFont(Font.font(20));
        chefValue.setFont(Font.font(20));
        TextFlow nomeChef = new TextFlow(chefLabel, chefValue);

        nomeChef.setMaxWidth(400);
        nomeChef.setPrefWidth(400);
        nomeChef.setLineSpacing(2);
        infoBox.getChildren().add(nomeChef);


        Text modalitaLabel = new Text("Modalità: ");
        modalitaLabel.setStyle("-fx-font-weight: bold;");
        Text modalitaValue = new Text(corso.getModalita_corso().getLabel());
        modalitaLabel.setFont(Font.font(20));
        modalitaValue.setFont(Font.font(20));
        TextFlow modalita = new TextFlow(modalitaLabel, modalitaValue);

        modalita.setMaxWidth(400);
        modalita.setPrefWidth(400);
        modalita.setLineSpacing(2);
        infoBox.getChildren().add(modalita);


        Text diffLabel = new Text("Difficoltà: ");
        diffLabel.setStyle("-fx-font-weight: bold;");
        Text diffValue = new Text(corso.getDifficolta().toString());
        diffLabel.setFont(Font.font(20));
        diffValue.setFont(Font.font(20));
        TextFlow difficolta = new TextFlow(diffLabel, diffValue);

        difficolta.setMaxWidth(400);
        difficolta.setPrefWidth(400);
        difficolta.setLineSpacing(2);
        infoBox.getChildren().add(difficolta);


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Text periodoLabel = new Text("Periodo: ");
        periodoLabel.setStyle("-fx-font-weight: bold;");
        Text periodoValue = new Text(
                sdf.format(corso.getDataInizio()) + " - " + sdf.format(corso.getDataFine())
        );
        periodoLabel.setFont(Font.font(20));
        periodoValue.setFont(Font.font(20));
        TextFlow date = new TextFlow(periodoLabel, periodoValue);

        date.setMaxWidth(400);
        date.setPrefWidth(400);
        date.setLineSpacing(2);
        infoBox.getChildren().add(date);

        // Ore totali
        DecimalFormat df = new DecimalFormat("#.##");
        Text oreLabel = new Text("Ore totali: ");
        oreLabel.setStyle("-fx-font-weight: bold;");
        Text oreValue = new Text(
                df.format(corso.getOreTotali()));
        oreLabel.setFont(Font.font(20));
        oreValue.setFont(Font.font(20));
        TextFlow ore = new TextFlow(oreLabel, oreValue);

        ore.setMaxWidth(400);
        ore.setPrefWidth(400);
        ore.setLineSpacing(2);
        infoBox.getChildren().add(ore);


        Text freqLabel = new Text("Frequenza: ");
        freqLabel.setStyle("-fx-font-weight: bold;");
        Text freqValue = new Text(
                corso.getFrequenzaSettimanale() +
                        (corso.getFrequenzaSettimanale() == 1 ? " lezione a settimana" : " lezioni a settimana")
        );
        freqLabel.setFont(Font.font(20));
        freqValue.setFont(Font.font(20));
        TextFlow frequenza = new TextFlow(freqLabel, freqValue);

        frequenza.setMaxWidth(400);
        frequenza.setPrefWidth(400);
        frequenza.setLineSpacing(2);
        infoBox.getChildren().add(frequenza);

        // Costo
        Text costoLabel = new Text("Costo: ");
        costoLabel.setStyle("-fx-font-weight: bold;");
        Text costoValue = new Text(df.format(corso.getCosto()) + " €");
        costoLabel.setFont(Font.font(20));
        costoValue.setFont(Font.font(20));
        TextFlow costo = new TextFlow(costoLabel, costoValue);

        costo.setMaxWidth(400);
        costo.setPrefWidth(400);
        costo.setLineSpacing(2);
        infoBox.getChildren().add(costo);
    }

    private void buildDescBox(VBox descBox) {
        Label ricetteTrattate = new Label("Ricette trattate: ");
        ricetteTrattate.setFont(Font.font(30));;
        ricetteTrattate.setTextFill(Color.valueOf("#000000"));
        ricetteTrattate.setStyle("-fx-font-weight: bold;");
        ricetteTrattate.setAlignment(Pos.CENTER_LEFT);
        descBox.getChildren().add(ricetteTrattate);
        descBox.setMargin(ricetteTrattate, new Insets(0, 500, 10, 0));

        for (Ricetta r : corso.getRicetteTrattate()) {
            Label ricettaLabel = new Label("   \u2022 " + r.getNome());
            ricettaLabel.setFont(Font.font(17));;
            ricettaLabel.setTextFill(Color.valueOf("#000000"));
            ricettaLabel.setAlignment(Pos.CENTER_LEFT);
            ricettaLabel.setStyle("-fx-cursor: hand;");

            // Evento click sulla label
            ricettaLabel.setOnMouseClicked(event -> {
                controller.openRicettaPage(r);
            });

            descBox.getChildren().add(ricettaLabel);
        }
    }
}
