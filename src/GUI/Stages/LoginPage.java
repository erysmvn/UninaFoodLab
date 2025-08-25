package GUI.Stages;

import Controller.Controller;
import GUI.Buttons.*;

import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.*;
import javafx.scene.image.*;

import java.sql.SQLException;
import java.util.Objects;

public class LoginPage extends Stage {

    BorderPane root;
    Scene scene;
    Controller controller;
    TextField emailField;
    TextField passwordField;
    Label passwordErrorLabel;
    Label emailErrorLabel;
    Label errorLoginLabel;


    public LoginPage(Controller controller) {
        this.controller = controller;

        root = new BorderPane();
        this.setRootAesthetics();
        root.setTop(createButtonsTopBox());
        root.setCenter(createCenterContent());

        scene = new Scene(root, 500, 725);
        scene.setFill(Color.TRANSPARENT);
        scene.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == KeyCode.W
                    && this.isShowing() && !this.isIconified()) {
                this.close();
            }
        });

        this.initStyle(StageStyle.TRANSPARENT);
        this.mostraInserireEmail();
        this.setScene(scene);
        this.show();
    }


    private VBox createCenterContent(){

        VBox centerBox = new VBox(20);
        centerBox.setAlignment(Pos.CENTER);

        ImageView logo = createLogo();
        VBox emailBox = createEmailBox();
        VBox passwordBox = createPasswordBox();

        errorLoginLabel = new Label();
        errorLoginLabel.setTextFill(Color.RED);

        Button loginButton = createButtonLogin();
        Button registerButton = createButtonRegister();
        Region spacer = new Region();
        spacer.setPrefHeight(8);
        Region spacer2 = new Region();
        spacer2.setPrefHeight(0.12);
        Region spacer3 = new Region();
        spacer3.setPrefHeight(7);

        centerBox.getChildren().addAll(
                logo,
                spacer3,
                spacer,
                emailBox,
                passwordBox,
                errorLoginLabel,
                loginButton,
                spacer2,
                registerButton
        );

        return centerBox;
    }


    private void setRootAesthetics() {
        root.setPadding(new Insets(20));
        root.setBackground(new Background(new BackgroundFill(
                Color.WHITE, new CornerRadii(30), Insets.EMPTY
        )));
        root.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(30),
                new BorderWidths(2)
        )));
    }

    private HBox createButtonsTopBox() {
        HBox buttonsTopBox = new HBox(5);
        buttonsTopBox.setAlignment(Pos.TOP_RIGHT);
        buttonsTopBox.setPadding(new Insets(3, 0, 0, 0));
        buttonsTopBox.getChildren().addAll(
                new CircleButton().setToMinimizeButtonWithAction(this),
                new CircleButton().setToCloseButtonWithAction(this)
        );
        return buttonsTopBox;
    }


    private VBox createPasswordBox() {
        passwordField = new PasswordField();
        passwordField.setFont(Font.font("Arial", 16));
        passwordField.setPrefSize(330, 30);
        passwordField.setMaxSize(330, 30);
        passwordField.setPromptText("Inserire password");
        passwordErrorLabel = new Label();
        passwordErrorLabel.setTextFill(Color.RED);

        VBox passwordBox = new VBox(5, passwordField, passwordErrorLabel);
        passwordBox.setAlignment(Pos.CENTER);

        return passwordBox;
    }

    private VBox createEmailBox() {
        emailField = new TextField();
        emailField.setFont(Font.font("Arial", 16));
        emailField.setPrefSize(330, 30);
        emailField.setMaxSize(330, 30);
        emailField.setPromptText("Inserire email");

        emailErrorLabel = new Label();
        emailErrorLabel.setTextFill(Color.RED);

        VBox emailBox = new VBox(5, emailField, emailErrorLabel);
        emailBox.setAlignment(Pos.CENTER);
        return emailBox;
    }

    private ImageView createLogo() {
        Image logoImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Media/Logos/LogoLoginPage.png")));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(450);
        logoView.setPreserveRatio(true);
        return logoView;
    }

    public void mostraInserireEmail() {
        root.requestFocus();
    }

    public void tryLogin(String email, String password) {

        if (email.isEmpty() && password.isEmpty()) {
            emailErrorLabel.setText("Inserire Email");
            passwordErrorLabel.setText("Inserire Password");
            errorLoginLabel.setText("");
        } else if (email.isEmpty() && !password.isEmpty()) {
            emailErrorLabel.setText("Inserire Email");
            passwordErrorLabel.setText("");
            errorLoginLabel.setText("");

        } else if (!email.isEmpty() && password.isEmpty()) {
            passwordErrorLabel.setText("Inserire Password");
            emailErrorLabel.setText("");
            errorLoginLabel.setText("");
        } else {
            emailErrorLabel.setText("");
            passwordErrorLabel.setText("");
            errorLoginLabel.setText("");
            doLogin(emailField.getText(), passwordField.getText());
        }
    }

    public Button createButtonLogin() {
        Button loginButton = new Button("Login");

        loginButton.setMinHeight(30);
        loginButton.setMaxHeight(30);

        loginButton.setOnAction(event -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            tryLogin(email, password);
        });

        this.styleButton(loginButton, Color.valueOf("#3A6698"));
        return loginButton;
    }

    public void doLogin(String email, String password) {
        try {
            controller.loginMethod(email, password);
            this.close();

        } catch (SQLException sqle) {
            showErrorLoginLabel();
        }
    }

    public void showErrorLoginLabel() {
        errorLoginLabel.setTextFill(Color.RED);
        errorLoginLabel.setText("ACCOUNT INESISTENTE");
    }

    public Button createButtonRegister() {
        Button registerButton = new Button("Register");

        registerButton.setMinHeight(30);
        registerButton.setMaxHeight(30);

        registerButton.setOnAction(event -> {
            controller.openRegisterPage();
            this.close();
        });

        this.styleButton(registerButton, Color.valueOf("#3A6698"));
        return registerButton;
    }

    private void styleButton(Button button, Color color) {
        button.setPrefSize(80, 40);
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setBackground(new Background(new BackgroundFill(color, new CornerRadii(8), Insets.EMPTY)));
        button.setCursor(Cursor.HAND);

        button.setOnMouseEntered(e -> button.setOpacity(0.8));
        button.setOnMouseExited(e -> button.setOpacity(1.0));
    }

}