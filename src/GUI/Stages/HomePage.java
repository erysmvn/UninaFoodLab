package GUI.Stages;

import Controller.*;
import Entity.*;
import Exception.CorsoExceptions.corsiNotFoundException;
import GUI.Pane.*;
import GUI.Buttons.*;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

public class HomePage extends Stage {
    private Controller controller;
    private ArrayList<Corso> corsi;
    private Utente utente = null;

    private BorderPane root;
    private Scene scene;
    private Cursor originalCursor;

    private Boolean isLoggedIn = false;
    private Boolean isChef = false;
    private boolean mostraTuttiCorsiClicked = false;

    private HBox homeButtons;
    private HBox corsiBox;
    private HBox choiceBox;
    private ScrollPane corsiScrollPane;

    private TextField searchField;

    private Button searchButton;
    private Button tuttiCorsi;
    private Button homeButton;
    private Button costoFilterUp;
    private Button costoFilterDown;

    ToggleGroup choiceGroup;
    ToggleButton corsoChoice;
    ToggleButton chefChoice;
    ToggleButton tipologiaChoice;

    private enum Modalita {
        ONLINE, PRESENZA
    }

    private Modalita modalitaSelezionata = null;

    private enum PriceFilter {
        CRESCENTE, DECRESCENTE
    }

    private PriceFilter priceFilter = null;

    public HomePage(Controller controller) {
        this.controller = controller;
        setFunctionalitiesHomePage();
        setAestheticsHomePage();
        this.setCTRLW();
        this.originalCursor = scene.getCursor();
    }

    private void setFunctionalitiesHomePage() {
        root = new BorderPane();
        root.setTop(createTopBar());
        root.setCenter(createCenterContent());
    }

