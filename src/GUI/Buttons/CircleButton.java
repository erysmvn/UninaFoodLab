package GUI.Buttons;

import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CircleButton extends Button {

    public CircleButton() {
        this.setShape(new Circle(25));
        this.setMinSize(21, 21);
        this.setMaxSize(21, 21);
        this.setFocusTraversable(false);
    }

    public void setToMinimizeButton() {
        this.setText("-");
        this.setStyle("-fx-background-color: WHITE");
        this.setTextFill(Color.GREY);
        this.setBorder(new Border(new BorderStroke(
                Color.GRAY,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(1)
        )));
    }

    public void setToMinimizeButtonWithAction(Stage stage) {
        this.setToMinimizeButton();
        this.setOnAction(event -> {
            stage.setIconified(true);
        });
    }

    public void setToCloseButton() {
        this.setText("x");
        this.setFont(Font.font("", 10));
        this.setStyle("-fx-background-color: WHITE");
        this.setTextFill(Color.RED);
        this.setBorder(new Border(new BorderStroke(
                Color.RED,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(1)
        )));
    }

    public void setToCloseButtonWithAction(Stage stage) {
        this.setToCloseButton();
        this.setOnAction(e -> stage.close());
    }

    public void setToCloseButtonWithAction(EventHandler<ActionEvent> action) {
        this.setToCloseButton();
        this.setOnAction(action);
    }

}
