package GUI.Stages.CreateStages;

import Controller.Controller;
import Entity.*;
import Exception.UserExceptions.ChangePasswordException.passwordTroppoCortaException;
import Exception.UserExceptions.RegisterException.*;
import GUI.Buttons.*;

import GUI.Stages.MyStage;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.SQLException;

public class RegisterPage extends MyStage {
    private Controller controller;

    private VBox root;

    private Label lblMatricola;
    private Label lblNomeError;
    private Label lblCognomeError;
    private Label lblEmailError;
    private Label lblMatricolaError;
    private Label lblPasswordError;
    private Label lblRipetiPasswordError;
    private Label inserimentoErratoLabel;

    private TextField txtMatricola;
    private TextField txtEmail;
    private TextField txtNome;
    private TextField txtCognome;

    private PasswordField txtPassword;
    private PasswordField txtRipetiPassword;


    public RegisterPage(Controller controller) {
        super(850, 650, RootType.VBOX);
        this.controller = controller;

        root = getRootVBox();
        setRootElements();

        this.addStylesheet("/Media/StyleSheets/fieldsAndBoxesStyle.css");
    }

    private void setRootElements(){
        Label lblCredenziali = new Label("Credenziali di accesso");
        lblCredenziali.setStyle("-fx-font-weight: bold; -fx-text-fill: #3A6698;");
        Label lblDati = new Label("Dati anagrafici");
        lblDati.setStyle("-fx-font-weight: bold; -fx-text-fill: #3A6698;");

        GridPane gridCredenziali = createGridCredenziali();
        GridPane gridDati = createGridDati();
        HBox topBox = createTopBox();
        HBox funcBox = createFunctionalityButtonBox();
        HBox bottomBox = createBottomBox();

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        Region spacer1 = new Region();
        VBox.setVgrow(spacer1, Priority.ALWAYS);
        Region spacer2 = new Region();
        VBox.setVgrow(spacer2, Priority.ALWAYS);

        root.setPadding(new Insets(20, 20, 50, 50));
        root.setSpacing(15);
        root.getChildren().addAll(funcBox, spacer1, topBox, spacer2, lblCredenziali, gridCredenziali, lblDati, gridDati, spacer, bottomBox);
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
            boolean isStudente = newValue.contains("@studenti.unina.it");
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
        topBox.setAlignment(Pos.TOP_LEFT);
        topBox.setSpacing(10);

        Label labelBenvenuto = new Label("Benvenuto!");
        labelBenvenuto.setStyle("-fx-font-weight: bold; -fx-text-fill: #3A6698;");
        Font robotoFont = Font.loadFont(
                getClass().getResourceAsStream("/Media/Fonts/Roboto.ttf"),
                45
        );
        labelBenvenuto.setFont(robotoFont);
        labelBenvenuto.setAlignment(Pos.CENTER);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topBox.getChildren().addAll(labelBenvenuto, spacer);

        return topBox;
    }

    public HBox createFunctionalityButtonBox() {
        HBox funcBox = new HBox(5);
        funcBox.setAlignment(Pos.TOP_RIGHT);
        funcBox.setSpacing(10);

        CircleButton closeButton = createCloseButton();
        CircleButton minimizeButton = createMinimizeButton();

        funcBox.getChildren().addAll(minimizeButton, closeButton);
        return funcBox;
    }

    private HBox createBottomBox() {
        HBox bottomBox = new HBox(20);
        Button indietroButton = createIndietroButton();
        Button confermaButton = createConfermaButton();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        Region spacer3 = new Region();
        spacer3.setMaxWidth(10);


        inserimentoErratoLabel = new Label("Errore inserimento dati. Riprovare più tardi.");
        inserimentoErratoLabel.setTextFill(Color.RED);
        inserimentoErratoLabel.setVisible(false);
        bottomBox.setAlignment(Pos.BOTTOM_CENTER);
        bottomBox.getChildren().addAll(indietroButton, spacer,inserimentoErratoLabel, spacer2,confermaButton, spacer3);

        return bottomBox;
    }

