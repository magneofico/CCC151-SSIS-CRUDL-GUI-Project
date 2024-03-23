/**
 * This package file is using a JavaFX language for the SQL Server Integration Services (SSIS) of students'
 * and courses registration. The code also includes various implementations of functions primarily Create,
 * Read, Update and Delete (CRUDL) scattered among the files and classes executions.
 *
 * @author Kristoffer Neo Senyahan | BSCS2
 * @email kristofferneo.senyahan@g.msuiit.edu.ph
 * @project SSIS CCC151 Project
 **/

package d.base.final_dbase;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * This class integrates all the scenes and Stages of every section.
 * This also serves as the Main file and the Main stage of the whole package implementations.
 */
public class HelloApplication extends Application {

    private Stage primaryStage;

    /**
     * This method overrides the start method of the JavaFX Application class.
     * It initializes the primary stage and sets up the initial scene (Scene 1).
     * Additionally, it registers an event handler to handle the close request
     * event of the primary stage.
     *
     * @param primaryStage The primary stage of the JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        switchToScene1();

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit(); // Close the application
        });
    }

    /**
     * This method initializes a scene using an FXML file and sets up the primary stage.
     * It loads the FXML file, sets up the scene with specified dimensions, adds styling,
     * and sets event handlers for the primary stage, including close request handling.
     * Additionally, it sets the controller for the scene based on the FXML file loaded.
     *
     * @param fxmlFileName The name of the FXML file to load.
     * @param title The title of the scene.
     */
    private void initializeScene(String fxmlFileName, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            StackPane root = loader.load();
            Scene scene = new Scene(root, 1100, 800); // sets the size of the application pane to 1100x800 pixels.
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm()); // Adds the external CSS file "styling.css" for styling the scene.

            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // Make the stage not resizable
            primaryStage.setOnCloseRequest(event -> {
                event.consume(); // Consume the event to prevent the window from closing immediately
                switchToScene2(); // Switch to Scene 2
            });
            primaryStage.show();

            // Set the controllers for each of the scenes.
            switch (fxmlFileName) {
                case "scene1.fxml" -> {
                    HelloController1 scene1Controller = loader.getController();
                    scene1Controller.setHelloApplication(this);

                    primaryStage.setOnCloseRequest(event -> {
                        Platform.exit(); // Close the application
                    });
                }
                case "scene2.fxml" -> {
                    InitializingStage scene2Controller = loader.getController();
                    scene2Controller.setInitializingStage(this);

                    primaryStage.setOnCloseRequest(event -> {
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

    /** Switches to Scene 1: Student Registration System Sign In.*/
    public void switchToScene1() {
        initializeScene("scene1.fxml", "Student Registration System Sign In");
    }

    /** Switches to Scene 2: Student Registration.*/
    public void switchToScene2() {
        initializeScene("scene2.fxml", "Student Registration");
    }

    /**
     * Switches to Scene 3(CrudlImplementation) displaying student information.
     * This also loads Scene 3 FXML, sets up the stage, and initializes controller with student ID.
     *
     * @param studentID The ID of the student.
     */
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

            CrudlImplementation crudlController = loader.getController(); // Set controller for Scene 3 and initialize student ID
            crudlController.setHelloApplication();
            crudlController.initializeStudentID(studentID); // Pass the student ID to Scene 3 controller

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switches to Scene 3(CrudlImplementation) displaying student information.
     * This also loads Scene 3 FXML, sets up the stage, and initializes controller with student ID.
     *
     * @param courseName The courseCode of the course.
     */
    public void switchToScene4(String courseName) {
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
            courseEditController.initializeCourseFields(courseName); // Pass the student ID to Scene 3 controller

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Main method to launch the JavaFX application.*/
    public static void main(String[] args) {
        launch(args);
    }
}
