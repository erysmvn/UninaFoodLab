package GUI.Pane;

import Controller.Controller;
import Entity.Chef;
import Entity.Utente;
import Exception.CorsoExceptions.noCorsiTenutiException;
import GUI.Buttons.MyButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import java.sql.SQLException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class ReportPanel extends VBox {
    private Controller controller;

    private ComboBox<YearMonth> meseComboBox;
    private Label titoloLabel;
    private VBox statisticheBox;
    private BorderPane graficiPane;
    PieChart sessioniChart;

    public ReportPanel(Controller controller) {
        this.controller = controller;
    }

    public void initialize() {
        this.setSpacing(20);
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: WHITE;");

        titoloLabel = new Label("Report Mensile Corsi");
        titoloLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        titoloLabel.setTextFill(Color.valueOf("#3A6698"));
        titoloLabel.setPadding(new Insets(0, 0, 20, 0));

        HBox selezioneMeseBox = new HBox(10);
        selezioneMeseBox.setAlignment(Pos.CENTER_LEFT);
        Label meseLabel = new Label("Seleziona mese:");
        meseLabel.setFont(Font.font("System", FontWeight.BOLD, 14));

        meseComboBox = new ComboBox<>();
        meseComboBox.setPromptText("Seleziona mese");
        ObservableList<YearMonth> mesi = FXCollections.observableArrayList();
        YearMonth current = YearMonth.now();
        for (int i = 0; i < 12; i++) {
            mesi.add(current.minusMonths(i));
        }
        meseComboBox.setItems(mesi);

        // Formattatore per visualizzare correttamente i mesi
        meseComboBox.setConverter(new StringConverter<YearMonth>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");

            @Override
            public String toString(YearMonth object) {
                if (object != null) {
                    return object.format(formatter);
                } else {
                    return "";
                }
            }

            @Override
            public YearMonth fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return YearMonth.parse(string, formatter);
                } else {
                    return null;
                }
            }
        });

        meseComboBox.setValue(YearMonth.now());

        MyButton generaReportButton = new MyButton("Genera Report", MyButton.ButtonType.PRIMARY);
        generaReportButton.setSize(150,30);
        generaReportButton.setOnAction(e -> generaReport());

        selezioneMeseBox.getChildren().addAll(meseLabel, meseComboBox, generaReportButton);


        statisticheBox = new VBox(10);
        statisticheBox.setPadding(new Insets(10));
        statisticheBox.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #dddddd; -fx-border-radius: 5;");

        graficiPane = new BorderPane();
        graficiPane.setPadding(new Insets(10));
        graficiPane.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #dddddd; -fx-border-radius: 5;");

        this.getChildren().addAll(titoloLabel, selezioneMeseBox, statisticheBox, graficiPane);

        generaReport();
    }

    private void generaReport() {
        YearMonth meseSelezionato = meseComboBox.getValue();
        if (meseSelezionato == null) {
            return;
        }

        Map<String, Object> reportData = null;
        try {
            reportData = controller.getReportMensile(meseSelezionato);
        } catch (noCorsiTenutiException NCTE) {
            graficiPane.getChildren().clear();
            statisticheBox.getChildren().clear();
        }catch (SQLException e) {
            graficiPane.getChildren().clear();
            statisticheBox.getChildren().clear();
            showDialog("Errore di Sistema. Riprovare più tardi: " + e.getMessage());
            return;
        }

        // Verifica che reportData non sia null prima di procedere
        if (reportData == null) {
            showDialog("Nessun dato disponibile per il mese selezionato");
            return;
        }

        aggiornaStatistiche(reportData);
        aggiornaGrafici(reportData);
    }


    private void aggiornaStatistiche(Map<String, Object> reportData) {
        statisticheBox.getChildren().clear();

        int corsiTotali = (int) reportData.getOrDefault("corsiTotali", 0);
        int sessioniOnline = (int) reportData.getOrDefault("sessioniOnline", 0);
        int sessioniPratiche = (int) reportData.getOrDefault("sessioniPratiche", 0);
        double mediaRicette = (double) reportData.getOrDefault("mediaRicette", 0.0);
        int maxRicette = (int) reportData.getOrDefault("maxRicette", 0);
        int minRicette = (int) reportData.getOrDefault("minRicette", 0);

        Label statsTitolo = new Label("Statistiche Mensili");
        statsTitolo.setFont(Font.font("System", FontWeight.BOLD, 16));
        statsTitolo.setTextFill(Color.valueOf("#3A6698"));

        // Griglia per le statistiche
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(10);
        statsGrid.setPadding(new Insets(10, 0, 10, 0));

        addStatistica(statsGrid, 0, "Corsi totali:", String.valueOf(corsiTotali));
        addStatistica(statsGrid, 1, "Sessioni online:", String.valueOf(sessioniOnline));
        addStatistica(statsGrid, 2, "Sessioni pratiche:", String.valueOf(sessioniPratiche));
        addStatistica(statsGrid, 3, "Media ricette per sessione:", String.format("%.2f", mediaRicette));
        addStatistica(statsGrid, 4, "Max ricette in una sessione:", String.valueOf(maxRicette));
        addStatistica(statsGrid, 5, "Min ricette in una sessione:", String.valueOf(minRicette));

        statisticheBox.getChildren().addAll(statsTitolo, statsGrid);
    }

    private void addStatistica(GridPane grid, int row, String label, String value) {
        Label statLabel = new Label(label);
        statLabel.setFont(Font.font("System", FontWeight.BOLD, 12));

        Label statValue = new Label(value);
        statValue.setFont(Font.font("System", 12));

        grid.add(statLabel, 0, row);
        grid.add(statValue, 1, row);
    }

    private void aggiornaGrafici(Map<String, Object> reportData) {
        graficiPane.getChildren().clear();

        int sessioniOnline = (int) reportData.getOrDefault("sessioniOnline", 0);
        int sessioniPratiche = (int) reportData.getOrDefault("sessioniPratiche", 0);

        sessioniChart = new PieChart();
        sessioniChart.setTitle("Distribuzione Sessioni");

        PieChart.Data onlineData = new PieChart.Data("Sessioni Online", sessioniOnline);
        PieChart.Data praticheData = new PieChart.Data("Sessioni Pratiche", sessioniPratiche);

        sessioniChart.getData().addAll(onlineData, praticheData);

        Map<String, Integer> ricettePerSessione = (Map<String, Integer>) reportData.get("ricettePerSessione");
        if (ricettePerSessione != null && !ricettePerSessione.isEmpty()) {
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            BarChart<String, Number> ricetteChart = new BarChart<>(xAxis, yAxis);
            ricetteChart.setTitle("Ricette per Sessione Pratica");

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Numero di Ricette");

            for (Map.Entry<String, Integer> entry : ricettePerSessione.entrySet()) {
                series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            }

            ricetteChart.getData().add(series);

            HBox chartsBox = new HBox(20);
            chartsBox.getChildren().addAll(sessioniChart, ricetteChart);
            graficiPane.setCenter(chartsBox);
        } else {
            graficiPane.setCenter(sessioniChart);
        }
    }

    protected void showDialog(String message) {
        Stage dialog = createDialogAScomparsa(message);
        dialog.show();

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

}