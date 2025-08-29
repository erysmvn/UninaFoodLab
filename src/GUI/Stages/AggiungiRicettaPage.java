package GUI.Stages;

import Controller.Controller;
import Entity.Ricetta;
import Entity.Ingrediente;
import Entity.Enum.UnitaIngrediente;
import GUI.Buttons.CircleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;

public class AggiungiRicettaPage extends Stage {

    private final Controller controller;
    private Ricetta ricetta;

    private VBox ingredientiBox;

    public AggiungiRicettaPage(Controller controller) {
        this.controller = controller;

        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_LEFT);
        root.setStyle("-fx-background-color: white;" +
                "-fx-border-radius: 30;" +
                "-fx-background-radius: 30;" +
                "-fx-border-width: 1;" +
                "-fx-border-color: #3a6698"
        );

        Region spacer = new Region();
        spacer.setPrefHeight(30);
        VBox.setVgrow(spacer, Priority.ALWAYS);

        root.getChildren().addAll(
                createTopBox(),
                spacer,
                createRicettaBox(),
                createIngredientiSection(),
                createActionButtons()
        );

        Scene scene = new Scene(root, 650, 700);

        scene.setFill(Color.TRANSPARENT);
        this.initStyle(StageStyle.TRANSPARENT);
        this.setScene(scene);
    }


    private VBox createRicettaBox() {
        VBox box = new VBox(10);
        box.setStyle("-fx-background-color: transparent");

        box.getChildren().addAll(
                createCampoConLabel("Nome *", "Inserisci nome ricetta"),
                createCampoConLabel("Tempo di preparazione *", "Inserisci tempo"),
                createCampoConLabel("Descrizione", "Inserisci descrizione")
        );
        return box;
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


    private VBox createIngredientiSection() {
        VBox container = new VBox(10);
        container.setStyle("-fx-background-color: transparent;");

        ingredientiBox = new VBox(10);
        ingredientiBox.setPadding(new Insets(5));

        ScrollPane scroll = new ScrollPane(ingredientiBox);
        scroll.setFitToWidth(true);
        scroll.setPrefHeight(250);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        Button aggiungiIngredienteBtn = new Button("Aggiungi ingrediente");
        aggiungiIngredienteBtn.setStyle(
                "-fx-background-color: #3a6698;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-radius: 7;" +
                        "-fx-padding: 5 10 5 10;"
        );

        aggiungiIngredienteBtn.setOnAction(e -> {
            VBox singoloIngredienteBox = createIngredienteBox();
            ingredientiBox.getChildren().add(singoloIngredienteBox);
        });

        container.getChildren().addAll(aggiungiIngredienteBtn, scroll);
        return container;
    }

    private VBox createIngredienteBox() {
        VBox box = new VBox(5);
        box.setStyle("-fx-border-color: #3a6698; -fx-border-radius: 5; -fx-padding: 5;");


        ComboBox<String> ingredienteCombo = new ComboBox<>();
        ingredienteCombo.setPromptText("Seleziona ingrediente");
        ingredienteCombo.getItems().add("Nuovo Ingrediente");

        try {
            ArrayList<Ingrediente> ingredientiEsistenti = controller.getAllIngredientes();
            for (Ingrediente ing : ingredientiEsistenti) {
                ingredienteCombo.getItems().add(ing.getNome());
            }
            enableIngredienteSearch(ingredienteCombo, ingredientiEsistenti);
        } catch (Exception e) {
            e.printStackTrace();
        }


        HBox sottoBox = new HBox(10);
        TextField allergeniField = new TextField();
        allergeniField.setPromptText("Allergeni");
        TextField categoriaField = new TextField();
        categoriaField.setPromptText("Categoria");
        sottoBox.getChildren().addAll(allergeniField, categoriaField);

        HBox quantitaBox = new HBox(10);
        TextField quantitaField = new TextField();
        quantitaField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,4}?")) return change;
            return null;
        }));
        quantitaField.setPromptText("Quantità");
        quantitaField.setPrefWidth(70);

        ComboBox<UnitaIngrediente> unitaBox = new ComboBox<>();
        unitaBox.getItems().setAll(UnitaIngrediente.values());
        unitaBox.getSelectionModel().selectFirst();
        quantitaBox.getChildren().addAll(quantitaField, unitaBox);

        box.getChildren().addAll(ingredienteCombo, sottoBox, quantitaBox);
        return box;
    }

    private void enableIngredienteSearch(ComboBox<String> combo, ArrayList<Ingrediente> ingredienti) {
        ObservableList<String> originalItems = FXCollections.observableArrayList();
        originalItems.add("Nuovo Ingrediente");
        for (Ingrediente ing : ingredienti) {
            originalItems.add(ing.getNome());
        }

        combo.setEditable(true);
        combo.setItems(originalItems);

        combo.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (newText == null) return;

            ObservableList<String> filtered = FXCollections.observableArrayList();
            for (String nome : originalItems) {
                if (nome.toLowerCase().contains(newText.toLowerCase())) {
                    filtered.add(nome);
                }
            }

            combo.setItems(filtered);
            combo.show();
        });

        combo.setOnMousePressed(e -> {
            combo.setItems(originalItems);
            combo.show();
        });
    }


    private HBox createTopBox() {
        HBox topBox = new HBox(5);
        topBox.setAlignment(Pos.TOP_RIGHT);

        Label titolo = new Label("Nuova Ricetta !");
        Font robotoFont = Font.loadFont(
                getClass().getResourceAsStream("/Media/Fonts/Roboto.ttf"),
                25
        );
        titolo.setFont(robotoFont);
        titolo.setStyle("-fx-font-weight: bold; -fx-text-fill: #3A6698;");

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);

        CircleButton closeButton = createCloseButton();
        CircleButton minimizeButton = createMinimizeButton();

        topBox.getChildren().addAll(titolo, spacer1, minimizeButton, closeButton);
        return topBox;
    }

    private CircleButton createMinimizeButton() {
        CircleButton minimizeButton = new CircleButton();
        minimizeButton.setToMinimizeButtonWithAction(this);
        return minimizeButton;
    }

    private CircleButton createCloseButton() {
        CircleButton closeButton = new CircleButton();
        closeButton.setToCloseButtonWithAction(this);
        return closeButton;
    }


    private VBox createActionButtons() {
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.BOTTOM_CENTER);

        Button aggiungiBtn = new Button("Aggiungi");
        Button annullaBtn = new Button("Annulla");
        aggiungiBtn.setPrefSize(80, 30);
        annullaBtn.setPrefSize(80, 30);

        aggiungiBtn.setStyle("-fx-text-fill: white;-fx-border-radius: 7;-fx-border-width: 1; -fx-background-color: #3a6698;");
        aggiungiBtn.setOnMouseEntered(e -> aggiungiBtn.setOpacity(0.8));
        aggiungiBtn.setOnMouseExited(e -> aggiungiBtn.setOpacity(1.0));

        annullaBtn.setStyle("-fx-background-color: #da3d26;-fx-text-fill: white;-fx-border-radius: 7;-fx-border-width: 1;");
        annullaBtn.setOnMouseEntered(e -> annullaBtn.setOpacity(0.8));
        annullaBtn.setOnMouseExited(e -> annullaBtn.setOpacity(1.0));

        annullaBtn.setOnAction(e -> this.close());
        aggiungiBtn.setOnAction(event -> {
            controller.updateRicetteAggiunte(ricetta);
            this.close();
        });

        buttonBox.getChildren().addAll(aggiungiBtn, annullaBtn);
        return buttonBox;
    }
}
