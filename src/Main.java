import Controller.Controller;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Controller controller = new Controller();
        controller.showHomePage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}