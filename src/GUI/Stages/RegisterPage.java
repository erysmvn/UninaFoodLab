package GUI.Stages;

import Controller.Controller;
import Entity.*;
import GUI.Pane.*;
import GUI.Buttons.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RegisterPage extends Stage {

    private Controller controller;
    private VBox root;
    private Label lblMatricola;
    private TextField txtMatricola;
    private TextField txtEmail;
    private TextField txtNome;
    private TextField txtCognome;
    private PasswordField txtPassword;
    private PasswordField txtRipetiPassword;
    private Label lblNomeError;
    private Label lblCognomeError;
    private Label lblEmailError;
    private Label lblMatricolaError;
    private Label lblPasswordError;
    private Label lblRipetiPasswordError;

    public RegisterPage(Controller controller) {

        this.controller = controller;
        this.setRoot();
        this.setRootAestetics();

        Scene scene = new Scene(root, 850, 650);
        scene.setFill(Color.TRANSPARENT);
        scene.setOnKeyPressed(e->{
            if(e.isControlDown() && e.getCode()== KeyCode.W){
                this.close();
            }
        });

        this.initStyle(StageStyle.TRANSPARENT);
        this.setScene(scene);
    }

    private void setRoot(){
        Label lblCredenziali = new Label("Credenziali di accesso");
        lblCredenziali.setStyle("-fx-font-weight: bold; -fx-text-fill: #3A6698;");
        Label lblDati = new Label("Dati anagrafici");
        lblDati.setStyle("-fx-font-weight: bold; -fx-text-fill: #3A6698;");

        GridPane gridCredenziali = createGridCredenziali();
        GridPane gridDati = createGridDati();
        HBox topBox = createTopBox();
        HBox bottomBox = createBottomBox();

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        root = new VBox(15, topBox, lblCredenziali, gridCredenziali, lblDati, gridDati, spacer, bottomBox);
    }

    private void setRootAestetics(){
        root.setPadding(new Insets(15));
        root.setAlignment(Pos.TOP_LEFT);
        root.setBackground(new Background(
                new BackgroundFill(Color.WHITE, new CornerRadii(30), Insets.EMPTY)
        ));
        root.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(30),
                new BorderWidths(2)
        )));
    }

    private VBox createNomeBox() {
        Label lbl = new Label("Nome *");
        txtNome = new TextField();
        lblNomeError = new Label("");
        lblNomeError.setTextFill(Color.RED);
        return new VBox(5, lbl, txtNome, lblNomeError);
    }

    private VBox createCognomeBox() {
        Label lbl = new Label("Cognome *");
        txtCognome = new TextField();
        lblCognomeError = new Label("");
        lblCognomeError.setTextFill(Color.RED);
        return new VBox(5, lbl, txtCognome, lblCognomeError);
    }

    private VBox createEmailBox() {
        Label lbl = new Label("Email *");
        txtEmail = new TextField();
        lblEmailError = new Label("");
        lblEmailError.setTextFill(Color.RED);

        txtEmail.textProperty().addListener((obs, oldValue, newValue) -> {
            boolean isStudente = newValue.contains("@studenti");
            lblMatricola.setVisible(isStudente);
            txtMatricola.setVisible(isStudente);
            lblMatricola.setManaged(isStudente);
            txtMatricola.setManaged(isStudente);
        });

        return new VBox(5, lbl, txtEmail, lblEmailError);
    }

    private VBox createMatricolaBox() {
        lblMatricola = new Label("Matricola *");
        txtMatricola = new TextField();
        lblMatricolaError = new Label("");
        lblMatricolaError.setTextFill(Color.RED);
        lblMatricola.setVisible(false);
        txtMatricola.setVisible(false);
        lblMatricola.setManaged(false);
        txtMatricola.setManaged(false);
        return new VBox(5, lblMatricola, txtMatricola, lblMatricolaError);
    }

    private VBox createPasswordBox() {
        Label lbl = new Label("Password *");
        txtPassword = new PasswordField();
        lblPasswordError = new Label("");
        lblPasswordError.setTextFill(Color.RED);
        txtPassword.setStyle(
                "-fx-display-caret: true;" +
                        "-fx-echo-char: '*';"
        );


        return new VBox(5, lbl, txtPassword, lblPasswordError);
    }

    private VBox createRipetiPasswordBox() {
        Label lbl = new Label("Ripeti Password *");
        txtRipetiPassword = new PasswordField();
        lblRipetiPasswordError = new Label("");
        lblRipetiPasswordError.setTextFill(Color.RED);
        return new VBox(5, lbl, txtRipetiPassword, lblRipetiPasswordError);
    }

    private GridPane createGridDati() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(createNomeBox(), 0, 0);
        grid.add(createCognomeBox(), 1, 0);

        return grid;
    }

    private GridPane createGridCredenziali() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(createEmailBox(), 0, 0, 2, 1);
        grid.add(createMatricolaBox(), 2, 0, 2, 1);
        grid.add(createPasswordBox(), 0, 1, 2, 1);
        grid.add(createRipetiPasswordBox(), 2, 1, 2, 1);

        return grid;
    }



    private HBox createTopBox() {
        HBox topBox = new HBox(5);
        topBox.setAlignment(Pos.TOP_RIGHT);
        topBox.setSpacing(10);

        Label labelBenvenuto = new Label("Benvenuto!");
        labelBenvenuto.setStyle("-fx-font-weight: bold; -fx-text-fill: #3A6698;");
        labelBenvenuto.setFont(Font.font("", 45));
        labelBenvenuto.setAlignment(Pos.CENTER);

        CircleButton closeButton = createCloseButton();
        CircleButton minimizeButton = createMinimizeButton();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBox.getChildren().addAll(labelBenvenuto, spacer, minimizeButton, closeButton);

        return topBox;
    }

    private HBox createBottomBox() {
        HBox bottomBox = new HBox(50);
        Button indietroButton = createIndietroButton();
        Button confermaButton = createConfermaButton();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        bottomBox.setAlignment(Pos.BOTTOM_CENTER);
        bottomBox.getChildren().addAll(indietroButton, spacer, confermaButton);

        return bottomBox;
    }

    private Button createConfermaButton() {
        Button confermaButton = new Button("Conferma");
        confermaButton.setTextFill(Color.WHITE);
        confermaButton.setStyle("-fx-background-color: #3A6698;");

        confermaButton.setOnAction(e -> {{
            if(validConferma()){
                //todo insert nel database
            }
        }
        });
        this.setOnMouseTraverse(confermaButton);
        this.setFocusPropreties(confermaButton);

        return confermaButton;
    }

    private Boolean validConferma() {
        boolean valid = true;

        if (txtNome.getText().trim().isEmpty()) {
            valid = false;
            txtNome.setStyle("-fx-border-color: red;");
            lblNomeError.setText("Inserire nome");
        } else {
            txtNome.setStyle(null);
            lblNomeError.setText("");
        }

        if (txtCognome.getText().trim().isEmpty()) {
            valid = false;
            txtCognome.setStyle("-fx-border-color: red;");
            lblCognomeError.setText("Inserire cognome");
        } else {
            txtCognome.setStyle(null);
            lblCognomeError.setText("");
        }

        if (txtEmail.getText().trim().isEmpty() || !txtEmail.getText().contains("@")) {
            valid = false;
            txtEmail.setStyle("-fx-border-color: red;");
            lblEmailError.setText("Inserire email valida");
        } else {
            txtEmail.setStyle(null);
            lblEmailError.setText("");
        }

        if (txtEmail.getText().contains("@studenti") && txtMatricola.isVisible() && txtMatricola.getText().trim().isEmpty()) {
            valid = false;
            txtMatricola.setStyle("-fx-border-color: red;");
            lblMatricolaError.setText("Inserire matricola");
        } else {
            txtMatricola.setStyle(null);
            lblMatricolaError.setText("");
        }

        if (txtPassword.getText().trim().isEmpty()) {
            valid = false;
            txtPassword.setStyle("-fx-border-color: red;");
            lblPasswordError.setText("Inserire password");
        } else {
            txtPassword.setStyle(null);
            lblPasswordError.setText("");
        }

        if (txtRipetiPassword.getText().trim().isEmpty() && !txtPassword.getText().trim().isEmpty()) {
            valid = false;
            txtRipetiPassword.setStyle("-fx-border-color: red;");
            lblRipetiPasswordError.setText("Le password non coincidono");
        } else if(!txtPassword.getText().trim().isEmpty() &&  !txtRipetiPassword.getText().trim().isEmpty() && !txtPassword.getText().equals(txtRipetiPassword.getText())){
            valid = false;
            txtRipetiPassword.setStyle("-fx-border-color: red;");
            lblRipetiPasswordError.setText("Le password non coincidono");
        }else{
            txtRipetiPassword.setStyle(null);
            lblRipetiPasswordError.setText("");
        }

        return valid;
    }

    private Button createIndietroButton() {
        Button indietroButton = new Button("Indietro");
        indietroButton.setTextFill(Color.WHITE);
        indietroButton.setStyle("-fx-background-color: #3A6698;");

        indietroButton.setOnAction(e -> {
            controller.openLoginPage();
            this.close();
        });

        this.setFocusPropreties(indietroButton);
        this.setOnMouseTraverse(indietroButton);

        return indietroButton;
    }

    private CircleButton createMinimizeButton() {
        CircleButton minimizeButton = new CircleButton();
        minimizeButton.setToMinimizeButtonWithAction(this);
        return minimizeButton;
    }

    private CircleButton createCloseButton() {
        CircleButton minimizeButton = new CircleButton();
        minimizeButton.setToCloseButtonWithAction(this);
        return minimizeButton;
    }

    private void setOnMouseTraverse(Button button) {
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
                    button.setStyle("-fx-background-color: \"#3A6698\";-fx-text-fill: WHITE;");
                }
        );
    }

    private void setFocusPropreties(Button button) {
        button.setFocusTraversable(true);
        button.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                button.setStyle("-fx-background-color: WHITE;-fx-text-fill: \"#3A6698\";");
                button.setBorder(new Border(new BorderStroke(
                        Color.valueOf("#3A6698"),
                        BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        new BorderWidths(0, 0, 1, 0)
                )));
            } else {
                button.setStyle("-fx-background-color: \"#3A6698\";-fx-text-fill: WHITE;");
            }

        });

    }
}
