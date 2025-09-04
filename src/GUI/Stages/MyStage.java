package GUI.Stages;

import GUI.Buttons.MyButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MyStage extends Stage {

    protected Pane root;
    protected Scene scene;
    protected int width;
    protected int height;

    public enum RootType {
        BORDERPANE, VBOX, HBOX
    }

    public MyStage(int width, int height, RootType type) {
        this.width = width;
        this.height = height;
        switch (type) {
            case BORDERPANE:
                root = new BorderPane();
                break;
            case VBOX:
                root = new VBox();
                break;
            case HBOX:
                root = new HBox();
                break;
        }

        initializeStage();
        setupRoot();
        setupCloseOnCtrlW();
    }

    private void initializeStage() {
        this.initStyle(StageStyle.TRANSPARENT);
    }

    private void setupRoot() {
        setupRootAesthetics();

        scene = new Scene(root, width, height);
        scene.setFill(Color.TRANSPARENT);
        this.setScene(scene);
    }

    protected void setupRootAesthetics() {
        root.setPadding(new Insets(20));
        root.setBackground(new Background(new BackgroundFill(
                Color.WHITE, new CornerRadii(30), Insets.EMPTY
        )));
        root.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(30),
                new BorderWidths(2)
        )));
    }

    protected void addStylesheet(String stylesheetPath) {
        try {
            scene.getStylesheets().add(
                    getClass().getResource(stylesheetPath).toExternalForm()
            );
        } catch (Exception e) {
            System.err.println("Stylesheet not found: " + stylesheetPath);
        }
    }

    protected void setupCloseOnCtrlW() {
        scene.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode() == javafx.scene.input.KeyCode.W
                    && this.isShowing() && !this.isIconified()) {
                this.close();
            }
        });
    }

    protected void setupFullScreen() {
        Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        this.setX(screenBounds.getMinX());
        this.setY(screenBounds.getMinY());
        this.setWidth(screenBounds.getWidth());
        this.setHeight(screenBounds.getHeight());
    }

    protected HBox createTopBarWithControls() {
        HBox topBar = new HBox(5);
        topBar.setAlignment(javafx.geometry.Pos.TOP_RIGHT);
        topBar.setPadding(new Insets(3, 0, 0, 0));

        GUI.Buttons.CircleButton minimizeBtn = new GUI.Buttons.CircleButton().setToMinimizeButtonWithAction(this);
        GUI.Buttons.CircleButton closeBtn = new GUI.Buttons.CircleButton().setToCloseButtonWithAction(this);

        topBar.getChildren().addAll(minimizeBtn, closeBtn);
        return topBar;
    }

    protected VBox createCenteredVBox(int spacing) {
        VBox vbox = new VBox(spacing);
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        return vbox;
    }

    protected HBox createCenteredHBox(int spacing) {
        HBox hbox = new HBox(spacing);
        hbox.setAlignment(javafx.geometry.Pos.CENTER);
        return hbox;
    }

    protected BorderPane getRootBorderPane() {
        return (BorderPane) root;
    }

    protected VBox getRootVBox() {
        return (VBox) root;
    }

    protected HBox getRootHBox() {
        return (HBox) root;
    }

    protected void showConfirmPanel(String message, Runnable onConfirm) {
        Stage confirmStage = new Stage();
        confirmStage.initModality(Modality.APPLICATION_MODAL);
        confirmStage.initStyle(StageStyle.TRANSPARENT);

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(15), Insets.EMPTY)));
        root.setBorder(new Border(new BorderStroke(Color.valueOf("#3A6698"), BorderStrokeStyle.SOLID, new CornerRadii(15), new BorderWidths(2))));

        Label label = new Label(message);
        label.setFont(Font.font("System", FontWeight.BOLD, 18));
        label.setTextFill(Color.valueOf("#2F3A42"));
        label.setWrapText(true);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setMaxWidth(300);

        MyButton yesButton = new MyButton("Si", MyButton.ButtonType.PRIMARY);
        MyButton noButton = new MyButton("No", MyButton.ButtonType.SECONDARY);

        HBox buttons = new HBox(15, yesButton, noButton);
        buttons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(label, buttons);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        confirmStage.setScene(scene);

        yesButton.setOnAction(e -> {
            onConfirm.run();
            confirmStage.close();
            this.close();
        });

        noButton.setOnAction(e -> confirmStage.close());

        confirmStage.showAndWait();
    }
}