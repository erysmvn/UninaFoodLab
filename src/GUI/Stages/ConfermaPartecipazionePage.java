package GUI.Stages;

import Controller.Controller;
import DAO.FoglioAdesioneDAO;
import DAO.SessioneDAO;
import Entity.SessionePresenza;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ConfermaPartecipazionePage extends Stage {

    Controller controller;
    private Button uploadButton;
    private Label dropArea;
    private Label errorLabel;
    private SessionePresenza sessionePresenza;

    public ConfermaPartecipazionePage(Controller controller) {
        this.controller = controller;
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setBackground(new Background(new BackgroundFill(
                Color.WHITE, new CornerRadii(30), Insets.EMPTY
        )));
        root.setBorder(new Border(new BorderStroke(
                Color.valueOf("#3A6698"),
                BorderStrokeStyle.SOLID,
                new CornerRadii(30),
                new BorderWidths(2)
        )));
        root.setAlignment(Pos.CENTER);

        setUploadButton();
        setDropArea();
        setErrorLabel();

        root.getChildren().addAll(dropArea, uploadButton, errorLabel, createCloseButton());

        Scene scene = new Scene(root, 550, 400);
        scene.setFill(Color.TRANSPARENT);
        this.initStyle(StageStyle.TRANSPARENT);
        this.setScene(scene);
    }

    private void setDropArea(){
        dropArea = new Label("Trascina qui il file");
        dropArea.setStyle("-fx-border-color: #3a6698;" +
                " -fx-border-width: 1;" +
                " -fx-min-height: 200;" +
                "-fx-min-width: 200;" +
                "-fx-opacity: 0.5;" +
                "-fx-alignment: center;");
        setDropAreaFunctionalities();
    }

    private void setDropAreaFunctionalities(){
        dropArea.setOnDragOver((DragEvent event) -> {
            if (event.getGestureSource() != dropArea && event.getDragboard().hasFiles())
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);

            event.consume();
        });


        dropArea.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                File file = db.getFiles().getFirst();
                try {
                    Path destDir = Paths.get("src/Media/FogliDiAdesione/");

                    Files.copy(file.toPath(),
                            destDir.resolve(file.getName()),
                            StandardCopyOption.REPLACE_EXISTING
                    );

                    dropArea.setText("File caricato: " + file.getName());
                    //todo solo con il controller
                    FoglioAdesioneDAO foglioAdesioneDAO = new FoglioAdesioneDAO(controller);
                    foglioAdesioneDAO.insertFoglioDiAdesione(destDir+file.getName(),sessionePresenza);
                    sessionePresenza.getFogliAdesione().add(foglioAdesioneDAO.getFoglioAdesioneBySessioneNPath(sessionePresenza, file.toPath().toString()));
                    controller.changeSessionePageButton(sessionePresenza);
                    showSuccessDialog();
                    errorLabel.setText("");
                    success = true;

                } catch (Exception ex) {
                    ex.printStackTrace();
                    errorLabel.setText("Caricamento fallito");
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void setUploadButton(){
        uploadButton = new Button("Carica file");
        styleButton(uploadButton, Color.valueOf("#3A6698"));
        setUploadButtonOnAction();
    }

    private void setUploadButtonOnAction(){
        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File PDF", "*.pdf"));
            File file = fileChooser.showOpenDialog(this);

            if (file != null) {
                setUploadButtonToConfirmButton(file);
                dropArea.setText("File caricato: " + file.getName());
            }

        });
    }

    private void setUploadButtonToConfirmButton(File file){
        uploadButton.setText("Carica");
        uploadButton.setOnAction(e -> {
                    Path destDir = Paths.get("src/Media/FogliDiAdesione/");
                   try {
                        Files.copy(file.toPath(),
                                destDir.resolve(file.getName()),
                                StandardCopyOption.REPLACE_EXISTING
                        );
                       dropArea.setText("File caricato: " + file.getName());

                       FoglioAdesioneDAO foglioAdesioneDAO = new FoglioAdesioneDAO(controller);
                       foglioAdesioneDAO.insertFoglioDiAdesione(destDir+file.getName(),sessionePresenza);
                       sessionePresenza.getFogliAdesione().add(foglioAdesioneDAO.getFoglioAdesioneBySessioneNPath(sessionePresenza, file.toPath().toString()));
                       controller.changeSessionePageButton(sessionePresenza);
                       showSuccessDialog();
                       errorLabel.setText("");
                       this.close();

                   }catch (Exception ex) {
                       ex.printStackTrace();
                        errorLabel.setText("Caricamento fallito");
                        e.consume();
                   }
        });
    }

    private void setErrorLabel() {
        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
    }

    private Button createCloseButton() {
        Button closeButton = new Button("Chiudi");
        styleButton(closeButton, Color.valueOf("#da3d26"));
        closeButton.setOnAction(e -> this.close());
        return closeButton;
    }

    private void styleButton(Button button, Color color) {
        button.setPrefSize(100, 30);
        button.setFont(Font.font("System", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setBackground(new Background(new BackgroundFill(color, new CornerRadii(8), Insets.EMPTY)));
        button.setCursor(Cursor.HAND);
        button.setAlignment(Pos.BOTTOM_CENTER);

        button.setOnMouseEntered(e -> button.setOpacity(0.8));
        button.setOnMouseExited(e -> button.setOpacity(1.0));
    }

    private void showSuccessDialog() {
        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.TRANSPARENT);

        Label label = new Label("Inserito con successo !");
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-background-color: rgba(0, 128, 0, 0.8); -fx-padding: 20px; -fx-background-radius: 10;");
        label.setFont(Font.font("System", FontWeight.BOLD, 16));

        StackPane pane = new StackPane(label);
        pane.setStyle("-fx-background-color: transparent;");
        Scene scene = new Scene(pane);
        scene.setFill(Color.TRANSPARENT);

        dialog.setScene(scene);
        dialog.show();

        javafx.application.Platform.runLater(this::close);

        new Thread(() -> {
            try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            javafx.application.Platform.runLater(dialog::close);
        }).start();
    }

    public void setSessionePresenza(SessionePresenza sessionePresenza) {
        this.sessionePresenza = sessionePresenza;
    }

}
