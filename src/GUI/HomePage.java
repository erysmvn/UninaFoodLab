package GUI;

import Controller.Controller;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class HomePage extends VBox {
    private Controller controller;

    private Button loginButton;

    public HomePage(Controller controller) {
        this.controller = controller;

        loginButton = new Button("Login");

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.loginStudenteClicked();
                System.out.println("Login Button pressed");
            }
        });

        this.getChildren().add(loginButton);
        // Puoi aggiungere altri componenti qui nel VBox
    }
}
