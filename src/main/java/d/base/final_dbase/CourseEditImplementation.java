package d.base.final_dbase;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CourseEditImplementation {

    @FXML
    private ComboBox<String> CollegeComboBoxEdit;

    @FXML
    private TextField courseCodefield;

    @FXML
    private TextField CourseNameField;

    @FXML
    private Button deleteCourseData;

    @FXML
    private Button saveUpdatedCourseData;

    @FXML
    private Button closeButton;

    private HelloApplication helloApplication;

    public void setHelloApplication(HelloApplication helloApplication) {
        this.helloApplication = helloApplication;
    }

    public void initializeCourseFields(String courseName) {
        CourseNameField.setText(courseName);
        fetchAndSetCourseData(courseName);
    }

    private void fetchAndSetCourseData(String courseName) {
        try {
            Course course = CSVHandler.fetchCourseName(courseName);
            if (course != null) {
                CourseNameField.setText(course.getCourseName());
                courseCodefield.setText(course.getCourseCode()); // Set the course code text
            } else {
                CourseNameField.clear(); // Clear the course name field
                courseCodefield.clear(); // Clear the course code field
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    @FXML
    private void initialize() {


        Course.populateCollegeComboBox(CollegeComboBoxEdit);
        CourseNameField.textProperty().addListener((observable, oldValue, newValue) -> fetchAndSetCourseData(newValue));

        // Fetch and set course data immediately when initializing
        fetchAndSetCourseData(CourseNameField.getText());

        closeButton.setOnAction(event -> backToTable());
    }


    @FXML
    private void backToTable() {
        // Get the stage associated with the current scene
        Stage stage = (Stage) closeButton.getScene().getWindow(); // Assuming backButton is a button in the scene

        // Close the stage to exit the current scene
        stage.close();
    }
}
