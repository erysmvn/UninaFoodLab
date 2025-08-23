package GUI.Stages;

import Controller.*;
import DAO.CorsoDAO;
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
import javafx.scene.effect.*;
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
    private Scene scene;
    private HBox loginButtons;
    private Boolean isLoggedIn = false;
    private Boolean isChef = false;
    private HBox corsiBox;;
    private ArrayList<Corso> corsi;
    private Timeline AnimazioneRicerca;
    private CorsoDAO corsoDAO;
    private TextField searchField;
    private Button searchButton;
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


    private VBox createTopBar() {
        VBox topBar = new VBox();
        this.setTopBarAesthetics(topBar);

        BorderPane closeAndMinimizePane = createCloseAndMinimizePane();

        loginButtons = createLoginButtonsBox();

        topBar.getChildren().addAll(closeAndMinimizePane, createLogoView(), loginButtons);
        topBar.setAlignment(Pos.TOP_CENTER);

        return topBar;
    }

    private ImageView createLogoView() {
        Image logoImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Media/Logos/LogoHomePage.png")));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(600);
        logoView.setFitHeight(600);
        logoView.setPreserveRatio(true);
        return logoView;
    }

    private HBox createLoginButtonsBox() {
        HBox buttonsBox = new HBox(20);
        buttonsBox.setAlignment(Pos.CENTER);
        buttonsBox.getChildren().addAll(
                createLoginButtonWithImage(),
                createLoginButton()
        );

        return buttonsBox;
    }

    private Button createLoginButtonWithImage(){
        Image loginImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Media/Icons/LoginImage.png")));
        ImageView loginView = new ImageView(loginImage);

        loginView.setPreserveRatio(true);
        loginView.setFitWidth(35);
        loginView.setFitHeight(35);

        Button loginButton = new Button();
        loginButton.setGraphic(loginView);
        loginButton.setStyle("-fx-background-color: TRANSPARENT; -fx-cursor: hand;");
        loginButton.setOnMouseClicked(e -> {
                    controller.openLoginPage();
                }
        );

        return loginButton;
    }


    private Button createLoginButton() {
        Button loginButton = new Button("Login");
        loginButton.setFont(Font.font("Arial", 20));
        loginButton.setStyle("-fx-background-color: \"#3A6698\"; -fx-text-fill: WHITE; -fx-cursor: hand;");
        loginButton.setFocusTraversable(false);
        loginButton.setOnAction(e ->{
            controller.openLoginPage();
        });
        this.setOnMouseTraverse(loginButton);
        this.setFocusPropreties(loginButton);
        return  loginButton;
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
        field.setPromptText("Cerca un corso");
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

            corsiBox.getChildren().clear();

            this.setLoadingPane();

            pause.setOnFinished(pauseEnded -> {
                AnimazioneRicerca.stop();
                String nomeCorso = searchField.getText();

                if (!nomeCorso.isEmpty()) {
                    corsi = corsoDAO.searchCorsiLikeString(nomeCorso);

                    corsiBox.getChildren().clear();

                    if (corsi.isEmpty()) {
                        this.setNotFoundTextField();
                    } else {
                        CorsoPanel tempCorsoPanel;
                        for (Corso corso : corsi) {
                            tempCorsoPanel = new CorsoPanel(this.controller);
                            tempCorsoPanel.setCorso(corso);
                            corsiBox.getChildren().add(tempCorsoPanel);
                        }
                    }

                } else {
                    this.loadTopCorsi(corsoDAO);
                }
            });


            pause.play();
        });

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

    private void setSearchFieldProprieties(TextField searchField){

        searchField.setFocusTraversable(true);
        searchField.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ENTER)
                    searchButton.fire();

        });
        searchField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal && searchField.getText().isEmpty()) {
               this.loadTopCorsi(corsoDAO);
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

    private void loadTopCorsi(CorsoDAO corsoDAO) {
        corsiBox.getChildren().clear();
        ArrayList<Corso> topCorsi = corsoDAO.getCorsiConPiuStudenti(4);
        CorsoPanel tempCorsoPanel;

        for (Corso corso : topCorsi) {
            tempCorsoPanel = new CorsoPanel(this.controller);
            tempCorsoPanel.setCorso(corso);
            corsiBox.getChildren().add(tempCorsoPanel);
        }
    }

    private HBox createCorsiContainer() {
        corsiBox = new HBox(20);
        corsiBox.setAlignment(Pos.TOP_CENTER);
        corsiBox.setPadding(new Insets(20));
        //TODO oppure una funzione in controller getCorsiConPiuStudenti????
        //---------------------------------------------------
        corsoDAO = controller.getCorsoDAO();
        ArrayList<Corso> corsi = corsoDAO.getCorsiConPiuStudenti(4);
        // --------------------------------------------------
        CorsoPanel tempCorsoPanel;
        for(Corso c: corsi){
            tempCorsoPanel = new CorsoPanel(controller);
            tempCorsoPanel.setCorso(c);
            corsiBox.getChildren().add(tempCorsoPanel);
        }
        return corsiBox;
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


        HBox searchBar = createSearchBar();

        HBox corsoRow = new HBox(20);
        corsoRow.setAlignment(Pos.CENTER);

        corsiBox = createCorsiContainer();

        for (Node node : corsiBox.getChildren()) {
            if (node instanceof Region) {
                DropShadow dropShadowPanel = new DropShadow(10, Color.rgb(0, 0, 0, 0.3));
                node.setEffect(dropShadowPanel);
            }
        }

        corsoRow.getChildren().add(corsiBox);

        center.getChildren().addAll(searchBar, corsoRow);

        return center;
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

        homeButton.setFont(Font.font("Nimbus Roman", 18));
        homeButton.setStyle("-fx-border-color: #3a6698; -fx-border-width: 1px; -fx-border-radius: 30px; -fx-background-color: white; -fx-text-fill: #3a6698;");


        homeButton.setOnAction(e -> {
            controller.openAccountPage(utente);
        });

        return homeButton;
    }

    private void setOnMouseTraverse(Button button){
        button.setOnMouseEntered(e->{
                    button.setStyle("-fx-background-color: WHITE;-fx-text-fill: \"#3A6698\"; -fx-cursor: hand;");
                    button.setBorder(new Border(new BorderStroke(
                            Color.valueOf("#3A6698"),
                            BorderStrokeStyle.SOLID,
                            CornerRadii.EMPTY,
                            new BorderWidths(0,0,1, 0)
                    )));
                }
        );
        button.setOnMouseExited(e->{
                    button.setStyle("-fx-background-color: \"#3A6698\";-fx-text-fill: WHITE; -fx-cursor: hand;");
                }
        );
    }
    private void setFocusPropreties(Button button){
        button.setFocusTraversable(true);
        button.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                button.setStyle("-fx-background-color: WHITE;-fx-text-fill: \"#3A6698\"; -fx-cursor: hand;");
                button.setBorder(new Border(new BorderStroke(
                        Color.valueOf("#3A6698"),
                        BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        new BorderWidths(0,0,1, 0)
                )));
            }else{
                button.setStyle("-fx-background-color: \"#3A6698\";-fx-text-fill: WHITE; -fx-cursor: hand;");
            }

        });

    }

    public void setUtente(Utente utente){
        this.utente = utente;
        createHomeButton();
        loginButtons.getChildren().clear();
        loginButtons.getChildren().add(homeButton);
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
    public void setLogOut() {
        this.isLoggedIn = false;
        loginButtons.getChildren().clear();
        loginButtons.getChildren().addAll(createLoginButtonWithImage(),createLoginButton());
    }

    public void setChef() {
        isChef = true;
    }
}


