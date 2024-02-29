package d.base.final_dbase;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;

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


    private static final String STUDENT_CSV_FILE_PATH = "/Users/kristofferneo/registration_dbase/src/student_registration.csv";
    private static final String COURSE_CSV_FILE_PATH = "/Users/kristofferneo/registration_dbase/src/course_registration.csv";


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
        deleteCourseData.setOnAction(event -> handleDeleteButton());

    }

    @FXML
    private void handleDeleteButton() {
        String courseName = CourseNameField.getText().trim();
        if (courseName.isEmpty()) {
            System.out.println("Course name is empty.");
            return;
        }

        // Call a method to delete the course
        boolean success = deleteCourse(courseName);
        if (success) {
            System.out.println("Course deleted successfully.");
            // Clear the text fields
            clearFields();
        } else {
            System.out.println("Failed to delete course.");
        }
    }

    private boolean deleteCourse(String courseName) {
        try {
            // Delete the course record from the CSV file
            boolean success = CSVHandler.deleteCourse(courseName);
            if (!success) {
                System.out.println("Course with name " + courseName + " not found.");
            }
            return success;
        } catch (IOException e) {
            System.out.println("Error occurred while deleting course.");
            e.printStackTrace();
            return false;
        }
    }

    // Method to clear all fields
    private void clearFields() {
        CourseNameField.clear();
    }











    @FXML
    private void backToTable() {
        // Get the stage associated with the current scene
        Stage stage = (Stage) closeButton.getScene().getWindow(); // Assuming backButton is a button in the scene

        // Close the stage to exit the current scene
        stage.close();
    }
}
