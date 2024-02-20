package d.base.final_dbase;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Objects;

public class HelloApplication extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        switchToScene1();
    }

    private void initializeScene(String fxmlFileName, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFileName));
            StackPane root = loader.load();
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styling.css")).toExternalForm());

            primaryStage.setTitle(title);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false); // Make the stage not resizable
            primaryStage.show();

            // Set the controller for the scene
            switch (fxmlFileName) {
                case "scene1.fxml" -> {
                    HelloController1 scene1Controller = loader.getController();
                    scene1Controller.setHelloApplication(this);
                }
                case "scene2.fxml" -> {
                    HelloController2 scene2Controller = loader.getController();
                    scene2Controller.setHelloApplication(this);
                }
                case "scene3.fxml" -> {
                    CrudlImplementation studentCourseEdit = loader.getController();
                    studentCourseEdit.setHelloApplication(this);
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

    public void switchToScene3(){
        initializeScene("scene3.fxml", "CRUDL Implementation");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