    private void setCTRLW() {
        scene.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.W) {
                controller.closeApp();
            }
        });
    }

    private void setAestheticsHomePage() {
        this.initStyle(StageStyle.UNDECORATED);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        this.setX(screenBounds.getMinX());
        this.setY(screenBounds.getMinY());
        this.setWidth(screenBounds.getWidth());
        this.setHeight(screenBounds.getHeight());

        scene = new Scene(root, 800, 600);
        this.setScene(scene);
    }

    private void setTopBarAesthetics(VBox topBar) {
        topBar.setPadding(new Insets(10));
        topBar.setSpacing(20);
        topBar.setStyle("-fx-background-color: WHITE;");
        topBar.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(0, 0, 2, 0)
        )));
    }

    private BorderPane createCloseAndMinimizePane() {
        BorderPane closeAndMinimizePane = new BorderPane();
        HBox controlButtons = new HBox(5);
        controlButtons.setAlignment(Pos.TOP_RIGHT);

        CircleButton minimizeBtn = new CircleButton();
        CircleButton closeBtn = new CircleButton();

        minimizeBtn.setToMinimizeButtonWithAction(this);
        closeBtn.setToCloseButtonWithAction(e -> controller.closeApp());


        controlButtons.getChildren().addAll(
                minimizeBtn, closeBtn
        );
        closeAndMinimizePane.setRight(controlButtons);
        return closeAndMinimizePane;
    }

    private ImageView createLogoView() {
        Image logoImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Media/Logos/LogoHomePage.png")));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(600);
        logoView.setFitHeight(600);
        logoView.setPreserveRatio(true);
        return logoView;
    }

    private VBox createTopBar() {
        VBox topBar = new VBox();
        this.setTopBarAesthetics(topBar);

        BorderPane closeAndMinimizePane = createCloseAndMinimizePane();

        homeButtons = new HBox(20);
        homeButtons.setAlignment(Pos.CENTER);
        homeButtons.getChildren().add(createLoginButton());

        topBar.getChildren().addAll(closeAndMinimizePane, createLogoView(), homeButtons);
        topBar.setAlignment(Pos.TOP_CENTER);

        return topBar;
    }

    private Button createLoginButton() {
        Button loginButton = new Button("Login");

        Image loginImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Media/Icons/LoginImage.png")));
        ImageView loginView = new ImageView(loginImage);
        loginView.setFitWidth(30);
        loginView.setFitHeight(30);
        loginButton.setGraphic(loginView);
        loginButton.setContentDisplay(ContentDisplay.LEFT);
        loginButton.setGraphicTextGap(10);

        Font robotoFont = Font.loadFont(
                getClass().getResourceAsStream("/Media/Fonts/Roboto.ttf"),
                20
        );
        loginButton.setFont(robotoFont);

        loginButton.setStyle("-fx-border-color: #3a6698; -fx-border-width: 1px; -fx-border-radius: 30px; -fx-background-color: white; -fx-text-fill: #3a6698; -fx-cursor: hand;");

        loginButton.setOnAction(e -> {
            controller.openLoginPage();
        });
        return loginButton;
    }

    private void createHomeButton() {

        homeButton = new Button("Home");

        Button accountButton = new Button();
        Image houseImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Media/Icons/homeLogo.png")));
        ImageView houseView = new ImageView(houseImage);

        houseView.setFitHeight(30);
        houseView.setFitWidth(30);

        accountButton.setStyle("-fx-border-color: #3a6698; -fx-border-width: 1px; -fx-border-radius: 30px; -fx-background-color: white; -fx-cursor: hand;");

        homeButton.setGraphic(houseView);
        homeButton.setContentDisplay(ContentDisplay.LEFT);
        homeButton.setGraphicTextGap(10);

        Font robotoFont = Font.loadFont(
                getClass().getResourceAsStream("/Media/Fonts/Roboto.ttf"),
                20
        );
        homeButton.setFont(robotoFont);
        homeButton.setStyle("-fx-border-color: #3a6698; -fx-border-width: 1px; -fx-border-radius: 30px; -fx-background-color: white; -fx-text-fill: #3a6698; -fx-cursor: hand;");

        homeButton.setOnAction(e -> {
            controller.openAccountPage(utente);
        });

    }

    private Button createSearchButton() {
        searchButton = new Button("🔍");
        searchButton.setStyle("-fx-font-size: 26px; -fx-background-radius: 8;-fx-text-fill: \"3A6698\";-fx-background-color: WHITE; -fx-cursor: hand;");
        searchButton.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(7),
                new BorderWidths(1.5)
        )));
        searchButton.setFocusTraversable(true);
        searchButton.setMaxHeight(30);
        searchButton.setPrefHeight(30);

        return searchButton;
    }

    private void setSearchFieldAesthetics(TextField field) {
        field.setFont(Font.font("Arial", 26));
        field.setPrefHeight(30);
        field.setPrefWidth(600);
        field.setPromptText("Cerca per nome corso");
        field.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(1)
        )));
    }

    private void setSearchButtonOnAction(Button searchButton) {
        searchButton.setOnAction(Click -> {
            mostraTuttiCorsiClicked = false;
            setNotClickedButtonAesthetic(tuttiCorsi);
            setCorsiByChoice();
        });
    }

    private VBox createSearchArea() {
        VBox searchArea = new VBox(5);

        HBox searchBar = createSearchBar();
        choiceBox = createChoiceBox();

        searchArea.getChildren().addAll(searchBar, choiceBox);

        return searchArea;
    }

    private HBox createSearchBar() {

        HBox searchBar = new HBox(10);
        searchBar.setPadding(new Insets(20));
        searchBar.setAlignment(Pos.TOP_CENTER);

        Button searchButton = createSearchButton();
        this.setSearchButtonOnAction(searchButton);

        searchField = new TextField();
        this.setSearchFieldAesthetics(searchField);
        this.setSearchFieldProprieties(searchField);

        searchBar.getChildren().addAll(searchField, searchButton);

        return searchBar;
    }

    public void mostraTuttiCorsi() {
        tuttiCorsi.fire();
    }

    private HBox createChoiceBox() {
        choiceBox = new HBox(4);
        choiceBox.setAlignment(Pos.CENTER);
        choiceBox.setPadding(new Insets(4));

        choiceBox.setStyle("-fx-background-color: transparent; " +
                "-fx-border-color: transparent; " +
                "-fx-border-width: 0; " +
                "-fx-border-radius: 4; " +
                "-fx-background-radius: 4; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 0, 0, 0, 1);");


        choiceGroup = new ToggleGroup();
        corsoChoice = new ToggleButton("Corso");
        corsoChoice.setToggleGroup(choiceGroup);
        chefChoice = new ToggleButton("Chef");
        chefChoice.setToggleGroup(choiceGroup);
        tipologiaChoice = new ToggleButton("Tipologia");
        tipologiaChoice.setToggleGroup(choiceGroup);

        HBox cercaPer = new HBox(4);
        cercaPer.setAlignment(Pos.CENTER);
        cercaPer.setPadding(new Insets(4));

        cercaPer.setStyle("-fx-background-color: #EEE; " +
                "-fx-border-color: transparent; " +
                "-fx-border-width: 0; " +
                "-fx-border-radius: 4; " +
                "-fx-background-radius: 4; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 0, 0, 0, 1);");

        cercaPer.getChildren().addAll(corsoChoice, chefChoice, tipologiaChoice);

        Separator separator1 = new Separator();
        separator1.setOrientation(Orientation.VERTICAL);
        separator1.setStyle("-fx-padding: 0 10 0 10; -fx-opacity: 0;");

        Button onlineChoice = new Button("Online");
        Button presenzaChoice = new Button("Presenza");

        HBox modCorso = new HBox(4);
        modCorso.setAlignment(Pos.CENTER);
        modCorso.setPadding(new Insets(4));

        modCorso.setStyle("-fx-background-color: #EEE; " +
                "-fx-border-color: transparent; " +
                "-fx-border-width: 0; " +
                "-fx-border-radius: 4; " +
                "-fx-background-radius: 4; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 0, 0, 0, 1);");

        modCorso.getChildren().addAll(onlineChoice, presenzaChoice);


        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);
        separator2.setStyle("-fx-padding: 0 10 0 10; -fx-opacity: 0;");

        costoFilterUp = new Button("€↑");
        costoFilterDown = new Button("€↓");

        HBox costoBox = new HBox(4);
        costoBox.setAlignment(Pos.CENTER);
        costoBox.setPadding(new Insets(4));

        costoBox.setStyle("-fx-background-color: #EEE; " +
                "-fx-border-color: transparent; " +
                "-fx-border-width: 0; " +
                "-fx-border-radius: 4; " +
                "-fx-background-radius: 4; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 0, 0, 0, 1);");

        costoBox.getChildren().addAll(costoFilterUp, costoFilterDown);

        Separator separator3 = new Separator();
        separator3.setOrientation(Orientation.VERTICAL);
        separator3.setStyle("-fx-padding: 0 10 0 10; -fx-opacity: 0;");

        tuttiCorsi = new Button("Mostra tutti");

        HBox showAllBox = new HBox(4);
        showAllBox.setAlignment(Pos.CENTER);
        showAllBox.setPadding(new Insets(4));

        showAllBox.setStyle("-fx-background-color: #EEE; " +
                "-fx-border-color: transparent; " +
                "-fx-border-width: 0; " +
                "-fx-border-radius: 4; " +
                "-fx-background-radius: 4; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 0, 0, 0, 1);");

        showAllBox.getChildren().add(tuttiCorsi);

        choiceGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            for (Toggle tb : choiceGroup.getToggles()) {
                ToggleButton temptb = (ToggleButton) tb;
                if (temptb == newToggle)
                    setClickedButtonAesthetic(temptb);
                else
                    setNotClickedButtonAesthetic(temptb);
            }

            if (newToggle != null) {
                ToggleButton selectedToggle = (ToggleButton) newToggle;
                switch (selectedToggle.getText()) {
                    case "Corso" -> searchField.setPromptText("Cerca per nome corso");
                    case "Chef" -> searchField.setPromptText("Cerca per chef");
                    case "Tipologia" -> searchField.setPromptText("Cerca per tipologia");
                }
            } else {
                searchField.setPromptText("Cerca per nome corso");
                setClickedButtonAesthetic(corsoChoice);
            }

        });

        onlineChoice.setOnAction(event -> {
            if (modalitaSelezionata == Modalita.ONLINE) {
                setNotClickedButtonAesthetic(onlineChoice);
                modalitaSelezionata = null;
            } else {
                setClickedButtonAesthetic(onlineChoice);
                setNotClickedButtonAesthetic(presenzaChoice);
                modalitaSelezionata = Modalita.ONLINE;
            }
            setCorsiByChoice();
        });

        presenzaChoice.setOnAction(event -> {
            if (modalitaSelezionata == Modalita.PRESENZA) {
                setNotClickedButtonAesthetic(presenzaChoice);
                modalitaSelezionata = null;
            } else {
                setClickedButtonAesthetic(presenzaChoice);
                setNotClickedButtonAesthetic(onlineChoice);
                modalitaSelezionata = Modalita.PRESENZA;
            }
            setCorsiByChoice();
        });

        costoFilterUp.setOnAction(event -> {
            if (priceFilter == priceFilter.CRESCENTE) {
                setNotClickedButtonAesthetic(costoFilterUp);
                priceFilter = null;
            } else {
                setClickedButtonAesthetic(costoFilterUp);
                setNotClickedButtonAesthetic(costoFilterDown);
                priceFilter = priceFilter.CRESCENTE;
            }
            sortCorsiByCostoIfNeeded();
            setCorsiBox();
        });

        costoFilterDown.setOnAction(event -> {
            if (priceFilter == priceFilter.DECRESCENTE) {
                setNotClickedButtonAesthetic(costoFilterDown);
                priceFilter = null;
            } else {
                setClickedButtonAesthetic(costoFilterDown);
                setNotClickedButtonAesthetic(costoFilterUp);
                priceFilter = priceFilter.DECRESCENTE;
            }
            sortCorsiByCostoIfNeeded();
            setCorsiBox();
        });

        tuttiCorsi.setOnAction(event -> {
            if (mostraTuttiCorsiClicked) {
                setNotClickedButtonAesthetic(tuttiCorsi);
                mostraTuttiCorsiClicked = false;
            } else {
                setClickedButtonAesthetic(tuttiCorsi);
                mostraTuttiCorsiClicked = true;
            }
            setCorsiByChoice();
        });

        setClickedButtonAesthetic(corsoChoice);
        setNotClickedButtonAesthetic(onlineChoice);
        setNotClickedButtonAesthetic(presenzaChoice);
        setNotClickedButtonAesthetic(tuttiCorsi);
        setNotClickedButtonAesthetic(chefChoice);
        setNotClickedButtonAesthetic(tipologiaChoice);
        setNotClickedButtonAesthetic(costoFilterUp);
        setNotClickedButtonAesthetic(costoFilterDown);


        choiceBox.getChildren().addAll(
                cercaPer,
                separator1,
                modCorso,
                separator2,
                costoBox,
                separator3,
                showAllBox
        );

        addHoverEffectToButtons();

        return choiceBox;
    }

    private void addHoverEffectToButtons() {
        for (Node node : choiceBox.getChildren()) {
            if (node instanceof ButtonBase) {
                ButtonBase button = (ButtonBase) node;
                final String originalStyle = button.getStyle();

                button.setOnMouseEntered(e -> {
                    if (!isButtonSelected(button)) {
                        button.setStyle(originalStyle + "-fx-background-color: #f5f5f5;");
                    }
                });

                button.setOnMouseExited(e -> {
                    if (!isButtonSelected(button)) {
                        button.setStyle(originalStyle);
                    }
                });
            }
        }
    }

    private boolean isButtonSelected(ButtonBase button) {
        if (button instanceof ToggleButton) {
            return ((ToggleButton) button).isSelected();
        }

        if (button.getText().equals("Online")) {
            return modalitaSelezionata == Modalita.ONLINE;
        }
        if (button.getText().equals("Presenza")) {
            return modalitaSelezionata == Modalita.PRESENZA;
        }
        if (button == costoFilterUp) {
            return priceFilter == priceFilter.CRESCENTE;
        }
        if (button == costoFilterDown) {
            return priceFilter == priceFilter.DECRESCENTE;
        }
        if (button == tuttiCorsi) {
            return mostraTuttiCorsiClicked;
        }

        return false;
    }

    private void setNotFoundTextField() {
        Text notFound = new Text("Nessun corso trovato");
        this.setTextAesthetics(notFound);
        StackPane notFoundPane = new StackPane();

        Rectangle bg = new Rectangle(600, 150);
        bg.setArcWidth(30);
        bg.setArcHeight(30);
        bg.setFill(Color.rgb(255, 255, 255, 0.85));

        notFoundPane.getChildren().addAll(bg, notFound);
        corsiBox.getChildren().add(notFoundPane);
    }

    private void setNotClickedButtonAesthetic(ToggleButton button) {
        String base = "-fx-background-color: transparent; " +
                "-fx-text-fill: #334155; " +
                "-fx-border-color: transparent; " +
                "-fx-border-width: 0; " +
                "-fx-border-radius: 4; " +
                "-fx-background-radius: 4; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8 16; " +
                "-fx-font-size: 14px; " +
                "-fx-effect: none; " +
                "-fx-transition: all 0.15s ease-in-out;";
        button.setStyle(base);
    }

    private void setClickedButtonAesthetic(ToggleButton button) {
        String selected = "-fx-background-color: white; " +
                "-fx-text-fill: #334155; " +
                "-fx-border-color: transparent; " +
                "-fx-border-width: 0; " +
                "-fx-border-radius: 4; " +
                "-fx-background-radius: 4; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8 16; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 0, 0, 0, 1); " +
                "-fx-transition: all 0.15s ease-in-out;";
        button.setStyle(selected);
    }

    private void setNotClickedButtonAesthetic(Button button) {
        String base = "-fx-background-color: transparent; " +
                "-fx-text-fill: #334155; " +
                "-fx-border-color: transparent; " +
                "-fx-border-width: 0; " +
                "-fx-border-radius: 4; " +
                "-fx-background-radius: 4; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8 16; " +
                "-fx-font-size: 14px; " +
                "-fx-effect: none; " +
                "-fx-transition: all 0.15s ease-in-out;";
        button.setStyle(base);
    }

    private void setClickedButtonAesthetic(Button button) {
        String selected = "-fx-background-color: white; " +
                "-fx-text-fill: #334155; " +
                "-fx-border-color: transparent; " +
                "-fx-border-width: 0; " +
                "-fx-border-radius: 4; " +
                "-fx-background-radius: 4; " +
                "-fx-cursor: hand; " +
                "-fx-padding: 8 16; " +
                "-fx-font-size: 14px; " +
                "-fx-font-weight: bold; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 0, 0, 0, 1); " +
                "-fx-transition: all 0.15s ease-in-out;";
        button.setStyle(selected);
    }

    private void setSearchFieldProprieties(TextField searchField) {
        searchField.setFocusTraversable(true);
        searchField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER)
                searchButton.fire();
        });
        searchField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && !searchField.getText().isEmpty()) {
                setCorsiByChoice();
            }
        });
    }

    private void setLoadingCursor() {
        scene.setCursor(Cursor.WAIT);
    }

    private void restoreCursor() {
        scene.setCursor(originalCursor);
    }

    private void setCorsiByChoice() {
        setLoadingCursor();
        corsiBox.getChildren().clear();
        this.setLoadingPane();

        new Thread(() -> {
            try {
                String toSearch = searchField.getText().trim();

                if (mostraTuttiCorsiClicked) {
                    searchField.setText("");
                    corsi = controller.getAllCourses();
                } else if (!toSearch.isEmpty()) {
                    if (searchField.getPromptText().equals("Cerca per nome corso")) {
                        corsi = controller.searchCorsiLikeString(toSearch);
                    } else if (searchField.getPromptText().equals("Cerca per chef")) {
                        corsi = controller.searchCorsiByChef(toSearch);
                    } else {
                        corsi = controller.searchCorsiByTipologia(toSearch);
                    }
                } else {
                    corsi = controller.getMostFollowedCourses(4);
                }

                if (corsi == null || corsi.isEmpty()) {
                    throw new corsiNotFoundException();
                }

                filterCorsiByModalitaIfNeeded();
                sortCorsiByCostoIfNeeded();

                Platform.runLater(() -> {
                    setCorsiBox();
                    restoreCursor();
                });

            } catch (corsiNotFoundException | SQLException e) {
                Platform.runLater(() -> {
                    corsiBox.getChildren().clear();
                    setNotFoundTextField();
                    restoreCursor();
                });
            }
        }).start();
    }

    private void filterCorsiByModalitaIfNeeded() {
        if (corsi == null || modalitaSelezionata == null) return;

        corsi.removeIf(corso -> corso.getModalita_corso() == null ||
                !corso.getModalita_corso().getLabel().equalsIgnoreCase(modalitaSelezionata.name()));
    }

    private void sortCorsiByCostoIfNeeded() {
        if (corsi == null || priceFilter == null) return;

        if (priceFilter == priceFilter.CRESCENTE) {
            corsi.sort(Comparator.comparing(Corso::getCosto));
        } else if (priceFilter == priceFilter.DECRESCENTE) {
            corsi.sort(Comparator.comparing(Corso::getCosto).reversed());
        }
    }

    private ScrollPane createCorsiContainer() {
        corsiBox = new HBox(20);
        corsiBox.setAlignment(Pos.CENTER);
        corsiBox.setPadding(new Insets(20));

        try {
            corsi = controller.getMostFollowedCourses(4);
            setCorsiBox();
        } catch (corsiNotFoundException | SQLException e) {
            setNotFoundTextField();
        }

        corsiScrollPane = new ScrollPane(corsiBox);
        corsiScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        corsiScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        corsiScrollPane.setFitToHeight(true);
        corsiScrollPane.setPannable(true);
        corsiScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        corsiScrollPane.addEventFilter(MouseEvent.MOUSE_PRESSED, Event::consume);

        return corsiScrollPane;
    }

    private VBox createCenterContent() {
        VBox center = new VBox(20);
        center.setPadding(new Insets(20));
        center.setAlignment(Pos.TOP_CENTER);

        Image sfondo = new Image(Objects.requireNonNull(getClass().getResource("/Media/Background/sfondoBianco.png")).toExternalForm());
        BackgroundSize backgroundSize = new BackgroundSize(
                BackgroundSize.AUTO,
                BackgroundSize.AUTO,
                false,
                false,
                true,
                false
        );

        BackgroundImage backgroundImage = new BackgroundImage(
                sfondo,
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.CENTER,
                backgroundSize
        );

        center.setBackground(new Background(backgroundImage));

        VBox searchArea = createSearchArea();
        HBox corsoRow = new HBox(20);
        corsoRow.setAlignment(Pos.CENTER);

        ScrollPane corsiScrollPane = createCorsiContainer();
        HBox scrollContainer = new HBox();
        scrollContainer.setAlignment(Pos.CENTER);
        scrollContainer.getChildren().add(corsiScrollPane);

        center.getChildren().addAll(searchArea, scrollContainer);
        return center;
    }

    private void addHoverAnimation(Region node) {
        ScaleTransition stEnlarge = new ScaleTransition(Duration.millis(200), node);
        stEnlarge.setToX(1.05);
        stEnlarge.setToY(1.05);

        ScaleTransition stShrink = new ScaleTransition(Duration.millis(200), node);
        stShrink.setToX(1.0);
        stShrink.setToY(1.0);

        node.setOnMouseEntered(e -> stEnlarge.playFromStart());
        node.setOnMouseExited(e -> stShrink.playFromStart());
    }

    private void setCorsiBox() {
        corsiBox.getChildren().clear();

        if (corsi == null || corsi.isEmpty()) {
            setNotFoundTextField();
            return;
        }

        CorsoPanel tempCorsoPanel;
        for (Corso corso : corsi) {
            tempCorsoPanel = new CorsoPanel(this.controller);
            tempCorsoPanel.setCorso(corso);
            corsiBox.getChildren().add(tempCorsoPanel);
            addHoverAnimation(tempCorsoPanel);
        }
    }

    private void setLoadingPane(){
        StackPane loadingPane = new StackPane();
        loadingPane.setPadding(new Insets(20));
        Rectangle background = new Rectangle(600, 150);
        background.setArcWidth(30);
        background.setArcHeight(30);
        background.setFill(Color.rgb(255, 255, 255, 0.85));

        corsiBox.getChildren().add(loadingPane);
        Text caricamentoText = new Text();
        this.setTextAesthetics(caricamentoText);
        startAnimation(caricamentoText);
        loadingPane.getChildren().addAll(background, caricamentoText);
    }

    public void setUtente(Utente utente){
        this.utente = utente;
        createHomeButton();
        setLoggedIn();
        if (utente instanceof Chef){
            setChef();
        }
        homeButtons.getChildren().clear();
        homeButtons.getChildren().add(homeButton);
    }

    public Boolean isLoggedIn(){
        return isLoggedIn;
    }

    public void setLoggedIn() {
        isLoggedIn = true;
    }

    public Boolean isChef(){
        return isChef;
    }

    public void setChef() {
        isChef = true;
    }

    public void setLogOut() {
        isChef = false;
        isLoggedIn = false;
        homeButtons.getChildren().clear();
        homeButtons.getChildren().add(createLoginButton());
    }

    private void startAnimation(Text caricamentoText){
        Timeline AnimazionePuntini = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> caricamentoText.setText("Ricerca in corso")),
                new KeyFrame(Duration.seconds(0.3), e -> caricamentoText.setText("Ricerca in corso.")),
                new KeyFrame(Duration.seconds(0.6), e -> caricamentoText.setText("Ricerca in corso..")),
                new KeyFrame(Duration.seconds(0.9), e -> caricamentoText.setText("Ricerca in corso...")),
                new KeyFrame(Duration.seconds(1.2), e -> caricamentoText.setText("Ricerca in corso..."))
        );
        AnimazionePuntini.setCycleCount(Animation.INDEFINITE);
        AnimazionePuntini.play();
    }

    private void setTextAesthetics(Text text){
        text.setFill(Color.valueOf("#3A6698"));
        text.setFont(
                Font.loadFont(getClass().getResourceAsStream("/Media/Fonts/times.ttf"), 70)
        );
    }
}