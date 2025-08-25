package GUI.Pane;

import Controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Objects;
import Exception.*;

public class ImpostazioniPanel extends VBox {
    Controller controller;
    Label supportoError;

    public ImpostazioniPanel(Controller controller) {
        this.controller = controller;

        setSpacing(30);
        setPadding(new Insets(40));
        setAlignment(Pos.TOP_CENTER);

        ImageView logoView = createLogoView();
        setSupportoError();

        Button modificaPassword = setAestheticsButton("Modifica Password");
        modificaPassword.setOnAction(e -> controller.openModificaPassword());

        Button supportoButton = setAestheticsButton("Supporto");
        supportoButton.setOnAction(e -> {
            try {
                controller.openEmail(
                        "supportfoodlab@uninasupport.it",
                        "Richiesta Supporto",
                        "Ciao,\nho bisogno di assistenza per..."
                );
            } catch (emailClientNotFound ex) {
                showSupportoErrorLabel(ex);
            }
        });

        Button logoutButton = setAestheticsButton("Logout");
        logoutButton.setOnAction(e -> controller.logOut());

        VBox supportoBox = new VBox(2, supportoButton, supportoError);
        supportoBox.setAlignment(Pos.CENTER);

        VBox.setMargin(modificaPassword, new Insets(10, 0, 0, 0));
        VBox.setMargin(supportoBox, new Insets(10, 0, 0, 0));
        VBox.setMargin(logoutButton, new Insets(10, 0, 0, 0));

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Region spacer1 = new Region();
        VBox.setVgrow(spacer1, Priority.ALWAYS);

        Region spacer2 = new Region();
        VBox.setVgrow(spacer2, Priority.ALWAYS);

        this.getChildren().addAll(
                logoView,
                spacer,
                spacer1,
                modificaPassword,
                supportoButton,
                logoutButton,
                spacer2
        );
    }
    private void setSupportoError(){
        supportoError = new Label();
        supportoError.setTextFill(Color.RED);
        supportoError.setFont(Font.font(14));
        supportoError.setText(" ");
    }

    private void showSupportoErrorLabel(emailClientNotFound ECN){
        supportoError.setText(ECN.getMessage());
    }

    private ImageView createLogoView() {
        Image logoImage = new Image(Objects.requireNonNull(
                getClass().getResourceAsStream("/Media/Logos/LogoHomePage.png")
        ));
        ImageView logoView = new ImageView(logoImage);

        logoView.setFitWidth(850);
        logoView.setFitHeight(200);
        logoView.setPreserveRatio(true);
        return logoView;
    }

    private Button setAestheticsButton(String nomeButton) {
        Button button = new Button(nomeButton);
        Font robotoFont = Font.loadFont(
                getClass().getResourceAsStream("/Media/Fonts/Roboto.ttf"),
                20
        );
        button.setFont(robotoFont);
        button.setStyle("-fx-background-color: transparent; -fx-text-fill: #3a6698; -fx-font-size: 30px; -fx-cursor: hand;");
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
