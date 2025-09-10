package GUI.Stages.ClassDataStages;

import Controller.Controller;
import Entity.*;

import Exception.CorsoExceptions.imageNotFoundException;
import GUI.Buttons.MyButton;
import GUI.Stages.MyStage;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.InputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class CorsoPage extends MyStage {
    private Controller controller;
    private Corso corso;

    private VBox root;
    private HBox topHbox;
    private HBox bottomHbox;
    private VBox footerVbox;

    public CorsoPage(Controller controller){
        super(900, 750, RootType.VBOX);
        this.controller = controller;

        root = getRootVBox();
        root.setSpacing(15);
        root.setAlignment(Pos.TOP_CENTER);

        topHbox = new HBox(15);
        topHbox.setPadding(new Insets(50, 0, 10, 0));
        topHbox.setAlignment(Pos.TOP_CENTER);
        topHbox.setSpacing(40);
        topHbox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        bottomHbox = new HBox(15);
        bottomHbox.setPadding(new Insets(0, 0, 0, 0));
        bottomHbox.setAlignment(Pos.TOP_CENTER);
        bottomHbox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        footerVbox = new VBox(15);
        footerVbox.setPadding(new Insets(0, 0, 50, 0));
        footerVbox.setAlignment(Pos.BOTTOM_CENTER);
        footerVbox.setSpacing(20);
        footerVbox.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        root.getChildren().addAll(topHbox, bottomHbox, spacer, footerVbox);
    }

    public void initPage(Corso corso){
        this.corso = corso;

        VBox infoBox = new VBox(10);
        infoBox.setAlignment(Pos.TOP_RIGHT);

        VBox imgBox = new VBox(10);
        infoBox.setAlignment(Pos.TOP_LEFT);

        addImageCorso(corso.getImagePath(), imgBox);

        this.buildInfoBox(infoBox);

        try {
            controller.getRicetteTrattate(corso);
        } catch (SQLException e) {
            showDialog("Errore di sistema. Riprovare più tardi");
        }

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
        HBox.setMargin(imgBox, new Insets(20, 0, 0, 0));
        bottomHbox.getChildren().add(descBox);
    }

    private void addImageCorso(String imagePath, VBox imgBox) {
            ImageView imageView;
            Image image;
            try {
                
                InputStream is = getClass().getResourceAsStream(imagePath);
                if (is == null)
                    throw new imageNotFoundException("/Media/Background/biancoNormale.png");

                image = new Image(is);
                imageView = new ImageView(image);


            }catch (imageNotFoundException IMNF) {
                imagePath = IMNF.getMessage();
                image = new Image(imagePath);
                imageView = new ImageView(image);
            }

            setImageShape(imageView,image);
            imgBox.getChildren().add(imageView);
        }

   private void setImageShape(ImageView imageView, Image image) {
       double size = 250;

       double imgWidth = image.getWidth();
       double imgHeight = image.getHeight();

       double puntoInizioRitaglioX = 0, puntoInizioRitaglioY = 0;

       double latoCorto = 0;

       if (imgWidth >= imgHeight) {
           // width = imgHeight;
           latoCorto = imgHeight;
           puntoInizioRitaglioX = (imgWidth - imgHeight) / 2;
       } else if (imgHeight > imgWidth) {
           // height = imgWidth;
           latoCorto = imgWidth;
           puntoInizioRitaglioY = (imgHeight - imgWidth) / 2;
       }
                imageView.setViewport(new Rectangle2D(puntoInizioRitaglioX, puntoInizioRitaglioY, latoCorto, latoCorto));
                imageView.setFitWidth(size);
                imageView.setFitHeight(size);
                imageView.setPreserveRatio(false);

                Rectangle clip = new Rectangle(size, size);
                clip.setArcWidth(20);
                clip.setArcHeight(20);
                imageView.setClip(clip);

   }

    public Corso getCorso() {
        return corso;
    }

    private MyButton createSubscribeButton(boolean isLoggedIn) {
        MyButton subscribeButton = new MyButton("Iscriviti", MyButton.ButtonType.PRIMARY);

        subscribeButton.setOnAction(event -> {
            try {
                if (!isLoggedIn) {
                    controller.openLoginPage();
                    this.close();
                } else {
                    controller.subscribeToCourse(corso);
                    setIscrittoCorso(subscribeButton);
                    showSuccessDialog();
                }
            } catch (SQLException sqle) {
                showDialog("Errore di sistema. Riprovare più tardi");
            }
        });

        try {
            if (controller.alreadySubscribed(corso))
                setIscrittoCorso(subscribeButton);

        } catch (SQLException e) {
            showDialog("Errore di sistema. Riprovare più tardi");
        }
        return subscribeButton;
    }

    private void showSuccessDialog() {
        Stage dialog = createSuccessDialog();
        dialog.show();
        controller.refreshAccountPage();
        new Thread(() -> {
            try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            Platform.runLater(dialog::close);
        }).start();
    }

    private Stage createSuccessDialog() {
        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.initOwner(this);
        Label label = new Label("Iscritto con successo !\nPer i pagamenti: segrepass->Pagamenti in debito");
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-background-color: rgba(128,128,128,0.8); -fx-padding: 20px; -fx-background-radius: 10;");
        label.setFont(Font.font("System", FontWeight.BOLD, 16));
        StackPane pane = new StackPane(label);
        pane.setPrefSize(500, 220);
        pane.setStyle("-fx-background-color: transparent;");
        Scene scene = new Scene(pane);
        scene.setFill(Color.TRANSPARENT);

        dialog.setScene(scene);
        return dialog;
    }

    private void setIscrittoCorso(MyButton subscribeButton) {
        subscribeButton.setText("Iscritto");
        subscribeButton.setDisabledStyle();
    }

    private MyButton createCloseButton() {
        MyButton closeButton = new MyButton("Chiudi", MyButton.ButtonType.SECONDARY);

        closeButton.setOnAction(e -> this.close());

        return closeButton;
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

        try {
            controller.setChefs(corso);
        } catch (SQLException sqle) {
            // TODO Dialog
        }

        Text chefLabel = new Text("Chef: ");
        chefLabel.setStyle("-fx-font-weight: bold;");
        Text chefValue = new Text(corso.getStringOfChefs());
        setAndAddFont(infoBox, chefLabel, chefValue);


        Text modalitaLabel = new Text("Modalità: ");
        modalitaLabel.setStyle("-fx-font-weight: bold;");
        Text modalitaValue = new Text();
        if (corso.getModalita_corso() != null) { // perche da online di default ??
            modalitaValue.setText(corso.getModalita_corso().getLabel());
        } else {
            modalitaValue.setText("Da definire");
        }
        setAndAddFont(infoBox, modalitaLabel, modalitaValue);


        Text diffLabel = new Text("Difficoltà: ");
        diffLabel.setStyle("-fx-font-weight: bold;");
        Text diffValue = new Text(corso.getDifficolta().toString());
        setAndAddFont(infoBox, diffLabel, diffValue);


        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Text periodoLabel = new Text("Periodo: ");
        periodoLabel.setStyle("-fx-font-weight: bold;");
        Text periodoValue = new Text();
        if (corso.getDataInizio() != null && corso.getDataFine() != null) {
            periodoValue.setText(sdf.format(corso.getDataInizio()) + " - " + sdf.format(corso.getDataFine()));
        } else {
            periodoValue.setText("Da definire");
        }
        setAndAddFont(infoBox, periodoLabel, periodoValue);

        // Ore totali
        DecimalFormat df = new DecimalFormat("#.##");
        Text oreLabel = new Text("Ore totali: ");
        oreLabel.setStyle("-fx-font-weight: bold;");
        Text oreValue = new Text();
        if (corso.getOreTotali() > 0) {
            oreValue.setText(df.format(corso.getOreTotali()));
        } else {
            oreValue.setText("Da definire");
        }
        setAndAddFont(infoBox, oreLabel, oreValue);


        Text freqLabel = new Text("Frequenza: ");
        freqLabel.setStyle("-fx-font-weight: bold;");
        Text freqValue = new Text(
                corso.getFrequenzaSettimanale() +
                        (corso.getFrequenzaSettimanale() == 1 ? " lezione a settimana" : " lezioni a settimana")
        );
        setAndAddFont(infoBox, freqLabel, freqValue);

        // Costo
        Text costoLabel = new Text("Costo: ");
        costoLabel.setStyle("-fx-font-weight: bold;");
        Text costoValue = new Text(df.format(corso.getCosto()) + " €");
        setAndAddFont(infoBox, costoLabel, costoValue);
    }

    static void setAndAddFont(VBox infoBox, Text costoLabel, Text costoValue) {
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
        ricetteTrattate.setFont(Font.font(32));
        ricetteTrattate.setTextFill(Color.BLACK);
        ricetteTrattate.setStyle("-fx-font-weight: bold;");
        ricetteTrattate.setAlignment(Pos.CENTER_LEFT);

        descBox.getChildren().add(ricetteTrattate);
        VBox.setMargin(ricetteTrattate, new Insets(0, 500, 10, 0));

        VBox ricetteList = new VBox(5);
        ricetteList.setAlignment(Pos.TOP_LEFT);

        if (!corso.getRicetteTrattate().isEmpty()) {
            for (Ricetta r : corso.getRicetteTrattate()) {
                createRicettaLabel(ricetteList, r);
            }
        } else {
            Label noRicetteTrattate = new Label("Ancora nessuna ricetta");
            noRicetteTrattate.setFont(Font.font(28));
            noRicetteTrattate.setTextFill(Color.WHITE);
            noRicetteTrattate.setAlignment(Pos.CENTER);
            ricetteList.getChildren().add(noRicetteTrattate);
        }

        ScrollPane scrollPane = new ScrollPane(ricetteList);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        scrollPane.setStyle("-fx-background: white; -fx-background-color: white;");
        ricetteList.setStyle("-fx-background-color: white;");

        scrollPane.setPrefHeight(250);
        scrollPane.setMaxHeight(250);

        descBox.getChildren().add(scrollPane);
    }

    private void createRicettaLabel(VBox ricetteList, Ricetta ricetta) {
        Label ricettaLabel = new Label("•" + ricetta.getNome());
        ricettaLabel.setFont(Font.font(17));
        ricettaLabel.setTextFill(Color.valueOf("#000000"));
        ricettaLabel.setAlignment(Pos.CENTER_LEFT);
        ricettaLabel.setStyle("-fx-cursor: hand;");

        ricettaLabel.setOnMouseClicked(event -> controller.openRicettaPage(ricetta));

        ricetteList.getChildren().add(ricettaLabel);
    }
}
