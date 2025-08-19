package GUI.Buttons;

import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class CircleButton  extends Button {

    public CircleButton() {
        this.setShape(new Circle(25));
        this.setMinSize(21, 21);
        this.setMaxSize(21, 21);
        this.setFocusTraversable(false);
    }


    public CircleButton setToMinimizeButton(){
        this.setText("-");
        this.setStyle("-fx-background-color: WHITE");
        this.setTextFill(Color.GREY);
        this.setBorder(new Border(new BorderStroke(
                Color.GRAY,
                BorderStrokeStyle.SOLID,
                CornerRadii.EMPTY,
                new BorderWidths(1)
        )));
        return this;

    }
    public CircleButton setToMinimizeButtonWithAction(Stage stage){
        this.setToMinimizeButton();
        this.setOnAction(event -> {
            stage.setIconified(true);
        });
        return this;
    }
    public CircleButton setToCloseButton() {
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
        return this;
    }
    public CircleButton setToCloseButtonWithAction(Stage stage) {
        this.setToCloseButton();
        this.setOnAction(e -> stage.close());
        return this;
    }
    public CircleButton setToCloseButtonWithAction(EventHandler<ActionEvent> action) {
        this.setToCloseButton();
        this.setOnAction(action);
        return this;
    }
}
