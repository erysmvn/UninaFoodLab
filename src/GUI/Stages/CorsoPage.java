package GUI.Stages;

import Controller.Controller;
import Entity.*;
import javafx.application.Platform;
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
import javafx.scene.text.TextFlow;
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
    private Chef chef;

    public CorsoPage(Controller controller){
        this.controller = controller;
        this.initStyle(StageStyle.TRANSPARENT);

        vbox = new VBox(15);
        vbox.setPadding(new Insets(50, 0, 0, 0));
        vbox.setPadding(new Insets(15));
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(30), Insets.EMPTY)));
        vbox.setBorder(new Border(new BorderStroke(Color.valueOf("#3A6698"), BorderStrokeStyle.SOLID, new CornerRadii(30), new BorderWidths(2))));

        topHbox = new HBox(15);
        topHbox.setPadding(new Insets(50, 0, 10, 0));
        topHbox.setAlignment(Pos.TOP_CENTER);
        topHbox.setSpacing(20);
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

        Scene scene = new Scene(vbox, 850, 700);
        scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
    }

    public void initPage(Corso corso, Chef chef){
        this.corso = corso;
        this.chef = chef;


        // VBox per le info del corso
        VBox infoBox = new VBox(10); // spacing tra le righe
        infoBox.setAlignment(Pos.TOP_LEFT);

        addImageCorso(corso.getImagePath());

        // Titolo del corso
        Label nomeCorso = new Label(corso.getNome());
        nomeCorso.setFont(Font.font("Nimbus Roman", 40));
        nomeCorso.setTextFill(Color.valueOf("#3A6698"));
        nomeCorso.setStyle("-fx-font-weight: bold;");
        infoBox.getChildren().add(nomeCorso);

        // Chef
        Text chefLabel = new Text("Chef: ");
        chefLabel.setStyle("-fx-font-weight: bold;");
        Text chefValue = new Text(chef.getNome() + " " + chef.getCognome());
        chefLabel.setFont(Font.font(20));
        chefValue.setFont(Font.font(20));
        TextFlow nomeChef = new TextFlow(chefLabel, chefValue);
        infoBox.getChildren().add(nomeChef);

        // Modalità
        Text modalitaLabel = new Text("Modalità: ");
        modalitaLabel.setStyle("-fx-font-weight: bold;");
        Text modalitaValue = new Text(corso.getModalita_corso().getLabel());
        modalitaLabel.setFont(Font.font(20));
        modalitaValue.setFont(Font.font(20));
        TextFlow modalita = new TextFlow(modalitaLabel, modalitaValue);
        infoBox.getChildren().add(modalita);

        // Difficoltà
        Text diffLabel = new Text("Difficoltà: ");
        diffLabel.setStyle("-fx-font-weight: bold;");
        Text diffValue = new Text(corso.getDifficolta().toString());
        diffLabel.setFont(Font.font(20));
        diffValue.setFont(Font.font(20));
        TextFlow difficolta = new TextFlow(diffLabel, diffValue);
        infoBox.getChildren().add(difficolta);

        // Periodo
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Text periodoLabel = new Text("Periodo: ");
        periodoLabel.setStyle("-fx-font-weight: bold;");
        Text periodoValue = new Text(
                sdf.format(corso.getDataInizio()) + " - " + sdf.format(corso.getDataFine())
        );
        periodoLabel.setFont(Font.font(20));
        periodoValue.setFont(Font.font(20));
        TextFlow date = new TextFlow(periodoLabel, periodoValue);
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
        infoBox.getChildren().add(ore);

        // Frequenza settimanale
        Text freqLabel = new Text("Frequenza: ");
        freqLabel.setStyle("-fx-font-weight: bold;");
        Text freqValue = new Text(
                corso.getFrequenzaSettimanale() +
                        (corso.getFrequenzaSettimanale() == 1 ? " lezione a settimana" : " lezioni a settimana")
        );
        freqLabel.setFont(Font.font(20));
        freqValue.setFont(Font.font(20));
        TextFlow frequenza = new TextFlow(freqLabel, freqValue);
        infoBox.getChildren().add(frequenza);

        // Costo
        Text costoLabel = new Text("Costo: ");
        costoLabel.setStyle("-fx-font-weight: bold;");
        Text costoValue = new Text(df.format(corso.getCosto()) + " €");
        costoLabel.setFont(Font.font(20));
        costoValue.setFont(Font.font(20));
        TextFlow costo = new TextFlow(costoLabel, costoValue);
        infoBox.getChildren().add(costo);

        controller.getRicetteTrattate(corso);
        corso.stampaRicette();

        if (!controller.isHomePageChef()) {
            Button subscribeButton = createSubscribeButton(controller.isAlreadyLoggedIn());
            footerVbox.getChildren().add(subscribeButton);
            VBox.setMargin(subscribeButton, new Insets(20, 0, 0, 0));
        }

        Button closeButton = createCloseButton();
        footerVbox.getChildren().add(closeButton);
        HBox.setMargin(closeButton, new Insets(0, 0, 10, 0));

        // VBox per la descrizione con scrollpane
        VBox descBox = new VBox();
        descBox.setAlignment(Pos.TOP_LEFT);

        // Titolo del corso
        Label ricetteTrattate = new Label("Ricette trattate: ");
        ricetteTrattate.setFont(Font.font("Nimbus Roman", 30));
        ricetteTrattate.setTextFill(Color.valueOf("#000000"));
        ricetteTrattate.setStyle("-fx-font-weight: bold;");
        ricetteTrattate.setAlignment(Pos.CENTER_LEFT);
        descBox.getChildren().add(ricetteTrattate);
        descBox.setMargin(ricetteTrattate, new Insets(0, 500, 10, 0));

        for (Ricetta r : corso.getRicetteTrattate()) {
            Label ricettaLabel = new Label(r.getNome());
            ricettaLabel.setFont(Font.font("Nimbus Roman", 17));
            ricettaLabel.setTextFill(Color.valueOf("#000000"));
            ricettaLabel.setAlignment(Pos.CENTER_LEFT);
            ricettaLabel.setStyle("-fx-font-weight: italic; -fx-cursor: hand;");

            // Evento click sulla label
            ricettaLabel.setOnMouseClicked(event -> {
                // Nuova finestra con descrizione
                Stage detailStage = new Stage();
                detailStage.setTitle(r.getNome());

                Text descrizioneText = new Text(r.getDescrizione());
                descrizioneText.setWrappingWidth(400);

                ScrollPane scrollPaneDesc = new ScrollPane(descrizioneText);
                scrollPaneDesc.setFitToWidth(true);

                VBox layout = new VBox(scrollPaneDesc);
                layout.setPadding(new Insets(10));

                Scene scene = new Scene(layout, 450, 300);
                detailStage.setScene(scene);
                detailStage.show();
            });

            descBox.getChildren().add(ricettaLabel);
        }

        topHbox.getChildren().add(infoBox);
        topHbox.setMargin(infoBox, new Insets(0, 0, 0, 20));
        bottomHbox.getChildren().add(descBox);
    }

    private void addImageCorso(String imagePath) {
        ImageView imageView;
        try {
            Image image = new Image(imagePath);
            imageView = new ImageView(image);

            // Dimensione quadrata desiderata
            double size = 250;

            // Calcoliamo i rapporti per ritaglio centrale
            double imgWidth = image.getWidth();
            double imgHeight = image.getHeight();

            double x = 0, y = 0, width = imgWidth, height = imgHeight;

            if (imgWidth > imgHeight) {
                // Ritaglio in larghezza
                width = imgHeight;
                x = (imgWidth - imgHeight) / 2;
            } else if (imgHeight > imgWidth) {
                // Ritaglio in altezza
                height = imgWidth;
                y = (imgHeight - imgWidth) / 2;
            }

            // Applichiamo il ritaglio
            imageView.setViewport(new javafx.geometry.Rectangle2D(x, y, width, height));
            imageView.setFitWidth(size);
            imageView.setFitHeight(size);
            imageView.setPreserveRatio(false);

            // Clip opzionale per bordi arrotondati
            Rectangle clip = new Rectangle(size, size);
            clip.setArcWidth(20);
            clip.setArcHeight(20);
            imageView.setClip(clip);

        } catch (Exception e) {
            imageView = new ImageView();
        }

        topHbox.getChildren().add(imageView);
        topHbox.setMargin(imageView, new Insets(20, 20, 0, 0)); // sposta verso il basso
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
            button.setStyle("-fx-background-color: WHITE;-fx-text-fill: \""+ color +"\"; -fx-cursor: hand;");
            button.setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,0,1,0))));
        });

        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: \""+ color +"\";-fx-text-fill: WHITE;");
            button.setBorder(null);
        });

        button.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                button.setStyle("-fx-background-color: WHITE;-fx-text-fill: \""+ color +"\"; -fx-cursor: hand;");
                button.setBorder(new Border(new BorderStroke(color, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,0,1,0))));
            } else {
                button.setStyle("-fx-background-color: \""+ color +"\";-fx-text-fill: WHITE;");
                button.setBorder(null);
            }
        });
    }
}
