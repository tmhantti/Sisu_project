package fi.tuni.prog3.sisu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Second Window view for the UI.
 * @author Sanna Nykänen & Tatu Anttila
 */
public class SecondWindow {
    
    int targetCreditsForDegree = 0;
    
    /**
     * Function creates scene for second window.
     * @param stage
     * @return scene2 - view for second window.
     * @throws IOException - if some classes are not found.
     */
    public Scene create(Stage stage) throws IOException {

        // Initializations
        Scene scene2 = null;
        TreeItem<String> rootNode
                = new TreeItem<>("");
        rootNode.setExpanded(true);
        Degrees deg = new Degrees();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(25, 25, 25, 25));
        stage.setTitle("Studyplanner");
        scene2 = new Scene(grid, 1340, 650);
        
        // Datastructures
        ArrayList<String> programArr = deg.getNames();
        ArrayList<String> temp = new ArrayList<>();
        HashMap<contentModule, TreeItem<String>> moduleNodes = new HashMap<>();
        HashMap<CourseUnit, TreeItem<String>> courseNodes = new HashMap<>();
        ArrayList<CourseUnit> chosenCourses = new ArrayList<>();

        // Study program combobox
        ComboBox studyProgramsComboBox = new ComboBox();
        studyProgramsComboBox.getItems().addAll(programArr);
        studyProgramsComboBox.setPromptText("Select study program");
        // Set combobox promptText color
        studyProgramsComboBox.setButtonCell(new ListCell() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setStyle("-fx-text-fill: derive(-fx-control-inner-background,-30%)");
                } else {
                    setStyle("-fx-text-fill: -fx-text-inner-color");
                    setText(item.toString());
                }
            }
        });
        grid.add(studyProgramsComboBox, 0, 4);

        // Buttons
        Button searchBtn = new Button("Search");
        GridPane.setMargin(searchBtn, new Insets(0, 0, 0, 10));
        searchBtn.setId("searchProgram");
        grid.add(searchBtn, 1, 4);

        Button exitBtn = new Button("Exit");
        grid.add(exitBtn, 6, 10);
        GridPane.setMargin(exitBtn, new Insets(0, 0, 0, 200));

        Button addBtn = new Button("Add >>");
        addBtn.setPadding(new Insets(10, 10, 10, 10));
        GridPane.setMargin(addBtn, new Insets(5, 5, 200, 30));
        addBtn.setStyle("-fx-background-color: #b3f0c7; " +
                "-fx-border-color: #8c898b; " +
                "-fx-border-radius: 0.3em; " +
                "-fx-background-radius: 0.3em;");
        addBtn.setDisable(true);
        grid.add(addBtn, 1, 8);

        Button removeBtn = new Button("Remove selected course");
        removeBtn.setPadding(new Insets(10, 10, 10, 10));
        removeBtn.setStyle("-fx-background-color: #fd8ec6; " +
                "-fx-border-color: #8c898b; " +
                "-fx-border-radius: 0.3em; " +
                "-fx-background-radius: 0.3em;");
        removeBtn.setDisable(true);
        grid.add(removeBtn, 4, 10);

        //Labels
        String firstNameInput = FirstWindow.getFirstName();
        String lastNameInput = FirstWindow.getLastName();
        String studentNumberInput = FirstWindow.getStudentNumber() ;
       
        Label studentName = new Label("");
        grid.add(studentName, 0, 0);
        studentName.setText(String.format(
                            "%s, %s", lastNameInput, firstNameInput));

        Label studentNumber = new Label("");
        grid.add(studentNumber, 0, 1);
        studentNumber.setText(String.format(
                            "%s", studentNumberInput));

        Label title1 = new Label("Search studies");
        title1.setPadding(new Insets(20, 0, 20, 0));
        title1.setFont(Font.font(
                "Verdana", FontWeight.BOLD, 18));
        GridPane.setColumnSpan(title1, 2);
        title1.setAlignment(Pos.CENTER);
        grid.add(title1, 0, 2);

        Label title2 = new Label("Own studies list");
        title2.setFont(Font.font(
                "Verdana", FontWeight.BOLD, 18));
        title2.setAlignment(Pos.CENTER);
        grid.add(title2, 4, 2);
  
        Label programLabel = new Label("Study Program:");
        GridPane.setMargin(programLabel, new Insets(10, 0, 5, 0));
        grid.add(programLabel, 0, 3);

        Label directionsLabel = new Label(
                "Select course and click 'Add>>' to add it to your own list");
        GridPane.setMargin(programLabel, new Insets(10, 0, 5, 0));
        directionsLabel.setStyle("-fx-text-fill: blue;");
        grid.add(directionsLabel, 0, 6);

        Label coursesLabel = new Label("Degree structure:");
        GridPane.setMargin(coursesLabel,
                new Insets(10, 0, 5, 0));
        grid.add(coursesLabel, 0, 7);

        // Label studypoints
        Label pointsCompletedLabel = new Label("Studypoints done:");
        grid.add(pointsCompletedLabel, 4, 0);
        
        Label pointsSelectedLabel = new Label("Studypoints selected:");
        GridPane.setMargin(pointsSelectedLabel,
                new Insets(0, 5, 0, 5));
        grid.add(pointsSelectedLabel, 5, 0);
        
        Label pointsTotalLabel = new Label("Program studypoints:");
        GridPane.setMargin(pointsTotalLabel,
                new Insets(0, 5, 0, 50));
        grid.add(pointsTotalLabel, 6, 0);

        // Label to put studypoints
        Label completedIntLabel = new Label("");
        grid.add(completedIntLabel, 4, 1);
        int zero = 0;
        completedIntLabel.setText(String.format("%s",
                zero));
        
        Label selectedIntLabel = new Label("");
        grid.add(selectedIntLabel, 5, 1);
        GridPane.setMargin(selectedIntLabel,
                new Insets(0, 5, 0, 5));
        selectedIntLabel.setText(String.format("%s",
                zero));
        
        Label totalIntLabel = new Label("");
        grid.add(totalIntLabel, 6, 1);
        GridPane.setMargin(totalIntLabel,
                new Insets(0, 5, 0, 50));
        totalIntLabel.setText(String.format("%s",
                zero));
        
        // Warning labels
        Label addNotificationLabel = new Label("");
        GridPane.setMargin(addNotificationLabel, new Insets(10, 0, 5, 0));
        addNotificationLabel.setStyle("-fx-text-fill: red;");
        grid.add(addNotificationLabel, 0, 9);

        Label searchWarningLabel = new Label("");
        GridPane.setMargin(searchWarningLabel, new Insets(10, 0, 5, 0));
        searchWarningLabel.setStyle("-fx-text-fill: red;");
        grid.add(searchWarningLabel, 0, 5);
        
        Label removeWarningLabel = new Label("");
        GridPane.setMargin(removeWarningLabel, new Insets(10, 0, 5, 0));
        removeWarningLabel.setStyle("-fx-text-fill: red;");
        GridPane.setRowSpan(removeWarningLabel, 2);
        grid.add(removeWarningLabel, 4, 9);

        //Separator
        SeparatorApp sep = new SeparatorApp();
        grid.add(sep.drawSeparator(), 3, 2);

        // VBox for the treeview & treeview "Degree structure"
        VBox studiesVbox = new VBox();
        TreeView<String> treeView = new TreeView<>(rootNode);
        studiesVbox.getChildren().add(treeView);
        grid.add(studiesVbox, 0, 8);

        // VBox & listView for "Own studies list"
        VBox listViewOwnListVbox = new VBox();
        ListView<CheckBox> listViewOwnList = new ListView<>();
        listViewOwnList.setPrefWidth(520);
        listViewOwnList.setPrefHeight(840);
        listViewOwnList.setPadding(new Insets(8, 3, 8, 3));
        listViewOwnListVbox.getChildren().add(listViewOwnList);
        GridPane.setRowSpan(listViewOwnListVbox, 6);
        GridPane.setColumnSpan(listViewOwnListVbox, 3);
        grid.add(listViewOwnListVbox, 4, 3);

        // EVENTHANDLERS >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        
        /**
         * searchBtn gets all degree programs by retrieving program id 
         * and generates tree view of study modules and courses.
         */
        searchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                boolean isSearchValid = true;
                String selectedProgram = null;
                if (studyProgramsComboBox.getValue() == null) {
                    searchWarningLabel.setText("Choose studyprogram first!");
                    isSearchValid = false;
                }
                if (isSearchValid) {
                    selectedProgram = studyProgramsComboBox.getValue().toString();
                    if (selectedProgram == null) {
                        searchWarningLabel.setText("Choose studyprogram first!");
                        isSearchValid = false;
                    }
                }
                if (isSearchValid) {
                    treeView.setRoot(rootNode);
                    //TreeView object that displays degree structure in hierarchical order:
                    DegreeModule DMtest = null;
                    Degrees deg = null;

                    try {
                        deg = new Degrees();
                        System.out.println("Degrees onnistui");
                    } catch (IOException ex) {
                        System.out.println(ex.toString());
                        System.out.println("Degrees.java not found");
                    }
                    String selectedProgramId = deg.getGroupIdBasedOnName(selectedProgram);

                    try {
                        DMtest = new DegreeModule(selectedProgramId);
                        System.out.println("DegreeModule onnistui");
                    } catch (IOException ex) {
                        System.out.println(ex.toString());
                        System.out.println("DegreeModule.java not found");
                    }

                    contentModule rootModule = DMtest.getRootModule();
                    targetCreditsForDegree = rootModule.getMinCredits();
                    System.out.println("Target credits: " + targetCreditsForDegree);
                    rootNode.setValue(selectedProgram + " (" + targetCreditsForDegree + " op)");
                    totalIntLabel.setText(String.format("%s",
                                                    targetCreditsForDegree));

                    // initialize some stuff before processing modules to nodes:
                    String curName; // name of the current module
                    contentModule curModule; // current processed module
                    contentModule parentModule; // parent of curModule
                    TreeItem<String> parentNode; // current parent node
                    TreeItem<String> newNode;
                    // queue containing modules:      
                    Queue<contentModule> contentQueue = new LinkedList<>();
                    contentQueue.add(rootModule);
                    parentModule = rootModule;

                    // go through modules and set up tree structure:
                    while (!contentQueue.isEmpty()) {
                        parentModule = contentQueue.remove(); // pop queue
                        // find parent node: 
                        if (!(parentModule.getGroupId()
                                .equals(rootModule.getGroupId()))) {
                            parentNode = moduleNodes.get(parentModule);
                        } else {
                            parentNode = rootNode; 
                        }
                        for (var modules : parentModule.getSubModules()) {
                            curName = modules.getName();
                            newNode = new TreeItem<>(curName);
                            // add a new node                                       
                            parentNode.getChildren().add(newNode); 
                            contentQueue.add(modules);    
                            moduleNodes.put(modules, newNode);
                        }
                    }
                    // Add courses under the corresponding modules:
                    TreeItem<String> curNode;
                    for (var elem : moduleNodes.entrySet()) {
                        curModule = elem.getKey();
                        curNode = elem.getValue();
                        for (var course : curModule.getCourses()) {
                            curName = course.getName();
                            String op = Integer.toString(course.getMinCredits());
                            curName = curName + " (" + op + " op)";
                            // add a new node     
                            newNode = new TreeItem<>(curName);
                            curNode.getChildren().add(newNode);
                            courseNodes.put(course, newNode);
                        }
                    }
                }
            }
        });
        
        /**
         * Tree view event listener enables addBtn
         */
        treeView.getSelectionModel().selectedItemProperty().addListener((
                observable, oldValue, newValue) -> {
            addBtn.setDisable(false);
        });

        /**
         * addBtn event handler  gets selected item from "Degree structure" 
         * tree view and adds it to "Own studies list".
         */
        addBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                removeBtn.setDisable(false);
                boolean isNodeCourse = false;
                CourseUnit curCourse = null;
                String curCourseName = "";
                addNotificationLabel.setText("");
                String selection = treeView.getSelectionModel().getSelectedItem().getValue();
                System.out.println("valinta: " + selection);
                if (selection.isEmpty()) {
                    addNotificationLabel.setText("Choose course first!");
                } else {
                    // check that the chosen node is course                  
                    if (!selection.isEmpty()) {
                        // if it is course, get the corresponding CourseUnit object:
                        curCourse = getCourseFromMap(courseNodes, selection);
                        if (curCourse != null) {
                            isNodeCourse = true;
                            curCourseName = curCourse.getName();
                        }
                        if (!isNodeCourse) {
                            addNotificationLabel.setText("Choose course first!");
                        }
                        if (temp.contains(curCourseName)) {
                            addNotificationLabel.setText("You already have this course on your list!");
                        }
                        if (isNodeCourse && (!temp.contains(curCourseName))) {
                            CheckBox cb = new CheckBox(curCourse.getName());
                            /**
                             * Checkbox event handler updates study points labels
                             * when courses are added to list, items are checked
                             * or unchecked.
                             */
                            EventHandler<ActionEvent> CBevent = new EventHandler<ActionEvent>() {
                                public void handle(ActionEvent e) {
                                    CourseUnit chosenCourse;
                                    String CBoxText = cb.getText();
                                    if (cb.isSelected()) {
                                        //  Find the corresponding CourseUnit object first:                                    
                                        for (var course : chosenCourses) {
                                            if (course.getName().equals(CBoxText)) {
                                                course.setCourseCompleted(true);
                                            }
                                        }
                                        // Update credit points (completed courses): 
                                        int totCreditSum = getTotalCreditsOfCompletedCourses(chosenCourses);
                                        completedIntLabel.setText(String.format("%s",
                                                totCreditSum));
                                        // System.out.println("total credits in completed courses: " + totCreditSum);
                                    } else {
                                        System.out.println(selection + "is not selected.");
                                        //  find the corresponding CourseUnit object first:                                    
                                        for (var course : chosenCourses) {
                                            if (course.getName().equals(CBoxText)) {
                                                course.setCourseCompleted(false);
                                            }
                                        }
                                        // Update credit points (completed courses): 
                                        int totCreditSum = getTotalCreditsOfCompletedCourses(chosenCourses);
                                        completedIntLabel.setText(String.format("%s",
                                                totCreditSum));
                                    }
                                }
                            };
                            // set event to checkbox
                            cb.setOnAction(CBevent);
                            listViewOwnList.getItems().add(cb);
                            // update list containing selected courses                    
                            chosenCourses.add(curCourse);
                            temp.add(curCourseName);
                            System.out.println(temp);
                            System.out.println(selection);

                            // Update credit points (selected courses): 
                            int totCreditSum = getTotalCreditsOfSelectedCourses(chosenCourses);
                            selectedIntLabel.setText(String.format("%s",
                                                totCreditSum));
                        }

                    }
                }
            }
        });

        /**
         * removeBtn event handler removes selected course from "Own studies list"
         * and updates total of credits selected and completed.
         */
        removeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CourseUnit curCourse = null;
                addNotificationLabel.setText("");
                CheckBox itemToRemove = listViewOwnList.getSelectionModel().getSelectedItem();
                if (itemToRemove == null) {
                    removeWarningLabel.setText("Choose course to be removed first!");
                }
                if (itemToRemove != null) {
                    String CBoxText = itemToRemove.getText();
                    // remove checkbox from the list: 
                    listViewOwnList.getItems().remove(itemToRemove);
                    // remove corresponding course from the chosenCourses ArrayList:
                    //  find the corresponding CourseUnit object first:
                    for (var course : chosenCourses) {
                        if (CBoxText.equals(course.getName())) {
                            curCourse = course;
                        }
                    }
                    if (curCourse != null) {
                        curCourse.setCourseCompleted(false);
                        chosenCourses.remove(curCourse);
                        temp.remove(curCourse.getName());
                        // System.out.println("course removed!");
                        // Update credit points (completed courses): 
                        int totCreditSum = getTotalCreditsOfCompletedCourses(chosenCourses);
                        completedIntLabel.setText(String.format("%s",
                                totCreditSum));
                        // System.out.println("total credits in completed courses: " + totCreditSum);
                        if (temp.isEmpty()){
                            removeBtn.setDisable(true);
                        }
                    }
                     // Update credit points (selected courses):              
                    int totCreditSum = getTotalCreditsOfSelectedCourses(chosenCourses);
                    selectedIntLabel.setText(String.format("%s",
                            totCreditSum));
                    System.out.println("total credits in chosen courses: " + totCreditSum);
                }
            }
        });
 
        /**
         * ComboBox event handler listens if "Study program" selection is changed
         * and creates confirmation alert.
         */
        studyProgramsComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
                if (!moduleNodes.isEmpty()) {
                    // Alert
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Changing study program");
                    alert.setHeaderText("Are you sure about changing the program?");
                    alert.setContentText("All your information in Own studies list will be erased.");
                    Optional<ButtonType> result = alert.showAndWait();
                    
                    if (result.get() == ButtonType.OK) {
                        treeView.getRoot().getChildren().clear();
                        treeView.setRoot(null);
                        moduleNodes.clear();
                        courseNodes.clear();
                        chosenCourses.clear();
                        listViewOwnList.getItems().clear();
                        addBtn.setDisable(true);
                        completedIntLabel.setText(String.format("%s",
                                zero));
                        selectedIntLabel.setText(String.format("%s",
                                zero));
                        totalIntLabel.setText(String.format("%s",
                                zero));
                    } else {
                    }
                } else {
                }

            }
        });

        //exit button functionality
        exitBtn.setOnAction((event) -> {
            stage.close();
        });
        stage.setOnCloseRequest((event) -> {
            Platform.exit();
        });
        
        return scene2;
    }

    /**
     * Updates total credits of selected courses
     * @param chosenCourses - array of chosen courses
     * @return int of study credits in total
     */
    private int getTotalCreditsOfSelectedCourses(ArrayList<CourseUnit> chosenCourses) {
        int totCreditSum = 0;
        for (var course : chosenCourses) {
            totCreditSum += course.getMinCredits();
        }
        return totCreditSum;
    }
    
    /**
     * Updates total credits of completed courses.
     * @param chosenCourses - array of chosen courses
     * @return int of study credits in total
     */
    private int getTotalCreditsOfCompletedCourses(ArrayList<CourseUnit> chosenCourses) {
        int totCreditSum = 0;
        for (var course : chosenCourses) {
            if (course.isCompleted()) {
                totCreditSum += course.getMinCredits();
            }
        }
        return totCreditSum;
    }
    
    /**
     * Gets course based on its name from the given map structure.
     * @param courseNodes 
     * @param selection - string selected item from Degree structure
     * @return - course key otherwise null.
     */
    private CourseUnit getCourseFromMap(HashMap<CourseUnit, TreeItem<String>> courseNodes, String selection) {
        for (var course : courseNodes.entrySet()) {
            if (selection.equals(course.getValue().getValue())) {
                return course.getKey();
            }
        }
        return null;
    }
}