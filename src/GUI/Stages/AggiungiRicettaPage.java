package GUI.Stages;

import GUI.Buttons.CircleButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AggiungiRicettaPage extends Stage {

    public AggiungiRicettaPage() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_LEFT);
        root.setStyle("-fx-background-color: white;" +
                "-fx-border-radius: 30;" +
                "-fx-background-radius: 30;" +
                "-fx-border-width: 1;" +
                "-fx-border-color: #3a6698"
        );

        VBox ricettaBox = new VBox(10);
        ricettaBox.getChildren().add(createCampoConLabel("Nome *", "Inserisci nome ricetta"));
        ricettaBox.getChildren().add(createCampoConLabel("Tempo di preparazione *", "Inserisci tempo"));
        ricettaBox.getChildren().add(createCampoConLabel("Descrizione", "Inserisci descrizione"));
        ricettaBox.setStyle("-fx-background-color: transparent");

        VBox ingredientiBox = new VBox(10);
        ingredientiBox.getChildren().add(createCampoConLabel("Nome ingrediente", "Inserisci nome ingrediente"));
        ingredientiBox.getChildren().add(createCampoConLabel("Allergeni", "Inserisci allergeni"));
        ingredientiBox.getChildren().add(createCampoConLabel("Categoria", "Inserisci categoria"));
        ingredientiBox.setStyle("-fx-background-color: transparent");
        Region spacer = new Region();
        spacer.setPrefHeight(30);
        spacer.setMinHeight(30);
        root.getChildren().addAll(createTopBox(),spacer,ricettaBox, ingredientiBox,createActionButtons());

        Scene scene = new Scene(root, 500, 700);
        scene.setFill(Color.TRANSPARENT);
        this.initStyle(StageStyle.TRANSPARENT);
        this.setScene(scene);
    }

    private VBox createCampoConLabel(String labelText, String prompt) {
        VBox box = new VBox(3);
        Label label = new Label(labelText);
        TextField field = new TextField();
        field.setPromptText(prompt);

        Label erroreLabel = new Label();
        erroreLabel.setTextFill(Color.RED);

        box.getChildren().addAll(label, field, erroreLabel);
        return box;
    }

    private HBox createTopBox(){
        HBox topBox = new HBox(5);
        topBox.setAlignment(Pos.TOP_RIGHT);
        topBox.setSpacing(5);
        Label titolo = new Label("Nuova Ricetta !");

        Font robotoFont = Font.loadFont(
                getClass().getResourceAsStream("/Media/Fonts/Roboto.ttf"),
                25
        );

        titolo.setFont(robotoFont);
        titolo.setStyle("-fx-font-weight: bold; -fx-text-fill: #3A6698;-fx-alignment: CENTER;-fx-background-color: transparent;");


        Region spacer1 =  new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        CircleButton closeButton = createCloseButton();
        CircleButton minimizeButton = createMinimizeButton();
        topBox.getChildren().addAll(titolo,spacer1,minimizeButton, closeButton);
        return topBox;
    }

    private CircleButton createMinimizeButton() {
        CircleButton minimizeButton = new CircleButton();
        minimizeButton.setToMinimizeButtonWithAction(this);
        return minimizeButton;
    }

    private CircleButton createCloseButton() {
        CircleButton minimizeButton = new CircleButton();
        minimizeButton.setToCloseButtonWithAction(this);
        return minimizeButton;
    }

    private VBox createActionButtons() {
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        Button aggiungiBtn = new Button("Aggiungi");
        Button annullaBtn = new Button("Annulla");
        aggiungiBtn.setPrefSize(80,30);
        aggiungiBtn.setMinSize(80,30);
        annullaBtn.setPrefSize(80,30);
        annullaBtn.setMinSize(80,30);
        aggiungiBtn.setStyle("-fx-text-fill: white;-fx-border-radius: 7;-fx-border-width: 1; -fx-background-color: #3a6698;");
        aggiungiBtn.setOnMouseEntered(e->aggiungiBtn.setOpacity(0.8));
        aggiungiBtn.setOnMouseExited(e->aggiungiBtn.setOpacity(1.0));

        annullaBtn.setStyle("-fx-background-color: #da3d26;-fx-text-fill: white;-fx-border-radius: 7;-fx-border-width: 1;");
        annullaBtn.setOnMouseEntered(e->annullaBtn.setOpacity(0.8));
        annullaBtn.setOnMouseExited(e->annullaBtn.setOpacity(1.0));

        annullaBtn.setOnAction(e -> this.close());
        aggiungiBtn.setOnAction(event ->{//TODO});
        });

        buttonBox.getChildren().addAll(aggiungiBtn,annullaBtn);

        return buttonBox;
    }

}
