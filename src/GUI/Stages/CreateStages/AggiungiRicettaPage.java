package GUI.Stages.CreateStages;

import Controller.Controller;
import Entity.IngredienteFormaRicetta;
import Entity.Ricetta;
import Entity.Ingrediente;
import Entity.Enum.UnitaIngrediente;
import Exception.IngredienteExceptions.CategoriaEmptyException;
import Exception.IngredienteExceptions.NomeIngredienteEmptyException;
import Exception.IngredienteExceptions.QuantitaEmptyException;
import Exception.RicettaExceptions.AlmenoUnIngredienteException;
import Exception.RicettaExceptions.NomeRicettaEmptyException;
import Exception.RicettaExceptions.TempoDiPreparazioneEmptyException;
import GUI.Buttons.CircleButton;
import GUI.Buttons.MyButton;
import GUI.Stages.MyStage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.ArrayList;


public class AggiungiRicettaPage extends MyStage {
    private final Controller controller;
    private Ricetta ricetta;

    private Stage caller;
    private VBox root;

    private VBox ingredientiBox;

    private TextField nomeRicettaField;
    private TextField tempoField;
    private TextField descrizioneField;

    private Label erroreNomeRicettaLabel;
    private Label erroreTempoLabel;
    private Label erroreInserimentoIngredientiLabel;

    private ArrayList<VBox> ingredientiBoxList;
    private UnitaIngrediente unita;
    private int quantita;

    public AggiungiRicettaPage(Controller controller,Stage caller) {
        super(450, 700, RootType.VBOX);
        this.controller = controller;
        this.caller = caller;

        root = getRootVBox();

        root.setSpacing(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_LEFT);

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

        this.addStylesheet("/Media/StyleSheets/fieldsAndBoxesStyle.css");
    }

