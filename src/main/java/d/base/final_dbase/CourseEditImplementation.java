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
    private TextField courseCodeField;

    @FXML
    private TextField CourseNameField;

    @FXML
    private Button deleteCourseData;

    @FXML
    private Button saveUpdatedCourseData;

    @FXML
    private Button closeButton;

    public void setHelloApplication() {
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
                courseCodeField.setText(course.getCourseCode()); // Set the course code text
                CollegeComboBoxEdit.setValue(course.getCollegeName()); // Set student's course

            } else {
                CourseNameField.clear(); // Clear the course name field
                courseCodeField.clear(); // Clear the course code field
                CollegeComboBoxEdit.getSelectionModel().clearSelection();
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

        saveUpdatedCourseData.setOnAction(event -> handleSaveUpdatedCourseData());
        deleteCourseData.setOnAction(event -> handleDeleteButton());
        closeButton.setOnAction(event -> backToTable());
    }



    @FXML
    private void handleSaveButton() {
        String courseName = CourseNameField.getText().trim();
        if (courseName.isEmpty()) {
            System.out.println("Student ID is empty.");
            return;
        }

        // Retrieve updated information from the text fields
        String updatedCourseName = CourseNameField.getText().trim();
        String updatedCourseCode = courseCodeField.getText().trim();
        String updatedCollegeName = CollegeComboBoxEdit.getValue(); // Get selected course

        // Update the student record
        boolean success = CSVHandler.updateCourse(updatedCourseName, updatedCourseCode, updatedCollegeName);
        if (success) {
            System.out.println("Course information updated successfully.");
        } else {
            System.out.println("Failed to update course information.");
        }
    }

    @FXML
    private void handleSaveUpdatedCourseData() {
        System.out.println("Save button clicked."); // Add this line for debugging
        handleSaveButton(); // Call handleSaveButton when the saveUpdatedStudentData button is clicked
    }


    @FXML
    private void handleDeleteButton() {
        String courseName = CourseNameField.getText().trim();
        if (courseName.isEmpty()) {
            System.out.println("Course name is empty.");
            return;
        }
        boolean success = deleteCourse(courseName); // Call a method to delete the course
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
            // Get the course record from the CSV file
            Course course = CSVHandler.fetchCourseName(courseName);
            if (course != null) {
                // Extract the course code
                String courseCode = course.getCourseCode();

                // Create a string combining course name and course code
                String courseFullName = courseName + " (" + courseCode + ")";

                // Delete the course record from the CSV file
                boolean success = CSVHandler.deleteCourse(courseName);
                if (success) {
                    // Delete corresponding entries in student_registration.csv
                    boolean studentRecordsDeleted = CSVHandler.deleteStudentRecordsByCourse(courseFullName);
                    if (studentRecordsDeleted) {
                        System.out.println("Course '" + courseFullName + "' deleted successfully.");
                        // Clear the text fields
                        clearFields();
                    } else {
                        System.out.println("Failed to delete student records for course '" + courseFullName + "'.");
                    }
                } else {
                    System.out.println("Failed to delete course.");
                }
                return success;
            } else {
                System.out.println("Course with name " + courseName + " not found.");
                return false;
            }
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
        stage.close(); // Close the stage to exit the current scene
    }
}
