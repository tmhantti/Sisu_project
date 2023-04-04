package fi.tuni.prog3.sisu;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX App
 * @author Sanna Nykänen
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FirstWindow first = new FirstWindow();
        primaryStage.setScene(first.create(primaryStage));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}