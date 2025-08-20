package GUI.Stages;

import Controller.Controller;
import Entity.*;
import GUI.Pane.*;
import GUI.Buttons.*;

import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.*;

public class AccountPage extends Stage {
    Controller controller;
    Scene scene;
    BorderPane root;
    Button impostazioniButton;
    Button accountButton;
    Button corsiButton;
    VBox impostazioniPanel;
    VBox accountPanel;
    StackPane content;
    BorderPane accountCorsiPanel;
    Utente utente;


    public

    AccountPage(Controller controller) {
        this.controller = controller;
        this.initStyle(StageStyle.TRANSPARENT);
        root = createRoot();
        content = new StackPane();
        content.setStyle("-fx-background-color: WHITE;");

        HBox topBar = createTopBar();
        root.setTop(topBar);
        root.setCenter(content);

        scene = new Scene(root, 1050,750);
        scene.setFill(Color.TRANSPARENT);
        scene.setOnKeyPressed(event -> {
            if(event.isControlDown() && event.getCode() == KeyCode.W){
                this.close();
            }
        });

        this.setScene(scene);

    }

    public void initPage(Utente utente){
        this.utente = utente;
        accountPanel = createAccountPanel(utente);
        accountCorsiPanel = createAccountCorsiPanel(controller);
        impostazioniPanel = new ImpostazioniPanel(controller);
        content.getChildren().addAll(accountPanel,accountCorsiPanel,impostazioniPanel);

    }



