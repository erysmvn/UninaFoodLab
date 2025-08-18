import Controller.Controller;
import GUI.Stages.*;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Controller controller = new Controller();
        controller.setHomePage(new HomePage(controller));
        controller.getHomePage().show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}