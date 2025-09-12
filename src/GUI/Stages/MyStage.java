package GUI.Stages;

import GUI.Buttons.MyButton;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

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

    protected BorderPane getRootBorderPane() {
        return (BorderPane) root;
    }

    protected VBox getRootVBox() {
        return (VBox) root;
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

    protected void showDialog(String message) {
        Stage dialog = createDialogAScomparsa(message);
        dialog.show();

        Platform.runLater(this::close);

        new Thread(() -> {
            try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            Platform.runLater(dialog::close);
        }).start();
    }

    private Stage createDialogAScomparsa(String message) {
        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.TRANSPARENT);

        Label label = new Label(message);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-background-color: rgba(128,128,128,0.73); -fx-padding: 20px; -fx-background-radius: 10;");
        label.setFont(Font.font("System", FontWeight.BOLD, 16));

        StackPane pane = new StackPane(label);
        pane.setStyle("-fx-background-color: transparent;");
        Scene scene = new Scene(pane);
        scene.setFill(Color.TRANSPARENT);

        dialog.setScene(scene);
        return dialog;
    }

    protected VBox createEmptyStageMessage(String message, String iconPath) {
        VBox box = new VBox(10);
        box.setAlignment(Pos.CENTER);

        Label label = new Label(message);
        label.setTextFill(Color.valueOf("#2F3A42"));
        label.setAlignment(Pos.CENTER);
        label.setPadding(new Insets(50, 10, 10, 10));
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 30");

        ImageView iconView = new ImageView(iconPath);
        if (iconPath != null) {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(iconPath)));
            iconView = new ImageView(icon);
            iconView.setFitWidth(60);
            iconView.setFitHeight(60);
            iconView.setPreserveRatio(true);
        }

        if (iconView != null) {
            box.getChildren().addAll(label, iconView);
        } else {
            box.getChildren().add(label);
        }

        return box;
    }

}