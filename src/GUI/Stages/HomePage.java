package GUI.Stages;

import Controller.*;
import Entity.*;
import GUI.Pane.*;
import GUI.Buttons.*;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.*;
import java.util.Objects;

public class HomePage extends Stage {

    private Controller controller;
    private BorderPane root;
    private Scene scene;
    private HBox loginButtons;

    private Boolean isLoggedIn = false;
    private Boolean isChef = false;


    public HomePage(Controller controller) {
        this.controller = controller;

        setFunctionalitiesHomePage();
        setAestheticsHomePage();
    }

    private void setFunctionalitiesHomePage(){
        root = new BorderPane();
        root.setTop(createTopBar());
        root.setCenter(createCenterContent());
        scene = new Scene(root, 800, 600);
        this.setScene(scene);
        this.setCTRLW();
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

    }


    private VBox createTopBar() {
        VBox topBar = new VBox();
        topBar.setPadding(new Insets(10));
        topBar.setSpacing(20);
        topBar.setStyle("-fx-background-color: WHITE;");
        topBar.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(0,0,2,0)
        )));

        BorderPane closePane = new BorderPane();
        HBox controlButtons = new HBox(5);
        controlButtons.setAlignment(Pos.TOP_RIGHT);

        controlButtons.getChildren().addAll(
                new CircleButton().setToMinimizeButtonWithAction(this),
                new CircleButton().setToCloseButtonWithAction(e->{
                    controller.endAll();
                })
        );

        closePane.setRight(controlButtons);

        Label uninaFoodLabel = new Label("UNINA FOOD LAB");
        uninaFoodLabel.setTextFill(Color.valueOf("#3A6698"));
        Font timesNewRoman = Font.loadFont(
                getClass().getResourceAsStream("/Images/times.ttf"),
                50
        );

        uninaFoodLabel.setFont(timesNewRoman);
        uninaFoodLabel.setAlignment(Pos.CENTER);

        loginButtons = createLoginButtonsBox();

        topBar.getChildren().addAll(closePane, createLogoView(), uninaFoodLabel, loginButtons);
        topBar.setAlignment(Pos.TOP_CENTER);

        return topBar;
    }


    private ImageView createLogoView() {
        Image logoImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/UninaFoodLabLogo.png")));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(250);
        logoView.setFitHeight(250);
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
        Image loginImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Images/LoginImage.png")));
        ImageView loginView = new ImageView(loginImage);

        loginView.setPreserveRatio(true);
        loginView.setFitWidth(35);
        loginView.setFitHeight(35);

        Button loginButton = new Button();
        loginButton.setGraphic(loginView);
        loginButton.setStyle("-fx-background-color: TRANSPARENT;");
        loginButton.setOnMouseClicked(e -> {
                    controller.openLoginPage();
                }
        );

        return loginButton;
    }

    private Button createLoginButton() {
        Button loginButton = new Button("Login");
        loginButton.setFont(Font.font("Arial", 20));
        loginButton.setStyle("-fx-background-color: \"#3A6698\"; -fx-text-fill: WHITE;");
        loginButton.setFocusTraversable(false);
        loginButton.setOnAction(e ->{
            controller.openLoginPage();
        });
        this.setOnMouseTraverse(loginButton);
        this.setFocusPropreties(loginButton);
        return  loginButton;
    }

    private HBox createSearchBar() {

        HBox searchBar = new HBox(10);
        searchBar.setPadding(new Insets(20));
        searchBar.setAlignment(Pos.TOP_CENTER);

        TextField searchField = new TextField();
        searchField.setFont(Font.font("Arial", 26));

        searchField.setPrefHeight(30);
        searchField.setPrefWidth(600);

        searchField.setPromptText("Cerca un corso");
        searchField.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(1)
        )));

        searchField.setFocusTraversable(true);

        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            String sql = "SELECT * FROM corso WHERE nome_corso LIKE '%" + newValue + "%'";
            System.out.println(sql);
        });

        searchBar.getChildren().add(searchField);

        return searchBar;
    }

    private HBox createCorsiContainer() {
        HBox corsiBox = new HBox(20);
        corsiBox.setAlignment(Pos.TOP_CENTER);
        corsiBox.setPadding(new Insets(20));

        CorsoPanel corsoPanel1 = new CorsoPanel("/Images/CucinaMolecolare.png", "Cucina Molecolare", controller);
        CorsoPanel corsoPanel2 = new CorsoPanel("/Images/CucinaAsiaticaFusion.png", "Cucina Asiatica Fusion", controller);
        CorsoPanel corsoPanel3 = new CorsoPanel("/Images/Primidimare.png", "Primi di mare", controller);
        CorsoPanel corsoPanel4 = new CorsoPanel("/Images/PasticceriadiBase.png", "Pasticceria di Base", controller);

        corsoPanel1.setOnMouseClicked(e->{
            controller.openCorsoPage("Cucina Molecolare", "/Images/CucinaMolecolare.png", controller);
        });
        corsoPanel2.setOnMouseClicked(e->{
            controller.openCorsoPage("Cucina Asiatica Fusion", "/Images/CucinaAsiaticaFusion.png", controller);
        });
        corsoPanel3.setOnMouseClicked(e->{
            controller.openCorsoPage("Primi di mare", "/Images/Primidimare.png", controller);
        });
        corsoPanel4.setOnMouseClicked(e->{
            controller.openCorsoPage("Pasticceria di Base", "/Images/PasticceriadiBase.png", controller);
        });

        corsiBox.getChildren().addAll(corsoPanel1, corsoPanel2, corsoPanel3, corsoPanel4);
        return corsiBox;
    }

    private VBox createCenterContent(){
        VBox center = new VBox(20);
        center.setPadding(new Insets(20));
        center.setAlignment(Pos.TOP_CENTER);

        HBox searchBar = createSearchBar();

        HBox corsoRow = new HBox(20);
        corsoRow.setAlignment(Pos.CENTER);

        corsoRow.getChildren().addAll(
                createCorsiContainer()
        );

        center.getChildren().addAll(
                searchBar,
                corsoRow
        );

        return center;
    }

    private Button createAccountButton(Utente utente) {
        Button accountButton = new Button("Account");
        accountButton.setFont(Font.font("Arial", 20));
        accountButton.setStyle("-fx-background-color: \"#3A6698\"; -fx-text-fill: WHITE;");
        accountButton.setFocusTraversable(false);
        accountButton.setOnAction(e -> {
            controller.openAccountPage(utente);
        });
        this.setFocusPropreties(accountButton);

        return accountButton;
    }

    private void setOnMouseTraverse(Button button){
        button.setOnMouseEntered(e->{
                    button.setStyle("-fx-background-color: WHITE;-fx-text-fill: \"#3A6698\";");
                    button.setBorder(new Border(new BorderStroke(
                            Color.valueOf("#3A6698"),
                            BorderStrokeStyle.SOLID,
                            CornerRadii.EMPTY,
                            new BorderWidths(0,0,1, 0)
                    )));
                }
        );
        button.setOnMouseExited(e->{
                    button.setStyle("-fx-background-color: \"#3A6698\";-fx-text-fill: WHITE;");
                }
        );
    }
    private void setFocusPropreties(Button button){
        button.setFocusTraversable(true);
        button.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                button.setStyle("-fx-background-color: WHITE;-fx-text-fill: \"#3A6698\";");
                button.setBorder(new Border(new BorderStroke(
                        Color.valueOf("#3A6698"),
                        BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        new BorderWidths(0,0,1, 0)
                )));
            }else{
                button.setStyle("-fx-background-color: \"#3A6698\";-fx-text-fill: WHITE;");
            }

        });

    }

    public void becomeHomePageChef(Chef chef){
        //TODO
        loginButtons.getChildren().clear();
        loginButtons.getChildren().add(createAccountButton(chef));
        setLoggedIn();
        setChef();
    }

    public void becomeHomePageStudente(Studente studente){
        //TODO
        loginButtons.getChildren().clear();
        loginButtons.getChildren().add(createAccountButton(studente));
        setLoggedIn();
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
}


