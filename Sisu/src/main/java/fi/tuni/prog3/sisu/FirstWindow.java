package fi.tuni.prog3.sisu;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * First window for UI.
 * @author Miia Raerinne & Sanna Nykänen
 */
public class FirstWindow {
    private static String firstNameInput;
    private static String lastNameInput;
    private static String studentNumberInput;
    
    public static String getFirstName () {
        return firstNameInput;
    }
    
    public static String getLastName () {
        return lastNameInput;
    }
    
    public static String getStudentNumber () {
        return studentNumberInput;
    }
    /**
     * Function creates scene for first window.
     * @param stage
     * @return scene1 - view for first window
     */
    public Scene create(Stage stage) {
        stage.setTitle("Studyplanner");
        BorderPane bPane = new BorderPane();
        Scene scene1 = new Scene(bPane, 400, 500);

        // title
        Label labelTitle = new Label("Study Planner");
        bPane.setTop(labelTitle);
        labelTitle.setAlignment(Pos.CENTER);
        bPane.setMargin(labelTitle, new Insets(30));
        labelTitle.setFont(new Font("Arial", 20));

        // labels and textFields for signing in
        VBox vbCenter = new VBox(10);
        bPane.setCenter(vbCenter);
        bPane.setMargin(vbCenter, new Insets(30));

        Label labelFirstName = new Label("First Name:");
        vbCenter.getChildren().add(labelFirstName);

        TextField inputFirstName = new TextField();
        vbCenter.getChildren().add(inputFirstName);

        Label labelLastName = new Label("Last Name:");
        vbCenter.getChildren().add(labelLastName);

        TextField inputLastName = new TextField();
        vbCenter.getChildren().add(inputLastName);

        Label labelStudentNumber = new Label("Student ID number:");
        vbCenter.getChildren().add(labelStudentNumber);

        TextField inputStudentNumber = new TextField();
        vbCenter.getChildren().add(inputStudentNumber);

        Label inputMissing = new Label("");
        inputMissing.setStyle("-fx-text-fill: red;");
        vbCenter.getChildren().add(inputMissing);

        // button
        HBox hbBtn = new HBox(40);
        bPane.setBottom(hbBtn);
        hbBtn.setAlignment(Pos.CENTER);
        bPane.setMargin(hbBtn, new Insets(30));

        Button btnLogIn = new Button("Log In");
        hbBtn.getChildren().add(btnLogIn);

        /**
         * btnLogIn event handler checks user input and saves input to
         * variables for second window.
         */
        btnLogIn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                firstNameInput= inputFirstName.getText();
                lastNameInput = inputLastName.getText();
                studentNumberInput = inputStudentNumber.getText();
                
                if (firstNameInput.isEmpty() || lastNameInput.isEmpty()
                        || studentNumberInput.isEmpty()) {
                    inputMissing.setText("Fill out all fields!");
                } else {
                    try {
                        SecondWindow sec = new SecondWindow();
                        Scene scen = sec.create(stage);
                        stage.setScene(scen);
                    } catch (IOException ex) {
                        System.out.println("SecondWindow.java not found: " + ex.toString());
                    }
                }
            }
        });
        return scene1;
    }
}
