package GUI;

import Controller.Controller;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginForm extends Stage {

    private Controller controller;

    public LoginForm(Controller controller) {
        this.controller = controller;

        // Layout principale
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        // Campi email e password
        TextField txtField = new TextField();
        txtField.setPromptText("Email");

        PasswordField pswField = new PasswordField();
        pswField.setPromptText("Password");

        // Bottone di login
        Button doneButton = new Button("Login");

        doneButton.setOnAction(e -> {
            try {
                String email = txtField.getText();
                String psw = pswField.getText();

                boolean check = controller.validateLoginStudente(email, psw);
                Alert alert;
                if (check) {
                    alert = new Alert(Alert.AlertType.INFORMATION, "You have successfully logged in");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR, "Invalid email address or password");
                }
                alert.showAndWait();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(txtField, pswField, doneButton);

        Scene scene = new Scene(root, 300, 150);
        this.setScene(scene);
        this.setTitle("Login Form");
    }
}