    private VBox createRicettaBox() {
        VBox box = new VBox(10);
        box.setStyle("-fx-background-color: transparent");

        VBox nomeRicettaBox = new VBox(5);
        Label metaNomeRicetta = new Label("Nome *");
        nomeRicettaField = new TextField();
        nomeRicettaField.setPromptText("Inserisci nome ricetta");
        nomeRicettaField.setMaxWidth(300);
        nomeRicettaField.setMinWidth(300);
        erroreNomeRicettaLabel = new Label();
        erroreNomeRicettaLabel.setTextFill(Color.RED);
        nomeRicettaBox.getChildren().addAll(metaNomeRicetta,nomeRicettaField,erroreNomeRicettaLabel);

        VBox tempoDiPreparazioneBox = new VBox(5);
        Label metaTempo = new Label("Tempo di preparazione *");
        tempoField = new TextField();
        tempoField.setPromptText("Inserisci tempo di  preparazione (min)");
        tempoField.setMaxWidth(150);
        tempoField.setMinWidth(150);
        tempoField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,3}?")) return change;
            return null;
        }));
        erroreTempoLabel = new Label();
        erroreTempoLabel.setTextFill(Color.RED);
        tempoDiPreparazioneBox.getChildren().addAll(metaTempo,tempoField,erroreTempoLabel);

        VBox descrizioneBox = new VBox(5);
        descrizioneField = new TextField();
        descrizioneField.setPromptText("Inserisci descrizione");
        Label metaDescrizione = new Label("Descrizione");
        descrizioneField.setAlignment(Pos.TOP_LEFT);
        descrizioneField.setPrefHeight(150);
        descrizioneField.setMinHeight(150);
        descrizioneBox.getChildren().addAll(metaDescrizione,descrizioneField);
        box.getChildren().addAll(
                nomeRicettaBox,
                tempoDiPreparazioneBox,
                descrizioneBox
        );
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

        MyButton aggiungiIngredienteBtn = new MyButton("Aggiungi ingrediente", MyButton.ButtonType.PRIMARY);
        aggiungiIngredienteBtn.setSize(200, 40);

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

        MyButton removeButton = new MyButton("x", MyButton.ButtonType.SECONDARY);

        HBox nomeBox = new HBox(5);
        nomeBox.getChildren().addAll(ingredienteCombo, nuovoIngredienteField, removeButton);
        HBox.setHgrow(ingredienteCombo, Priority.ALWAYS);
        HBox.setHgrow(nuovoIngredienteField, Priority.ALWAYS);

        removeButton.setOnAction(e -> {
            ingredientiBox.getChildren().remove(box);
            ingredientiBoxList.remove(box);
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


        ingredienteCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if ("Nuovo Ingrediente".equals(newVal)) {
                nuovoIngredienteField.setVisible(true);
                allergeniField.setDisable(false);
                categoriaField.setDisable(false);
            } else {
                nuovoIngredienteField.setVisible(false);
                allergeniField.setDisable(true);
                categoriaField.setDisable(true);
            }

        });

        ComboBox<UnitaIngrediente> unitaBox = new ComboBox<>();
        unitaBox.getItems().setAll(UnitaIngrediente.values());
        unitaBox.getSelectionModel().selectFirst();
        quantitaBox.getChildren().addAll(quantitaField, unitaBox);

        box.getChildren().addAll(nomeBox, sottoBox, quantitaBox);
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

        if(descrizione.isEmpty()) ricetta.setDescrizione("no description");
        else ricetta.setDescrizione(descrizione);

        ricetta.setNomeRicetta(nome);
        ricetta.setTempoPreparazione(Integer.parseInt(tempoDiPreparazione));

        Ingrediente ingrediente;
        for (VBox ingBox : ingredientiBoxList){
            ingrediente = getIngredienteFromBox(ingBox);
            controller.insertIngredienti(ingrediente);
            ricetta.addIngredienteFormaRicetta(getIngredienteFormaRicettaBox(ingrediente, ingBox));
        }

        if(ingredientiBoxList.isEmpty())
            throw new AlmenoUnIngredienteException();

        return ricetta;
    }

    private void enableIngredienteSearch(ComboBox<String> combo, ArrayList<Ingrediente> ingredienti) {
        ObservableList<String> originalItems = FXCollections.observableArrayList();
        originalItems.add("Nuovo Ingrediente");
        for (Ingrediente ing : ingredienti) {
            originalItems.add(ing.getNome());
        }

        FilteredList<String> filteredItems = new FilteredList<>(originalItems, s -> true);
        combo.setItems(filteredItems);

        combo.setEditable(true);

        combo.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) return;

            filteredItems.setPredicate(item -> {
                if (item == null) return false;
                return item.toLowerCase().contains(newValue.toLowerCase());
            });


            combo.show();
        });

        combo.setOnMouseClicked(e -> {
            filteredItems.setPredicate(s -> true);
        });
    }

    private HBox createTopBox() {
        HBox topBox = new HBox(5);
        topBox.setAlignment(Pos.TOP_RIGHT);

        Label titolo = new Label("Nuova Ricetta !");
        titolo.setStyle("-fx-font-weight: bold; -fx-text-fill: #3A6698;-fx-font-size: 25;");

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

        MyButton aggiungiBtn = new MyButton("Aggiungi", MyButton.ButtonType.PRIMARY);
        MyButton annullaBtn = new MyButton("Annulla", MyButton.ButtonType.SECONDARY);

        aggiungiBtn.setSize(80, 30);
        annullaBtn.setSize(80, 30);


        annullaBtn.setOnAction(e -> this.close());
        aggiungiBtn.setOnAction(event -> {
            try {
                erroreTempoLabel.setText("");
                erroreNomeRicettaLabel.setText("");
                erroreInserimentoIngredientiLabel.setText("");
                ricetta = createRicetta();
                controller.updateRicetteAggiunte(ricetta,caller);
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
                showDialog("Errore di sistema. Riprovare più tardi");
            }
        });

        buttonBox.getChildren().addAll(aggiungiBtn, annullaBtn);
        return buttonBox;
    }

    private IngredienteFormaRicetta getIngredienteFormaRicettaBox(Ingrediente ingrediente, VBox ingBox) {

        HBox quantitaBox = (HBox) ingBox.getChildren().get(2);
        TextField quantitaField = (TextField) quantitaBox.getChildren().getFirst();

        @SuppressWarnings("unchecked")
        ComboBox<UnitaIngrediente> unitaBox = (ComboBox<UnitaIngrediente>) quantitaBox.getChildren().get(1);

        String qText = quantitaField.getText();
        if (qText == null || qText.isBlank()) throw new QuantitaEmptyException();

        try { quantita = Integer.parseInt(qText.trim()); }
        catch (NumberFormatException ex) { throw new QuantitaEmptyException(); }

        unita = unitaBox.getValue();
        if (unita == null) unita = UnitaIngrediente.Quantita;

        return new IngredienteFormaRicetta(ingrediente,ricetta,quantita,unita);

    }

    private Ingrediente getIngredienteFromBox(VBox ingBox){
        HBox nomeBox = (HBox) ingBox.getChildren().getFirst();
        @SuppressWarnings("unchecked")
        ComboBox<String> ingredienteCombo = (ComboBox<String>) nomeBox.getChildren().getFirst();
        TextField nuovoIngredienteField = (TextField) nomeBox.getChildren().get(1);

        HBox sottoBox = (HBox) ingBox.getChildren().get(1);
        TextField allergeniField = (TextField) sottoBox.getChildren().getFirst();
        TextField categoriaField = (TextField) sottoBox.getChildren().get(1);


        String nome = ingredienteCombo.getValue();
        String categoria = categoriaField.getText();
        if ("Nuovo Ingrediente".equals(nome)) {
             nome = (nuovoIngredienteField != null && nuovoIngredienteField.isVisible())
                    ? nuovoIngredienteField.getText()
                    : ingredienteCombo.getEditor().getText();
            if (categoria == null || categoria.isBlank()) throw new CategoriaEmptyException();
        }
        if (nome == null || nome.trim().isEmpty()) throw new NomeIngredienteEmptyException();

        String allergeni = allergeniField.getText();
        if (allergeni == null || allergeni.isBlank()) allergeni = "Nessuno";


        return new Ingrediente(nome.trim(), allergeni.trim(), categoria.trim());
    }

    public Stage getCaller(){
        return caller;
    }

}
