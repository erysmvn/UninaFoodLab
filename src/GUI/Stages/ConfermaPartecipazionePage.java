package GUI.Stages;

import Controller.Controller;
import Entity.Corso;
import Entity.FoglioAdesione;
import Entity.SessionePresenza;
import Entity.Utente;
import Exception.UserExceptions.namingFileException;
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
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

public class ConfermaPartecipazionePage extends Stage {

    Controller controller;
    private Button uploadButton;
    private Label dropArea;
    private Label errorLabel;
    private SessionePresenza sessionePresenza;
    private String nomeCorsoNoSpace;
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


    private void checkNamingFile(String nomeFile)throws namingFileException{
            Utente utente = controller.getUtente();
            String dataItaliana = sessionePresenza.getData().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String namingRight = utente.getNome() +"_"+ utente.getCognome()+"_"+dataItaliana+".pdf";

            if(!nomeFile.equals(namingRight))
                throw new namingFileException();

    }

    private void addFolgioAdesione(String filePath)throws SQLException {
        controller.insertFoglioAdesione(filePath, sessionePresenza);
        FoglioAdesione foglio = controller.getFoglioAdesioneBySessioneNPath(filePath,sessionePresenza);
        sessionePresenza.getFogliAdesione().add(foglio);
    }

    private void addFileToServer(File file)throws Exception{
        Path destDir = Paths.get("src/Media/FogliDiAdesione/");
        Files.copy(file.toPath(),
                destDir.resolve(nomeCorsoNoSpace+file.getName()),
                StandardCopyOption.REPLACE_EXISTING
        );

    }

    private void setDropAreaFunctionalities(){
        dropArea.setOnDragOver((DragEvent event) -> {
            if (event.getGestureSource() != dropArea && event.getDragboard().hasFiles())
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            event.consume();
        });


        dropArea.setOnDragDropped((DragEvent event) -> {
            Dragboard db = event.getDragboard();
            if (db.hasFiles()) {
                File file = db.getFiles().getFirst();
                try {
                    checkNamingFile(file.getName());
                    setUploadButtonToConfirmButton(file);
                    errorLabel.setText("");
                } catch (namingFileException NFE){
                    errorLabel.setText("Il nome file deve essere Nome_Cognome_DD-MM-YYYY");
                }catch (Exception FAE ){
                    errorLabel.setText("Caricamento fallito. Riprovare più tardi");
                }
            }
            event.consume();
        });
    }

    private void setUploadButton(){
        uploadButton = new Button("Scegli file");
        styleButton(uploadButton, Color.valueOf("#3A6698"));
        setUploadButtonOnAction();
    }

    private void setUploadButtonOnAction(){
        uploadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File PDF", "*.pdf"));
            File file = fileChooser.showOpenDialog(this);
            try{
                if (file != null) {
                    checkNamingFile(file.getName());
                    setUploadButtonToConfirmButton(file);
                }
            }catch (namingFileException NFE){
                errorLabel.setText("Il nome file deve essere Nome_Cognome_DD-MM-YYYY");
            }
        });
    }

    private void setUploadButtonToConfirmButton(File file){
        uploadButton.setText("Carica");
        dropArea.setText("File caricato: " + file.getName());

        uploadButton.setOnAction(e -> {
                    Path destDir = Paths.get("src/Media/FogliDiAdesione/");
                       try{
                           Corso corso = controller.getCorsoDAO().getCorsoByIdCorso(sessionePresenza.getCorso().getIdCorso());
                           nomeCorsoNoSpace = corso.getNome().replaceAll(" ","_");

                           addFolgioAdesione(destDir+file.getName()+nomeCorsoNoSpace);
                           addFileToServer(file);

                           errorLabel.setText("");
                           showSuccessDialog();
                       }catch (Exception FileAdesioneExc ){
                           errorLabel.setText("Caricamento fallito. Riprovare più tardi");
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
        closeButton.setOnAction(e -> {
            this.close();
        });
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
        Stage dialog = createSuccessDialog();
        dialog.show();

        javafx.application.Platform.runLater(this::close);
        controller.changeSessionePageButton(sessionePresenza);
        new Thread(() -> {
            try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
            javafx.application.Platform.runLater(dialog::close);
        }).start();
    }

    private Stage createSuccessDialog() {
        Stage dialog = new Stage();
        dialog.initStyle(StageStyle.TRANSPARENT);

        Label label = new Label("Inserito con successo !");
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-background-color: rgba(0,128,0,0.73); -fx-padding: 20px; -fx-background-radius: 10;");
        label.setFont(Font.font("System", FontWeight.BOLD, 16));

        StackPane pane = new StackPane(label);
        pane.setStyle("-fx-background-color: transparent;");
        Scene scene = new Scene(pane);
        scene.setFill(Color.TRANSPARENT);

        dialog.setScene(scene);
        return dialog;
    }

    public void setSessionePresenza(SessionePresenza sessionePresenza) {
        this.sessionePresenza = sessionePresenza;
    }
    public SessionePresenza getSessionePresenza() {
        return sessionePresenza;
    }

}
