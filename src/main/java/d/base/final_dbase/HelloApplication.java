/**
 * @author Kristoffer Neo Senyahan | BSCS2
 * @project SSIS CCC151 Project
 *
 */

package d.base.final_dbase;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
public class HelloApplication extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        switchToScene1();

        primaryStage.setOnCloseRequest(event -> {
            // Handle close request here
            Platform.exit(); // Close the application
        });
    }

    private void initializeScene(String fxmlFileName, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            StackPane root = loader.load();
            Scene scene = new Scene(root, 1100, 800);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm());

            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // Make the stage not resizable
            primaryStage.setOnCloseRequest(event -> {
                // Handle close request
                event.consume(); // Consume the event to prevent the window from closing immediately
                switchToScene2(); // Switch to Scene 2
            });
            primaryStage.show();

            // Set the controller for the scene
            switch (fxmlFileName) {
                case "scene1.fxml" -> {
                    HelloController1 scene1Controller = loader.getController();
                    scene1Controller.setHelloApplication(this);

                    primaryStage.setOnCloseRequest(event -> {
                        // Handle close request here
                        Platform.exit(); // Close the application
                    });
                }
                case "scene2.fxml" -> {
                    HelloController2 scene2Controller = loader.getController();
                    scene2Controller.setHelloApplication(this);

                    primaryStage.setOnCloseRequest(event -> {
                        // Handle close request here
                        Platform.exit(); // Close the application
                    });
                }
                case "scene3.fxml" -> {
                    CrudlImplementation studentCourseEdit = loader.getController();
                    studentCourseEdit.setHelloApplication();
                }
                case "scene4.fxml" -> {
                    CourseEditImplementation CourseEdit = loader.getController();
                    CourseEdit.setHelloApplication();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void switchToScene1() {
        initializeScene("scene1.fxml", "Student Registration System Sign In");
    }

    public void switchToScene2() {
        initializeScene("scene2.fxml", "Student Registration");
    }

    public void switchToScene3(String studentID) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scene3.fxml"));
        try {
            StackPane root = loader.load();
            Scene scene = new Scene(root, 400, 600);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm());

            Stage newStage = new Stage();
            newStage.setTitle("CRUDL Implementation");
            newStage.setScene(scene);
            newStage.setResizable(false);
            newStage.show();

            CrudlImplementation crudlController = loader.getController();
            crudlController.setHelloApplication();
            crudlController.initializeStudentID(studentID); // Pass the student ID to Scene 3 controller

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchToScene4(String courseCode) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scene4.fxml"));
        try {
            StackPane root = loader.load();
            Scene scene = new Scene(root, 400, 600);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm());

            Stage newStage = new Stage();
            newStage.setTitle("Course CRUDL Implementation");
            newStage.setScene(scene);
            newStage.setResizable(false);
            newStage.show();

            CourseEditImplementation courseEditController = loader.getController();
            courseEditController.setHelloApplication();
            courseEditController.initializeCourseFields(courseCode); // Pass the student ID to Scene 3 controller

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
