package GUI.Stages;

import Controller.Controller;
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
import Exception.*;
public class ChangePasswordPage extends Stage {

        private Controller controller;
        private VBox root;
        private TextField txtVecchiaPassword;
        private PasswordField txtNuovaPassword;
        private PasswordField txtRipetiPassword;
        private Label lblVecchiaPasswordError;
        private Label lblNuovaPasswordError;
        private Label lblRipetiPasswordError;
        private Label lblErroreInserimentoDB;

        public ChangePasswordPage(Controller controller) {

            this.controller = controller;
            this.setRoot();
            this.setRootAesthetics();

            Scene scene = new Scene(root, 600, 400);
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
            Label lblDati = new Label(" Modifica Password");
            lblDati.setStyle("-fx-font-weight: bold; -fx-text-fill: #3A6698; -fx-font-size: 28;");

            GridPane gridDati = createGridDati();
            HBox bottomBox = createBottomBox();
            HBox funcBox = createFunctionalityButtonBox();

            Region spacer = new Region();
            VBox.setVgrow(spacer, Priority.ALWAYS);

            Region spacer1 = new Region();
            VBox.setVgrow(spacer1, Priority.ALWAYS);

            root = new VBox(15, funcBox, spacer1, lblDati, gridDati, spacer, bottomBox);
        }

        private void setRootAesthetics(){
            root.setPadding(new Insets(30, 30, 30, 30));
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
            lblVecchiaPasswordError = new Label("");
            lblVecchiaPasswordError.setTextFill(Color.RED);
            txtVecchiaPassword.setStyle(
                    "-fx-display-caret: true;" +
                            "-fx-echo-char: '*';"
            );

            return new VBox(5, lbl, txtVecchiaPassword, lblVecchiaPasswordError);
        }

        private VBox createNuovaPasswordBox() {
            Label lbl = new Label("Nuova Password *");
            txtNuovaPassword = new PasswordField();
            lblNuovaPasswordError = new Label("");
            lblNuovaPasswordError.setTextFill(Color.RED);
            txtNuovaPassword.setStyle(
                    "-fx-display-caret: true;" +
                            "-fx-echo-char: '*';"
            );

            return new VBox(5, lbl, txtNuovaPassword, lblNuovaPasswordError);
        }

        private VBox createRipetiPasswordBox() {
            Label lbl = new Label("Ripeti Password *");
            txtRipetiPassword = new PasswordField();
            lblRipetiPasswordError = new Label("");
            lblRipetiPasswordError.setTextFill(Color.RED);
            return new VBox(5, lbl, txtRipetiPassword, lblRipetiPasswordError);
        }

        private Label createErroreInserimentoLabel(){
            lblErroreInserimentoDB = new Label();
            lblErroreInserimentoDB.setTextFill(Color.RED);

            return lblErroreInserimentoDB;
        }

        private GridPane createGridDati() {
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(10));

            grid.add(createVecchiaPasswordBox(), 0, 0);
            grid.add(createNuovaPasswordBox(), 0, 1);
            grid.add(createRipetiPasswordBox(), 1, 1);
            grid.add(createErroreInserimentoLabel(), 0, 2);

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
                lblErroreInserimentoDB.setText("");
                try{
                    validConferma();
                    String nuovaPassword = txtNuovaPassword.getText();

                        controller.changeUserPassword(nuovaPassword);
                        this.close();

                }catch (oldPasswordErrorException OPEE){
                txtVecchiaPassword.setStyle("-fx-border-color: red;");
                lblVecchiaPasswordError.setText("Password errata");
                }catch (oldPasswordEmpty OPE){
                    txtVecchiaPassword.setStyle("-fx-border-color: red;");
                    lblVecchiaPasswordError.setText("Inserire vecchia password");
                }catch (newPasswordEmpty NPE) {
                    txtNuovaPassword.setStyle("-fx-border-color: red;");
                    lblNuovaPasswordError.setText("Inserire nuova password");
                }catch (passwordAndNewPasswordNotEqual PNPNE){
                    txtRipetiPassword.setStyle("-fx-border-color: red;");
                    lblRipetiPasswordError.setText("Le password non coincidono");
                }catch (SQLException sqlException){
                    lblErroreInserimentoDB.setText("Errore nell'inserimento dei dati");
                }

            }
            });
            this.styleButton(confermaButton, Color.valueOf("#3A6698"));

            return confermaButton;
        }

        private void validConferma() throws  changePasswordException{

                if (txtVecchiaPassword.getText().trim().isEmpty()) {
                    throw new oldPasswordEmpty();
                } else if (controller.checkOldPassword(txtVecchiaPassword.getText())){
                    txtVecchiaPassword.setStyle(null);
                    lblVecchiaPasswordError.setText("");
                }

                if (txtNuovaPassword.getText().trim().isEmpty()) {
                    throw new newPasswordEmpty();
                } else {
                    txtNuovaPassword.setStyle(null);
                    lblNuovaPasswordError.setText("");
                }

                if (txtRipetiPassword.getText().trim().isEmpty() && !txtNuovaPassword.getText().trim().isEmpty()) {
                    throw new passwordAndNewPasswordNotEqual();
                } else if (!txtNuovaPassword.getText().trim().isEmpty() &&  !txtRipetiPassword.getText().trim().isEmpty() && !txtNuovaPassword.getText().equals(txtRipetiPassword.getText())){
                    throw new passwordAndNewPasswordNotEqual();
                }else{
                    txtRipetiPassword.setStyle(null);
                    lblRipetiPasswordError.setText("");
                }

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

    private void styleButton(Button button, Color color) {
        button.setPrefSize(80, 30);
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setBackground(new Background(new BackgroundFill(color, new CornerRadii(8), Insets.EMPTY)));
        button.setCursor(Cursor.HAND);

        button.setOnMouseEntered(e -> button.setOpacity(0.8));
        button.setOnMouseExited(e -> button.setOpacity(1.0));
    }
}
