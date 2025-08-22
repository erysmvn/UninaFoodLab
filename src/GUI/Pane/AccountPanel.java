package GUI.Pane;

import Entity.*;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Objects;

public class AccountPanel extends VBox {

    private Label passwordValue;

    public AccountPanel(Utente utente) {
        this.setPadding(new Insets(20));
        this.setSpacing(15);
        GridPane grid = creaGrigliaAccount(utente);
        this.getChildren().add(grid);
    }

    private ImageView createLogoView() {
        javafx.scene.image.Image logoImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Media/uninaLogoAccountPage.png")));
        ImageView logoView = new ImageView(logoImage);
        logoView.setFitWidth(850);
        logoView.setFitHeight(200);
        logoView.setPreserveRatio(true);
        return logoView;
    }
    private GridPane creaGrigliaAccount(Utente utente) {

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(50);

        ImageView logoView = createLogoView();
        grid.add(logoView, 0, 0, 3, 1);

        Label nomeLabel = setMetadataAestetics(new Label("Nome"));
        Label cognomeLabel = setMetadataAestetics(new Label("Cognome"));
        Label emailLabel = setMetadataAestetics(new Label("Email"));
        Label passwordLabel = setMetadataAestetics(new Label("Password"));

        Label nomeValue = setAesteticsInfoLabel(new Label(utente.getNome()));
        Label cognomeValue = setAesteticsInfoLabel(new Label(utente.getCognome()));
        Label emailValue = setAesteticsInfoLabel(new Label(utente.getEmail()));
        Label passwordValue = setAesteticsInfoLabel( new Label(maskPassword(utente.getPassw()) ) );

        Button buttonPassword = new Button("👁");
        buttonPassword.setBackground(Background.EMPTY);
        buttonPassword.setTextFill(Color.valueOf("#3A6698"));
        buttonPassword.setOnAction(e -> {
            if (passwordValue.getText().contains("*")) {
                passwordValue.setText(utente.getPassw());
            } else {
                passwordValue.setText(maskPassword(utente.getPassw()));
            }
        });

        int rowIndex = 1;
        if (utente instanceof Studente) {
            Label matricolaLabel = setMetadataAestetics(new Label("Matricola"));
            Label matricolaValue = setAesteticsInfoLabel(new Label(((Studente) utente).getMatricola()));
            grid.add(matricolaLabel, 0, rowIndex);
            grid.add(matricolaValue, 1, rowIndex);
            rowIndex++;
        }
        grid.add(nomeLabel, 0, rowIndex);grid.add(nomeValue, 1, rowIndex++);
        grid.add(cognomeLabel, 0, rowIndex);grid.add(cognomeValue, 1, rowIndex++);
        grid.add(emailLabel, 0, rowIndex);grid.add(emailValue, 1, rowIndex++);
        grid.add(passwordLabel, 0, rowIndex);grid.add(passwordValue, 1, rowIndex);grid.add(buttonPassword, 2, rowIndex);

        return grid;
    }

    public Label setAesteticsInfoLabel(Label label) {
        Font valueFont = Font.font("Arial", 20);
        label.setFont(valueFont);
        return label;
    }

    public Label setMetadataAestetics(Label label){
        Font labelFont = Font.font("Nimbus Roman", 25);
        Color labelColor = Color.valueOf("#3A6698");
        label.setFont(labelFont);
        label.setTextFill(labelColor);
        return label;
    }

    private String maskPassword(String password) {
        return "*".repeat(password.length());
    }
}
