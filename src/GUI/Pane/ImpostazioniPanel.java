package GUI.Pane;

import Controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.Objects;

public class ImpostazioniPanel extends VBox {
    Controller controller;
    public ImpostazioniPanel(Controller controller) {
        this.setSpacing(20);
        this.setPadding(new Insets(30));
        this.setAlignment(Pos.CENTER_LEFT);
        
        ImageView logoView = createLogoView();
        Button profiloButton = setAestheticsButton("Modifica Profilo");
        Button supportoButton = setAestheticsButton("Supporto");
        Button logoutButton = setAestheticsButton("Logout");
        logoutButton.setOnAction(e -> {
            controller.logOut();

        });



        this.getChildren().addAll(
                logoView,
                profiloButton,
                supportoButton,
                logoutButton
        );
    }

    private ImageView createLogoView() {
        Image logoImage = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/Images/uninaLogoAccountPage.png")
        ));
        ImageView logoView = new ImageView(logoImage);
        
        logoView.setFitWidth(850);
        logoView.setFitHeight(200);
        logoView.setPreserveRatio(true);
        return logoView;
    }

    private Button setAestheticsButton(String nomeButton) {
        Button button = new Button(nomeButton);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #3a6698; -fx-font-size: 30px; -fx-font-weight: bold;");
        this.setFocusPropreties(button);
        this.setOnMouseTraverse(button);
        return button;
    }


    private void setOnMouseTraverse(Button button) {
        button.setOnMouseEntered(e -> {
                    button.setBorder(new Border(new BorderStroke(
                            Color.valueOf("#3A6698"),
                            BorderStrokeStyle.SOLID,
                            CornerRadii.EMPTY,
                            new BorderWidths(0, 0, 1, 0)
                    )));
                }
        );
        button.setOnMouseExited(e -> {
                    button.setBorder(new Border(new BorderStroke(
                            Color.valueOf("#3A6698"),
                            BorderStrokeStyle.SOLID,
                            CornerRadii.EMPTY,
                            new BorderWidths( 0)
                    )));
                }
        );
    }

    private void setFocusPropreties(Button button) {
        button.setFocusTraversable(true);
        button.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                button.setBorder(new Border(new BorderStroke(
                        Color.valueOf("#3A6698"),
                        BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        new BorderWidths(0, 0, 1, 0)
                )));
            } else {
                button.setBorder(new Border(new BorderStroke(
                        Color.valueOf("#3A6698"),
                        BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        new BorderWidths( 0)
                )));
            }

        });

    }

}
