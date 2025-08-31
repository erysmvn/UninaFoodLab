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
import Exception.*;


public class AggiungiRicettaPage extends Stage {

    private final Controller controller;
    private Ricetta ricetta;

    private VBox ingredientiBox;
    private TextField nomeRicettaField;
    private TextField tempoField;
    private Label erroreNomeRicettaLabel;
    private Label erroreTempoLabel;
    private Label erroreInserimentoIngredientiLabel;
    private ArrayList<VBox> ingredientiBoxList;
    private TextField descrizioneField;


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

        scene.getStylesheets().add(
                getClass().getResource("/Media/StyleSheets/fieldsAndBoxesStyle.css").toExternalForm()
        );

        this.initStyle(StageStyle.TRANSPARENT);
        this.setScene(scene);
    }


    private VBox createRicettaBox() {
        VBox box = new VBox(10);
        box.setStyle("-fx-background-color: transparent");

        VBox nomeRicetta = new VBox(5);
        nomeRicettaField = new TextField();
        nomeRicettaField.setPromptText("Inserisci nome ricetta");
        erroreNomeRicettaLabel = new Label();
        erroreNomeRicettaLabel.setTextFill(Color.RED);
        nomeRicetta.getChildren().addAll(nomeRicettaField,erroreNomeRicettaLabel);

        VBox tempoDiPreparazione = new VBox(5);
        tempoField = new TextField();
        tempoField.setPromptText("Inserisci tempo di  preparazione");
        erroreTempoLabel = new Label();
        erroreTempoLabel.setTextFill(Color.RED);
        tempoDiPreparazione.getChildren().addAll(tempoField,erroreTempoLabel);


        descrizioneField = new TextField();
        descrizioneField.setPromptText("Inserisci descrizione");

        box.getChildren().addAll(
                nomeRicetta,
                tempoDiPreparazione,
                descrizioneField
        );
        return box;
    }

    private VBox createCampoConLabel(String labelText, String prompt) {
        VBox box = new VBox(3);
        Label label = new Label(labelText);
        TextField field = new TextField();
        field.setPromptText(prompt);
        box.getChildren().addAll(label, field);
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
        ingredientiBoxList = new ArrayList<>();
        aggiungiIngredienteBtn.setOnAction(e -> {
            VBox singoloIngredienteBox = createIngredienteBox();
            ingredientiBox.getChildren().add(singoloIngredienteBox);
            ingredientiBoxList.add(singoloIngredienteBox);
        });
        erroreInserimentoIngredientiLabel = new Label();
        erroreInserimentoIngredientiLabel.setTextFill(Color.RED);
        container.getChildren().addAll(aggiungiIngredienteBtn, erroreInserimentoIngredientiLabel,scroll);
        return container;
    }

    private VBox createIngredienteBox() {
        VBox box = new VBox(5);
        box.setStyle("-fx-border-color: #3a6698; -fx-border-radius: 5; -fx-padding: 5;");


        ComboBox<String> ingredienteCombo = new ComboBox<>();
        ingredienteCombo.setPromptText("Seleziona ingrediente");
        ingredienteCombo.getItems().add("Nuovo Ingrediente");

        try {
            ArrayList<Ingrediente> ingredientiEsistenti = controller.getAllIngredienti();
            for (Ingrediente ing : ingredientiEsistenti) {
                ingredienteCombo.getItems().add(ing.getNome());
            }
            enableIngredienteSearch(ingredienteCombo, ingredientiEsistenti);
        } catch (Exception ignora) {}


        TextField nuovoIngredienteField = new TextField();
        nuovoIngredienteField.setPromptText("Nome nuovo ingrediente");
        nuovoIngredienteField.setVisible(false);

        ingredienteCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("Nuovo Ingrediente".equals(newVal)) {
                nuovoIngredienteField.setVisible(true);
            } else {
                nuovoIngredienteField.setVisible(false);
            }
        });

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


        box.getChildren().addAll(ingredienteCombo, nuovoIngredienteField, sottoBox, quantitaBox);
        return box;
    }

    private Ricetta createRicetta() throws Exception {
        Ricetta ricetta = new Ricetta();

        String nome = nomeRicettaField.getText();
        String tempoDiPreparazione = tempoField.getText();
        String descrizione = descrizioneField.getText();
        if(nome.isEmpty())
            throw new NomeRicettaEmptyException();
        if(tempoDiPreparazione.isEmpty())
            throw new TempoDiPreparazioneEmptyException();

        ricetta.setNomeRicetta(nome);
        ricetta.setTempoPreparazione(Integer.parseInt(tempoDiPreparazione));

        if(descrizione.isEmpty())
            ricetta.setDescrizione("no description");

        ArrayList<Ingrediente> ingredienti = new ArrayList<>();
        for (VBox ingBox : ingredientiBoxList) {
            ingredienti.add(getIngredienteFromBox(ingBox));
        }

        controller.insertIngredienti(ingredienti);

        if(ingredienti.isEmpty())
            throw new AlmenoUnIngredienteException();

        ricetta.setIngredienti(ingredienti);
        return ricetta;
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
            try {
                erroreTempoLabel.setText("");
                erroreNomeRicettaLabel.setText("");
                erroreInserimentoIngredientiLabel.setText("");
                ricetta = createRicetta();
                controller.updateRicetteAggiunte(ricetta);
                this.close();
            } catch (NomeIngredienteEmptyException NIEE) {
                erroreInserimentoIngredientiLabel.setText("Inserire nome ingrediente");
            }catch (QuantitaEmptyException QEE){
                erroreInserimentoIngredientiLabel.setText("Inserire quantità");
            }catch (NomeRicettaEmptyException NIEE){
                erroreNomeRicettaLabel.setText("Inserire nome ricetta");
            } catch (TempoDiPreparazioneEmptyException TPEE) {
                erroreTempoLabel.setText("Inserire tempo preparazione");
            } catch (AlmenoUnIngredienteException AIE) {
                erroreInserimentoIngredientiLabel.setText("Inserire almeno ingrediente");
            } catch (CategoriaEmptyException CEE) {
                erroreInserimentoIngredientiLabel.setText("Inserire categoria ingrediente");
            }catch (Exception e) {
                erroreInserimentoIngredientiLabel.setText("Errore nell'inserimento dati. Riprovare più tardi");
                e.printStackTrace();
            }
        });

        buttonBox.getChildren().addAll(aggiungiBtn, annullaBtn);
        return buttonBox;
    }


    private Ingrediente getIngredienteFromBox(VBox box) {
        @SuppressWarnings("unchecked")
        ComboBox<String> ingredienteCombo = (ComboBox<String>) box.getChildren().get(0);
        TextField nuovoIngredienteField = (TextField) box.getChildren().get(1);

        HBox sottoBox = (HBox) box.getChildren().get(2);
        TextField allergeniField = (TextField) sottoBox.getChildren().get(0);
        TextField categoriaField = (TextField) sottoBox.getChildren().get(1);

        HBox quantitaBox = (HBox) box.getChildren().get(3);
        TextField quantitaField = (TextField) quantitaBox.getChildren().get(0);
        @SuppressWarnings("unchecked")
        ComboBox<UnitaIngrediente> unitaBox = (ComboBox<UnitaIngrediente>) quantitaBox.getChildren().get(1);

        // Nome ingrediente (esistente o nuovo)
        String nome = ingredienteCombo.getValue();
        if ("Nuovo Ingrediente".equals(nome)) {
            String typed = (nuovoIngredienteField != null && nuovoIngredienteField.isVisible())
                    ? nuovoIngredienteField.getText()
                    : ingredienteCombo.getEditor().getText();
            nome = typed;
        }
        if (nome == null || nome.trim().isEmpty()) throw new NomeIngredienteEmptyException();

        String allergeni = allergeniField.getText();
        if (allergeni == null || allergeni.isBlank()) allergeni = "Nessuno";

        String categoria = categoriaField.getText();
        if (categoria == null || categoria.isBlank()) throw new CategoriaEmptyException();

        String qText = quantitaField.getText();
        if (qText == null || qText.isBlank()) throw new QuantitaEmptyException();
        final int quantita;
        try { quantita = Integer.parseInt(qText.trim()); }
        catch (NumberFormatException ex) { throw new QuantitaEmptyException(); }

        UnitaIngrediente unita = unitaBox.getValue();
        if (unita == null) unita = UnitaIngrediente.Quantita;

        return new Ingrediente(nome.trim(), allergeni.trim(), categoria.trim(), quantita, unita);
    }

/*

FATTO -> INSERT INGREDIENTE
AL METODO DEVO PASSARGLI RICETTA E INGREDIENTE

BEGIN;
INSERT INTO ricetta (nome_ricetta, descrizione_ricetta, tempo_di_preparazione, autore) values ('Pollo in Umido', 'no decription', 20, null);
INSERT INTO FORMA (idRicetta, idIngrediente, Unità, Quantità)
SELECT r.idRicetta, i.idIngrediente, 'Grammi (gr)', 150
FROM RICETTA r, INGREDIENTE i
WHERE r.Nome_ricetta = 'Pollo in Umido' AND i.Nome_ingrediente = 'Pollo';
END;

DOPO FARE QUESTA->
METODO TRANSACTION PER INSERIRE A SESSIONE
METODO INSERT RICETTATOSESSIONE()
INSERT INTO Tratta (idricetta, idsessione) VALUES (
(SELECT idricetta FROM ricetta WHERE nome_ricetta = 'Di pinto scicchitano'),
(SELECT idsessione FROM sessione NATURAL JOIN corso WHERE nome_corso = 'Primi di mare' AND data = '2026-06-06')

);

 */



}