    private BorderPane createRoot(){
        root = new BorderPane();
        root.setBackground(new Background(
                new BackgroundFill(Color.WHITE, new CornerRadii(30, 30, 30, 30, false), Insets.EMPTY)
        ));

        root.setPadding(new Insets(10));
        root.setBackground(new Background(new BackgroundFill(
                Color.WHITE, new CornerRadii(30, 30, 30, 30, false), Insets.EMPTY
        )));

        root.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(30,30,30,30,false),
                new BorderWidths(2)
        )));
        return  root;
    }

    private VBox createAccountPanel(Utente utente){
        accountPanel = new AccountPanel(utente);
        accountPanel.setSpacing(10);
        accountPanel.setPadding(new Insets(10));
        accountPanel.setVisible(true);
        accountPanel.setManaged(true);
        return accountPanel;
    }

    private AccountCorsiPanel createAccountCorsiPanel( Controller controller){
        AccountCorsiPanel accountCorsiPanel = new AccountCorsiPanel(this.controller);
        accountCorsiPanel.initPanel(utente);
        return accountCorsiPanel;
    }


    private HBox createTopBar() {
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(10));
        topBar.setSpacing(10);
        topBar.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        topBar.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(0,0,2, 0)
        )));

        accountButton = createAccountButton();
        corsiButton = createCorsiButton();
        impostazioniButton = createImpostazioniButton();

        CircleButton minimizeBtn = new CircleButton().setToMinimizeButtonWithAction(this);
        CircleButton closeBtn = new CircleButton().setToCloseButtonWithAction(this);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBar.getChildren().addAll(accountButton, corsiButton, impostazioniButton, spacer, minimizeBtn, closeBtn);

        return  topBar;
    }

    private Button createAccountButton(){
        Button accountButton = new Button("Account");
        accountButton.setTextFill(Color.valueOf("#3A6698"));
        accountButton.setStyle("-fx-background-color:WHITE;");
        accountButton.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(0,0,1, 0)
        )));
        accountButton.setOnAction(e -> {
            accountButton.setStyle("-fx-background-color: WHITE; -fx-text-fill: #3A6698;");
            accountButton.setBorder(new Border(new BorderStroke(
                    Color.valueOf("#3A6698"),
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    new BorderWidths(0,0,1, 0)
            )));
            corsiButton.setStyle("-fx-background-color: #3A6698; -fx-text-fill: WHITE;");
            impostazioniButton.setStyle("-fx-background-color: #3A6698; -fx-text-fill: WHITE;");
            accountPanel.setVisible(true);
            accountPanel.setManaged(true);
            impostazioniPanel.setVisible(false);
            impostazioniPanel.setManaged(false);
            accountCorsiPanel.setVisible(false);
            accountCorsiPanel.setManaged(false);
        });

        this.setFocusPropreties(accountButton);
        this.setOnMouseTraverse(accountButton,accountPanel);

        return accountButton;
    }
    private Button createCorsiButton(){
        Button corsiButton = new Button("Corsi");
        corsiButton.setStyle("-fx-background-color: #3A6698; -fx-text-fill: WHITE;");
        corsiButton.setOnAction(e -> {
            corsiButton.setStyle("-fx-background-color: WHITE; -fx-text-fill:#3A6698;");
            corsiButton.setBorder(new Border(new BorderStroke(
                    Color.valueOf("#3A6698"),
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    new BorderWidths(0,0,1, 0)
            )));
            accountButton.setStyle("-fx-background-color: #3A6698; -fx-text-fill: WHITE;");
            impostazioniButton.setStyle("-fx-background-color: #3A6698; -fx-text-fill: WHITE;");
            accountPanel.setVisible(false);
            accountPanel.setManaged(false);
            impostazioniPanel.setVisible(false);
            impostazioniPanel.setManaged(false);
            accountCorsiPanel.setVisible(true);
            accountCorsiPanel.setManaged(true);
        });

        this.setFocusPropreties(corsiButton);
        this.setOnMouseTraverse(corsiButton, accountCorsiPanel);
        return corsiButton;
    }
    private Button createImpostazioniButton(){
        Button impostazioniButton = new Button("Impostazioni");
        impostazioniButton.setStyle("-fx-background-color: #3A6698; -fx-text-fill: WHITE;");
        impostazioniButton.setOnAction(e -> {
            impostazioniButton.setStyle("-fx-background-color: WHITE; -fx-text-fill: #3A6698;");
            impostazioniButton.setBorder(new Border(new BorderStroke(
                    Color.valueOf("#3A6698"),
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    new BorderWidths(0,0,1, 0)
            )));
            corsiButton.setStyle("-fx-background-color: #3A6698; -fx-text-fill: WHITE;");
            accountButton.setStyle("-fx-background-color: #3A6698; -fx-text-fill: WHITE;");
            impostazioniPanel.setVisible(true);
            impostazioniPanel.setManaged(true);
            accountPanel.setVisible(false);
            accountPanel.setManaged(false);
            accountCorsiPanel.setVisible(false);
            accountCorsiPanel.setManaged(false);
        });

        this.setFocusPropreties(impostazioniButton);
        this.setOnMouseTraverse(impostazioniButton, impostazioniPanel);
        return impostazioniButton;
    }


    private void setOnMouseTraverse(Button button, Pane panel) {
        button.setOnMouseEntered(e -> {
                    button.setStyle("-fx-background-color: WHITE;-fx-text-fill: \"#3A6698\";");
                    button.setBorder(new Border(new BorderStroke(
                            Color.valueOf("#3A6698"),
                            BorderStrokeStyle.SOLID,
                            CornerRadii.EMPTY,
                            new BorderWidths(0, 0, 1, 0)
                    )));
                }
        );
        button.setOnMouseExited(e -> {
                    if(!panel.isVisible()){
                        button.setStyle("-fx-background-color: \"#3A6698\";-fx-text-fill: WHITE;");
                    }
                }
        );
    }

    private void setFocusPropreties(Button button) {
        button.setFocusTraversable(true);
        button.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                button.setStyle("-fx-background-color: WHITE;-fx-text-fill: \"#3A6698\"; -fx-cursor: hand;");
                button.setBorder(new Border(new BorderStroke(
                        Color.valueOf("#3A6698"),
                        BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        new BorderWidths(0, 0, 1, 0)
                )));
                button.fire();
            } else {
                button.setStyle("-fx-background-color: \"#3A6698\";-fx-text-fill: WHITE;");
            }

        });

    }

}