    private Button createConfermaButton() {
        MyButton confermaButton = new MyButton("Conferma", MyButton.ButtonType.PRIMARY);

        confermaButton.setOnAction(e -> {{
            try {
                inserimentoErratoLabel.setVisible(false);
                validConferma();
                String nome = txtNome.getText();
                String cognome = txtCognome.getText();
                String email = txtEmail.getText();
                String matricola = txtMatricola.getText();
                String password = txtPassword.getText();
                try {
                    if (matricola == null || matricola.trim().isEmpty()) {
                        Chef newChef = new Chef(nome, cognome, email, password);
                        controller.registerMethod(newChef);
                    } else {
                        Studente newStudente = new Studente(matricola, nome, cognome, email, password);
                        System.out.println("TEST "+newStudente.getNome());
                        controller.registerMethod(newStudente);
                    }
                    this.close();
                } catch (SQLException exc) {
                    showDialog("Errore di sistema. Riprovare più tardi");
                }
            } catch (nameEmptyException NEE) {
                txtNome.setStyle("-fx-border-color: red;");
                lblNomeError.setText("Inserire nome");
            } catch (matricolaEmptyException MEE) {
                txtMatricola.setStyle("-fx-border-color: red;");
                lblMatricolaError.setText("Inserire matricola");
            } catch (matricolaNotValidException MNVE) {
                txtMatricola.setStyle("-fx-border-color: red;");
                lblMatricolaError.setText("Matricola non valida");
            } catch (surnameEmptyException SEE) {
                txtCognome.setStyle("-fx-border-color: red;");
                lblCognomeError.setText("Inserire cognome");
            } catch (emailEmptyException EE) {
                txtEmail.setStyle("-fx-border-color: red;");
                lblEmailError.setText("Inserire email");
            } catch (emailNotValidException ENVE) {
                txtEmail.setStyle("-fx-border-color: red;");
                lblEmailError.setText("Inserire email valida");
            } catch (passwordEmptyException PEN) {
                txtPassword.setStyle("-fx-border-color: red;");
                lblPasswordError.setText("Inserire password");
            } catch (passwordTroppoCortaException passwordTroppoCortaException) {
                txtPassword.setStyle("-fx-border-color: red;");
                lblPasswordError.setText("Password troppo corta");
            } catch (repeatPasswordEmptyException RPEE) {
                txtRipetiPassword.setStyle("-fx-border-color: red;");
                lblRipetiPasswordError.setText("Inserire nuovamente la password");
            } catch (repeatPasswordIsNotEqualException RPEE) {
                txtRipetiPassword.setStyle("-fx-border-color: red;");
                lblRipetiPasswordError.setText("Le password non coincidono");
            } catch (registerException RE) {
                txtNome.setStyle("-fx-border-color: red;");
                lblNomeError.setText("Inserire nome");
                txtCognome.setStyle("-fx-border-color: red;");
                lblCognomeError.setText("Inserire cognome");
                txtEmail.setStyle("-fx-border-color: red;");
                lblEmailError.setText("Inserire email");
                txtPassword.setStyle("-fx-border-color: red;");
                lblPasswordError.setText("Inserire password");
            }
        }});

        return confermaButton;
    }

    private void validConferma() {

        if (txtEmail.getText().trim().isEmpty() && txtPassword.getText().trim().isEmpty() && txtCognome.getText().trim().isEmpty() && txtNome.getText().trim().isEmpty()) {
            throw new registerException();
        }

        if (txtNome.getText().trim().isEmpty()) {
            throw new nameEmptyException();
        } else {
            txtNome.setStyle(null);
            lblNomeError.setText("");
        }

        if (txtCognome.getText().trim().isEmpty()) {
            throw new surnameEmptyException();
        } else {
            txtCognome.setStyle(null);
            lblCognomeError.setText("");
        }

        if (txtEmail.getText().trim().isEmpty()) {
            throw new emailEmptyException();
        } else {
            txtCognome.setStyle(null);
            lblCognomeError.setText("");
        }

        if (!txtEmail.getText().contains("@") || !txtEmail.getText().contains(".") ||
                txtEmail.getText().lastIndexOf('.') < txtEmail.getText().indexOf('@')) {
            throw new emailNotValidException();
        } else {
            txtEmail.setStyle(null);
            lblEmailError.setText("");
        }

        if (txtEmail.getText().contains("@studenti.unina.it") && txtMatricola.isVisible() && txtMatricola.getText().trim().isEmpty()) {
            throw new matricolaEmptyException();
        } else {
            txtMatricola.setStyle(null);
            lblMatricolaError.setText("");
        }

        String pattern = "^[A-Za-z][0-9]{8}$";
        if (txtEmail.getText().endsWith("@studenti.unina.it") && txtMatricola.isVisible()) {
            String matricola = txtMatricola.getText();

            if (!matricola.matches(pattern)) {
                throw new matricolaNotValidException();
            } else {
                txtMatricola.setStyle(null);
                lblMatricolaError.setText("");
            }
        }

        if (txtPassword.getText().trim().isEmpty()) {
            throw new passwordEmptyException();
        } else {
            txtPassword.setStyle(null);
            lblPasswordError.setText("");
        }

        if(txtPassword.getText().length()<7){
            throw new passwordTroppoCortaException();
        }else{
            txtPassword.setStyle(null);
            lblPasswordError.setText("");
        }

        if (txtRipetiPassword.getText().trim().isEmpty() && !txtPassword.getText().trim().isEmpty()) {
            throw new repeatPasswordEmptyException();
        } else if(!txtPassword.getText().trim().isEmpty() &&  !txtRipetiPassword.getText().trim().isEmpty() && !txtPassword.getText().equals(txtRipetiPassword.getText())){
            throw new repeatPasswordIsNotEqualException();
        }else{
            txtRipetiPassword.setStyle(null);
            lblRipetiPasswordError.setText("");
        }
    }

    private MyButton createIndietroButton() {
        MyButton indietroButton = new MyButton("Indietro", MyButton.ButtonType.PRIMARY);

        indietroButton.setOnAction(e -> {
            controller.openLoginPage();
            this.close();
        });

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
}
