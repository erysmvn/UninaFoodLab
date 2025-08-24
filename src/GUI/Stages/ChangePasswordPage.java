package GUI.Stages;

import Controller.Controller;
import Entity.Chef;
import Entity.Studente;
import GUI.Buttons.CircleButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.sql.SQLException;

public class ChangePasswordPage extends Stage {

        private Controller controller;
        private VBox root;
        private TextField txtVecchiaPassword;
        private PasswordField txtNuovaPassword;
        private PasswordField txtRipetiPassword;
        private Label lblNomeError;
        private Label lblCognomeError;
        private Label lblEmailError;
        private Label lblMatricolaError;
        private Label lblPasswordError;
        private Label lblRipetiPasswordError;

        public ChangePasswordPage(Controller controller) {

            this.controller = controller;
            this.setRoot();
            this.setRootAesthetics();

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
            Label lblDati = new Label("Modifica Password");
            lblDati.setStyle("-fx-font-weight: bold; -fx-text-fill: #3A6698;");

            GridPane gridDati = createGridDati();
            HBox bottomBox = createBottomBox();

            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);

            root = new VBox(15, lblDati, gridDati, spacer, bottomBox);
        }

        private void setRootAesthetics(){
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


        private VBox createVecchiaPasswordBox() {
            Label lbl = new Label("Vecchia Password *");
            txtVecchiaPassword = new PasswordField();
            lblPasswordError = new Label("");
            lblPasswordError.setTextFill(Color.RED);
            txtVecchiaPassword.setStyle(
                    "-fx-display-caret: true;" +
                            "-fx-echo-char: '*';"
            );

            return new VBox(5, lbl, txtVecchiaPassword, lblPasswordError);
        }

        private VBox createNuovaPasswordBox() {
            Label lbl = new Label("Nuova Password *");
            txtNuovaPassword = new PasswordField();
            lblPasswordError = new Label("");
            lblPasswordError.setTextFill(Color.RED);
            txtNuovaPassword.setStyle(
                    "-fx-display-caret: true;" +
                            "-fx-echo-char: '*';"
            );

            return new VBox(5, lbl, txtNuovaPassword, lblPasswordError);
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

            grid.add(createVecchiaPasswordBox(), 0, 0);
            grid.add(createNuovaPasswordBox(), 1, 0);
            grid.add(createRipetiPasswordBox(), 1, 1);

            return grid;
        }


        private HBox createBottomBox() {
            HBox bottomBox = new HBox(50);
            Button confermaButton = createConfermaButton();

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            bottomBox.setAlignment(Pos.BOTTOM_CENTER);
            bottomBox.getChildren().add(confermaButton);

            return bottomBox;
        }

        private Button createConfermaButton() {
            Button confermaButton = new Button("Conferma");
            confermaButton.setTextFill(Color.WHITE);
            confermaButton.setStyle("-fx-background-color: #3A6698;");

            confermaButton.setOnAction(e -> {{
                if(validConferma()){
                    String nuovaPassword = txtNuovaPassword.getText();

                    try {
                        controller.changeUserPassword(nuovaPassword);
                        this.close();
                    } catch (Exception exc) {
                        exc.printStackTrace();
                    }
                }
            }
            });
            this.setOnMouseTraverse(confermaButton);
            this.setFocusProperties(confermaButton);

            return confermaButton;
        }

        private Boolean validConferma() {
            boolean valid = true;

            if (txtVecchiaPassword.getText().trim().isEmpty()) {
                valid = false;
                txtVecchiaPassword.setStyle("-fx-border-color: red;");
                lblNomeError.setText("Inserire vecchia password");
            } else if (!controller.checkOldPassword(txtVecchiaPassword.getText())) {
                valid = false;
                txtVecchiaPassword.setStyle("-fx-border-color: red;");
                lblNomeError.setText("Password errata");
            }
            else {
                txtVecchiaPassword.setStyle(null);
                lblNomeError.setText("");
            }

            if (txtNuovaPassword.getText().trim().isEmpty()) {
                valid = false;
                txtNuovaPassword.setStyle("-fx-border-color: red;");
                lblCognomeError.setText("Inserire nuova password");
            } else {
                txtNuovaPassword.setStyle(null);
                lblCognomeError.setText("");
            }

            if (txtRipetiPassword.getText().trim().isEmpty() && !txtNuovaPassword.getText().trim().isEmpty()) {
                valid = false;
                txtRipetiPassword.setStyle("-fx-border-color: red;");
                lblRipetiPasswordError.setText("Le password non coincidono");
            } else if (!txtNuovaPassword.getText().trim().isEmpty() &&  !txtRipetiPassword.getText().trim().isEmpty() && !txtNuovaPassword.getText().equals(txtRipetiPassword.getText())){
                valid = false;
                txtRipetiPassword.setStyle("-fx-border-color: red;");
                lblRipetiPasswordError.setText("Le password non coincidono");
            }else{
                txtRipetiPassword.setStyle(null);
                lblRipetiPasswordError.setText("");
            }

            return valid;
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

        private void setFocusProperties(Button button) {
            button.setFocusTraversable(true);
            button.focusedProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue) {
                    button.setStyle("-fx-background-color: WHITE; -fx-text-fill: \"#3A6698\"; -fx-cursor: hand;");
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

    private void styleConfirmButton(Button button, Color color) {
        button.setPrefSize(80, 30);
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setBackground(new Background(new BackgroundFill(color, new CornerRadii(8), Insets.EMPTY)));
        button.setCursor(Cursor.HAND);

        button.setOnMouseEntered(e -> button.setOpacity(0.8));
        button.setOnMouseExited(e -> button.setOpacity(1.0));
    }
}
