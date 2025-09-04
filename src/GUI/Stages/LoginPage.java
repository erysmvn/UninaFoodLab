package GUI.Stages;

import Controller.Controller;
import Exception.UserExceptions.LoginException.emailNotFoundException;
import Exception.UserExceptions.LoginException.passwordErrataException;
import Exception.UserExceptions.RegisterException.emailEmptyException;
import Exception.UserExceptions.RegisterException.emailNotValidException;
import Exception.UserExceptions.RegisterException.passwordEmptyException;
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
    Controller controller;

    Scene scene;
    BorderPane root;

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

        scene.getStylesheets().add(
                getClass().getResource("/Media/StyleSheets/fieldsAndBoxesStyle.css").toExternalForm()
        );

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
        try {
            if (email.isEmpty() && password.isEmpty())
                throw new emailEmptyException();

            if (email.isEmpty())
                throw new emailEmptyException();
            else if (!email.contains("@") || !email.contains(".") || email.lastIndexOf('.') < email.indexOf('@')) {
                throw new emailNotValidException();
            }

            if (password.isEmpty())
                throw new passwordEmptyException();

            emailErrorLabel.setText("");
            passwordErrorLabel.setText("");
            errorLoginLabel.setText("");
            doLogin(emailField.getText(), passwordField.getText());

        } catch (emailEmptyException eee) {
            emailErrorLabel.setText("Inserire Email");
            emailField.setStyle("-fx-border-color: red;");

            if(password.isEmpty()) {
                passwordErrorLabel.setText("Inserire Password");
                passwordField.setStyle("-fx-border-color: red;");
            } else {
                passwordErrorLabel.setText("");
                passwordField.setStyle("");
            }

            errorLoginLabel.setText("");

        } catch (emailNotValidException eee) {
            emailErrorLabel.setText("Email non valida");
            emailField.setStyle("-fx-border-color: red;");

            passwordErrorLabel.setText("");
            passwordField.setStyle("");

            errorLoginLabel.setText("");
        } catch (passwordEmptyException pee) {
            passwordErrorLabel.setText("Inserire Password");
            passwordField.setStyle("-fx-border-color: red;");

            if(email.isEmpty()) {
               emailErrorLabel.setText("Inserire Email");
               emailField.setStyle("-fx-border-color: red;");
            } else {
               emailErrorLabel.setText("");
               emailField.setStyle("");
            }
            errorLoginLabel.setText("");
        }

    }

    public MyButton createButtonLogin() {
        MyButton loginButton = new MyButton("Login", MyButton.ButtonType.PRIMARY);

        loginButton.setOnAction(event -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            tryLogin(email, password);
        });

        return loginButton;
    }

    public void doLogin(String email, String password) {
        try {
            controller.loginMethod(email, password);
            this.close();
        } catch(emailNotFoundException emailExc){
            emailField.setStyle("");
            passwordField.setStyle("");
            errorLoginLabel.setText("ACCOUNT INESISTENTE");
        } catch (passwordErrataException passwordExc){
            passwordErrorLabel.setText("Password errata");
            passwordField.setStyle("-fx-border-color: red;");
        } catch (SQLException sqle) {
            showErrorLoginLabel();
        }
    }

    public void showErrorLoginLabel(){
        errorLoginLabel.setTextFill(Color.RED);
        errorLoginLabel.setText("Errore nel recuperare i dati. Riprovare più tardi");
    }

    public MyButton createButtonRegister(){
        MyButton registerButton = new MyButton("Register", MyButton.ButtonType.PRIMARY);

        registerButton.setOnAction(event -> {
            controller.openRegisterPage();
            this.close();
        });

        return registerButton;
    }

}