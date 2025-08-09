package GUI;

import Controller.Controller;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class RegisterForm extends Stage {

    private Controller controller;

    public RegisterForm(Controller controller) {
        this.controller = controller;

        // Layout principale
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        // Campi matricola, nome, cognome, email e password
        TextField matrField = new TextField();
        matrField.setPromptText("Matricola");

        TextField nameField = new TextField();
        nameField.setPromptText("Nome");

        TextField surnameField = new TextField();
        surnameField.setPromptText("Cognome");

        TextField mailField = new TextField();
        mailField.setPromptText("Email");

        PasswordField pswField = new PasswordField();
        pswField.setPromptText("Password");

        // Bottone di login
        Button doneButton = new Button("Register");

        doneButton.setOnAction(e -> {
            try {
                String matricola = matrField.getText();
                String name = nameField.getText();
                String surname = surnameField.getText();
                String email = mailField.getText();
                String psw = pswField.getText();

                boolean check = controller.addNewStudent(matricola, name, surname, email, psw);
                Alert alert;
                if (check) {
                    alert = new Alert(Alert.AlertType.INFORMATION, "Student added");
                } else {
                    alert = new Alert(Alert.AlertType.ERROR, "ERROR");
                }
                alert.showAndWait();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        root.getChildren().addAll(matrField, nameField, surnameField, mailField, pswField, doneButton);

        Scene scene = new Scene(root, 300, 150);
        this.setScene(scene);
        this.setTitle("Login Form");
    }
}