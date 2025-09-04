package GUI.Buttons;

import Exception.CorsoExceptions.imageNotFoundException;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Objects;

public class MyButton extends Button {

    public static final Color UNI_COLOR = Color.valueOf("#3A6698");
    public static final Color RED_COLOR = Color.valueOf("#da3d26");

    private String outlineClickedStyle = "-fx-background-color: white; -fx-text-fill: #3a6698; -fx-border-color: #3a6698; " +
            "/*-fx-border-width: 1.5px;*/ -fx-border-radius: 7; -fx-background-radius: 7; -fx-cursor: hand; -fx-border-width: 0 0 1 0";

    private String outlineNotClickedStyle = "-fx-background-color: #3a6698; -fx-text-fill: white; -fx-border-color: #3a6698; " +
            "-fx-border-width: 1.5px; -fx-border-radius: 7; -fx-background-radius: 7; -fx-cursor: hand;";


    public enum ButtonType {
        PRIMARY, SECONDARY, TRANSPARENT, OUTLINE
    }

    public MyButton() {
        super();
        initDefaultStyles();
    }

    public MyButton(String text) {
        super(text);
        initDefaultStyles();
    }

    public MyButton(String text, ButtonType type) {
        super(text);
        applyButtonType(type);
    }

    public MyButton(String text, Color backgroundColor, Color textColor) {
        super(text);
        styleButton(backgroundColor, textColor);
    }

    private void initDefaultStyles() {
        this.setFocusTraversable(false);
        this.setCursor(Cursor.HAND);
        this.setFont(Font.font("System", FontWeight.BOLD, 14));
    }

    public MyButton applyButtonType(ButtonType type) {
        switch (type) {
            case PRIMARY:
                return styleButton(UNI_COLOR, Color.WHITE);
            case SECONDARY:
                return styleButton(RED_COLOR, Color.WHITE);
            case TRANSPARENT:
                return setToTransparentButton();
            case OUTLINE:
                return setToOutlineButton();
            default:
                return styleButton(UNI_COLOR, Color.WHITE);
        }
    }

    public MyButton styleButton(Color backgroundColor, Color textColor) {
        this.setPrefSize(100, 30);
        this.setFont(Font.font("System", FontWeight.BOLD, 14));
        this.setTextFill(textColor);
        this.setBackground(new Background(new BackgroundFill(backgroundColor, new CornerRadii(8), Insets.EMPTY)));
        this.setCursor(Cursor.HAND);

        this.setOnMouseEntered(e -> this.setOpacity(0.8));
        this.setOnMouseExited(e -> this.setOpacity(1.0));

        return this;
    }

    public MyButton setToTransparentButton() {
        this.setStyle("-fx-background-color: transparent; -fx-text-fill: #3a6698; -fx-font-size: 30px; -fx-cursor: hand;");
        this.setFont(Font.loadFont(getClass().getResourceAsStream("/Media/Fonts/Roboto.ttf"), 20));
        this.setFocusTraversable(true);

        this.setOnMouseEntered(e -> {
            this.setBorder(new Border(new BorderStroke(
                    UNI_COLOR,
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    new BorderWidths(0, 0, 1, 0)
            )));
        });

        this.setOnMouseExited(e -> {
            this.setBorder(new Border(new BorderStroke(
                    UNI_COLOR,
                    BorderStrokeStyle.SOLID,
                    CornerRadii.EMPTY,
                    new BorderWidths(0)
            )));
        });

        this.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                this.setBorder(new Border(new BorderStroke(
                        UNI_COLOR,
                        BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        new BorderWidths(0, 0, 1, 0)
                )));
            } else {
                this.setBorder(new Border(new BorderStroke(
                        UNI_COLOR,
                        BorderStrokeStyle.SOLID,
                        CornerRadii.EMPTY,
                        new BorderWidths(0)
                )));
            }
        });

        return this;
    }

    public MyButton setToOutlineButton() {
        this.setStyle(outlineClickedStyle);

        this.setOnMouseEntered(e -> {
            setOutlineClickedStyle();
        });

        this.setOnMouseClicked(e -> {
            setOutlineClickedStyle();
        });

        return this;
    }

    public void setOutlineClickedStyle() {
        this.setStyle(outlineClickedStyle);
    }

    public void setOutlineNotClickedStyle() {
        this.setStyle(outlineNotClickedStyle);
    }




    public MyButton setWithIcon(String iconPath, double width, double height) throws imageNotFoundException {
        try {
            ImageView icon = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath))));
            icon.setFitWidth(width);
            icon.setFitHeight(height);
            icon.setPreserveRatio(true);
            this.setGraphic(icon);
        } catch (Exception e) {
           throw new imageNotFoundException("/Media/Logos/newLogo.png");
        }
        return this;
    }

    public MyButton setSize(double width, double height) {
        this.setPrefSize(width, height);
        this.setMinSize(width, height);
        this.setMaxSize(width, height);
        return this;
    }

    public MyButton setCustomFont(String fontPath, double size) {
        try {
            Font customFont = Font.loadFont(getClass().getResourceAsStream(fontPath), size);
            this.setFont(customFont);
        } catch (Exception e) {
            System.err.println("Errore nel caricamento del font: " + fontPath);
            this.setFont(Font.font("System", FontWeight.BOLD, size));
        }
        return this;
    }

    public MyButton setDisabledStyle() {
        this.setDisable(true);
        this.setStyle("-fx-background-color: gray; -fx-text-fill: white;");
        return this;
    }
}