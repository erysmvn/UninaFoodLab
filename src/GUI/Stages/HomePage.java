package GUI.Stages;

import Controller.*;
import Entity.*;
import GUI.Pane.*;
import GUI.Buttons.*;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Objects;

public class HomePage extends Stage {

    private Controller controller;
    private BorderPane root;
    private ScrollPane corsiScrollPane;
    private Scene scene;
    private HBox homeButtons;
    private HBox corsiBox;
    private Boolean isLoggedIn = false;
    private Boolean isChef = false;
    private HBox choiceBox;
    private ArrayList<Corso> corsi;
    private Timeline AnimazioneRicerca;
    private TextField searchField;
    private Button searchButton;
    private Button tuttiCorsi;
    private Button homeButton;
    private Utente utente = null;

    public HomePage(Controller controller) {
        this.controller = controller;
        setFunctionalitiesHomePage();
        setAestheticsHomePage();
        this.setCTRLW();
    }


    private void setFunctionalitiesHomePage(){
        root = new BorderPane();
        root.setTop(createTopBar());
        root.setCenter(createCenterContent());

    }

    private void setCTRLW(){
        scene.setOnKeyPressed(event -> {
            if(event.isControlDown() && event.getCode() == KeyCode.W){
                controller.endAll();
            }
        });
    }

    private void setAestheticsHomePage(){
        this.initStyle(StageStyle.UNDECORATED);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        this.setX(screenBounds.getMinX());
        this.setY(screenBounds.getMinY());
        this.setWidth(screenBounds.getWidth());
        this.setHeight(screenBounds.getHeight());

        scene = new Scene(root, 800, 600);
        this.setScene(scene);

    }


    private void setTopBarAesthetics(VBox topBar){
        topBar.setPadding(new Insets(10));
        topBar.setSpacing(20);
        topBar.setStyle("-fx-background-color: WHITE;");
        topBar.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(0,0,2,0)
        )));
    }

    private BorderPane createCloseAndMinimizePane(){
        BorderPane closeAndMinimizePane = new BorderPane();
        HBox controlButtons = new HBox(5);
        controlButtons.setAlignment(Pos.TOP_RIGHT);

        controlButtons.getChildren().addAll(
                new CircleButton().setToMinimizeButtonWithAction(this),
                new CircleButton().setToCloseButtonWithAction(e->{
                    controller.endAll();
                })
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

    private Button createHomeButton(){

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
        return homeButton;
    }


    private Button createSearchButton(){
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

        return  searchButton;
    }

    private void setSearchFieldAesthetics(TextField field){
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
        PauseTransition pause = new PauseTransition(Duration.seconds(6));

        searchButton.setOnAction(Click -> {

            setNotClickedButtonAesthetic(tuttiCorsi);

            corsiBox.getChildren().clear();

            this.setLoadingPane();

            pause.setOnFinished(pauseEnded -> {
                AnimazioneRicerca.stop();
                String toSearch = searchField.getText();

                if (!toSearch.isEmpty()){
                    corsiBox.getChildren().clear();
                     if (searchField.getPromptText().equals("Cerca per nome corso")) {
                         corsi = controller.searchCorsiLikeString(toSearch);
                    }else if(searchField.getPromptText().equals("Cerca per chef")) {
                            corsi = controller.searchCorsiByChef(toSearch);
                    }else{
                            corsi = controller.searchCorsiByTipologia(toSearch);
                    }
                     if (corsi != null) {
                        CorsoPanel tempCorsoPanel;

                        for (Corso corso : corsi) {
                            tempCorsoPanel = new CorsoPanel(this.controller);
                            tempCorsoPanel.setCorso(corso);
                            corsiBox.getChildren().add(tempCorsoPanel);
                        }

                     }else {
                        this.setNotFoundTextField();
                        }
                }else{
                    this.loadTopCorsi();
                }

            });

            pause.play();
        });

    }


    private VBox createSearchArea() {
        VBox searchArea = new VBox(5);

        HBox searchBar = createSearchBar();
        choiceBox = createChoiceBox();

        searchArea.getChildren().addAll(searchBar, choiceBox);

        return  searchArea;
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

        searchBar.getChildren().addAll(searchField,searchButton);

        return searchBar;
    }

    private HBox createChoiceBox() {
        choiceBox = new HBox(2);
        choiceBox.setAlignment(Pos.CENTER);

        Label cercaPerLabel = new Label("Cerca per");
        cercaPerLabel.setFont(Font.font("System", 14));
        cercaPerLabel.setTextFill(Color.web("WHITE"));
        cercaPerLabel.setPadding(new Insets(6, 10, 5, 10));
        cercaPerLabel.setStyle("-fx-background-color: #3a6698; -fx-background-radius: 7;");

        ToggleGroup choiceGroup = new ToggleGroup();

        ToggleButton corsoChoice = new ToggleButton("Corso");
        corsoChoice.setToggleGroup(choiceGroup);

        ToggleButton chefChoice = new ToggleButton("Chef");
        chefChoice.setToggleGroup(choiceGroup);

        ToggleButton tipologiaChoice = new ToggleButton("Tipologia");
        tipologiaChoice.setToggleGroup(choiceGroup);

        tuttiCorsi = new Button("Mostra tutti");;


        tuttiCorsi.setOnAction(event -> {
            
            if(tuttiCorsi.getStyle().equals("-fx-background-color:white;-fx-text-fill:#3a6698;-fx-border-color:#3a6698;" +
                    "-fx-border-width:1.5px;-fx-border-radius:7;-fx-background-radius:7;-fx-cursor:hand;")) {

                setClickedButtonAesthetic(tuttiCorsi);
                ArrayList<Corso> corsi = controller.getAllCourses();
                if (corsi != null) {
                    CorsoPanel tempCorsoPanel;
                    for (Corso corso : corsi) {
                        tempCorsoPanel = new CorsoPanel(this.controller);
                        tempCorsoPanel.setCorso(corso);
                        corsiBox.getChildren().add(tempCorsoPanel);
                    }
                }
            }

        });

        setClickedButtonAesthetic(corsoChoice);
        setNotClickedButtonAesthetic(tuttiCorsi);
        setNotClickedButtonAesthetic(chefChoice);
        setNotClickedButtonAesthetic(tipologiaChoice);

        choiceGroup.selectedToggleProperty().addListener((obs, oldT, choosed) -> {
            for (Toggle tb : choiceGroup.getToggles()) {
                ToggleButton temptb = (ToggleButton) tb;
                if(temptb == choosed)
                    setClickedButtonAesthetic(temptb);
                else
                    setNotClickedButtonAesthetic(temptb);
            }

            if (choosed != null) {
                ToggleButton selectedToggle = (ToggleButton) choosed;
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
        choiceBox.getChildren().addAll(cercaPerLabel, corsoChoice, chefChoice, tipologiaChoice, tuttiCorsi);
        return choiceBox;
    }

    private void setNotClickedButtonAesthetic(ToggleButton button){
        String base = "-fx-background-color:white;-fx-text-fill:#3a6698;-fx-border-color:#3a6698;" +
                "-fx-border-width:1.5px;-fx-border-radius:7;-fx-background-radius:7;-fx-cursor:hand;";

        button.setStyle(base);
    }

    private void setClickedButtonAesthetic(ToggleButton button){
        String selected  = "-fx-background-color:#3a6698;-fx-text-fill:white;-fx-border-color:#3a6698;" +
                "-fx-border-width:1.5px;-fx-border-radius:7;-fx-background-radius:7;-fx-cursor:hand;";

        button.setStyle(selected);
    }

    private void setNotClickedButtonAesthetic(Button button){
        String base = "-fx-background-color:white;-fx-text-fill:#3a6698;-fx-border-color:#3a6698;" +
                "-fx-border-width:1.5px;-fx-border-radius:7;-fx-background-radius:7;-fx-cursor:hand;";

        button.setStyle(base);
    }

    private void setClickedButtonAesthetic(Button button){
        String selected  = "-fx-background-color:#3a6698;-fx-text-fill:white;-fx-border-color:#3a6698;" +
                "-fx-border-width:1.5px;-fx-border-radius:7;-fx-background-radius:7;-fx-cursor:hand;";

        button.setStyle(selected);
    }

    private void setSearchFieldProprieties(TextField searchField){
        searchField.setFocusTraversable(true);
        searchField.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ENTER)
                    searchButton.fire();
        });
        searchField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && searchField.getText().isEmpty()) {
               this.loadTopCorsi();
            }
        });

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
        AnimazioneRicerca = startAnimation(caricamentoText);

        loadingPane.getChildren().addAll(background, caricamentoText);

    }

    private void setTextAesthetics(Text text){
        text.setFill(Color.valueOf("#3A6698"));
        text.setFont(
                Font.loadFont(getClass().getResourceAsStream("/Media/Fonts/times.ttf"), 70)
        );
    }

    private Timeline startAnimation(Text caricamentoText){
      Timeline AnimazionePuntini = new Timeline(
                new KeyFrame(Duration.seconds(0), e -> caricamentoText.setText("Ricerca in corso")),
                new KeyFrame(Duration.seconds(0.3), e -> caricamentoText.setText("Ricerca in corso.")),
                new KeyFrame(Duration.seconds(0.6), e -> caricamentoText.setText("Ricerca in corso..")),
                new KeyFrame(Duration.seconds(0.9), e -> caricamentoText.setText("Ricerca in corso...")),
                new KeyFrame(Duration.seconds(1.2), e -> caricamentoText.setText("Ricerca in corso..."))
        );
        AnimazionePuntini.setCycleCount(Animation.INDEFINITE);
        AnimazionePuntini.play();

        return AnimazionePuntini;
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

    private void loadTopCorsi(){

        corsiBox.getChildren().clear();
        ArrayList<Corso> topCorsi = controller.getMostFollowedCourses(4);
        CorsoPanel tempCorsoPanel;

        for (Corso corso : topCorsi) {
            tempCorsoPanel = new CorsoPanel(this.controller);
            tempCorsoPanel.setCorso(corso);
            corsiBox.getChildren().add(tempCorsoPanel);
        }
    }

    private ScrollPane createCorsiContainer() {
    HBox corsiHBox = new HBox(20);
    corsiHBox.setAlignment(Pos.CENTER);
    corsiHBox.setPadding(new Insets(20));


    ArrayList<Corso> corsi = controller.getMostFollowedCourses(4);;

    for (Corso c : corsi) {
        CorsoPanel tempCorsoPanel = new CorsoPanel(controller);
        tempCorsoPanel.setCorso(c);
        corsiHBox.getChildren().add(tempCorsoPanel);
    }

    corsiScrollPane = new ScrollPane(corsiHBox);

    corsiScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    corsiScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    corsiScrollPane.setFitToHeight(true);;
    corsiScrollPane.setPannable(true);
    corsiScrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");


    corsiScrollPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
        event.consume();
    });


    this.corsiBox = corsiHBox;

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

    public Boolean isLoggedIn(){
        return isLoggedIn;
    }

    public Boolean isChef(){
        return isChef;
    }

    public void setLoggedIn() {
        isLoggedIn = true;
    }

    public void setChef() {
        isChef = true;
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

    public void setLogOut() {
        homeButtons.getChildren().clear();
        homeButtons.getChildren().add(createLoginButton());
    }

}


