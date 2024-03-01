package d.base.final_dbase;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.*;

/**
 * {@code CourseEditImplementation} is the controller class responsible for managing CRUD operations
 * (Create, Read, Update, Delete) related to course information in the application.
 * It interacts with the user interface elements defined in the corresponding FXML file,
 * such as text fields, combo boxes, and buttons, to edit course data.
 * This class provides methods to initialize the UI components, fetch and display course data,
 * update course information, delete course records, and navigate back to the table view.
 * Additionally, it includes exception handling for potential errors during data operations.
 *
 * @author  Kristoffer Neo Senyahan | BSCS2 | kristofferneo.senyahan@g.msuiit.edu.ph | SSIS CCC151 Project
 */
public class CourseEditImplementation {

    /** Annotated {@code @FXML} fields for edit course registration details part.
     *  These fields are specifically for managing course registration within the application.*/
    @FXML private ComboBox<String> CollegeComboBoxEdit;
    @FXML private TextField courseCodeField;
    @FXML private TextField CourseNameField;
    @FXML private Button deleteCourseData;
    @FXML private Button saveUpdatedCourseData;
    @FXML private Button closeButton;

    /** Instance of the main application.*/
    public void setHelloApplication() {
    }

    /**Initializes the course name text field with the provided course name and fetches and sets the corresponding
     * course data based on the provided course name.
     *
     * @param courseName The course name to initialize.*/
    public void initializeCourseFields(String courseName) {
        CourseNameField.setText(courseName);
        fetchAndSetCourseData(courseName);
    }

    /** Method to fetch course data based on the provided course name and set it into text fields*/
    private void fetchAndSetCourseData(String courseName) {
        try {
            Course course = CSVHandler.fetchCourseName(courseName);
            if (course != null) {
                CourseNameField.setText(course.getCourseName());
                courseCodeField.setText(course.getCourseCode()); // Set the course code text
                CollegeComboBoxEdit.setValue(course.getCollegeName()); // Set students' course

            } else {
                CourseNameField.clear(); // Clear the course name field
                courseCodeField.clear(); // Clear the course code field
                CollegeComboBoxEdit.getSelectionModel().clearSelection(); // Clear the college combobox text-field
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Initializes the controller when the FXML is loaded.*/
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

    /** Handles the action when the user clicks the save button to update course information.
     *  It retrieves the entered data from the text fields, including the course name, course code,
     *  and college name, and then updates the corresponding course record.*/
    @FXML
    private void handleSaveButton() {
        String courseName = CourseNameField.getText().trim();
        if (courseName.isEmpty()) {
            System.out.println("Student ID is empty.");
            return;
        }

        String updatedCourseName = CourseNameField.getText().trim(); // Get selected updated course name from the text-field
        String updatedCourseCode = courseCodeField.getText().trim(); // Get selected updated course code from the text-field
        String updatedCollegeName = CollegeComboBoxEdit.getValue(); // Get selected updated course from the text-field

        // Update the student record
        boolean success = CSVHandler.updateCourse(updatedCourseName, updatedCourseCode, updatedCollegeName);
        if (success) {
            System.out.println("Course information updated successfully."); // Debugging statements
        } else {
            System.out.println("Failed to update course information."); // Debugging statements
        }
    }

    /** Handles the action when the user clicks the save updated course data button.*/
    @FXML
    private void handleSaveUpdatedCourseData() {
        System.out.println("Save button clicked."); // Add this line for debugging
        handleSaveButton(); // Call handleSaveButton when the saveUpdatedStudentData button is clicked
    }

    /** Handles the action when the 'delete' button is clicked.
     *  It retrieves the course name from the corresponding text field and attempts to delete the course record.*/
    @FXML
    private void handleDeleteButton() {
        String courseName = CourseNameField.getText().trim();
        if (courseName.isEmpty()) {
            System.out.println("Course name is empty."); // Debugging statements
            return;
        }
        boolean success = deleteCourse(courseName); // Call a method to delete the course
        if (success) {
            System.out.println("Course deleted successfully."); // Debugging statements
            clearFields(); // Clear text-fields
        } else {
            System.out.println("Failed to delete course."); // Debugging statements
        }
    }

    /**Deletes a course record from the CSV file along with corresponding student records.
     *
     * @param courseName The name of the course to be deleted.
     * @return True if the course record is deleted successfully, false otherwise.*/
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
                        System.out.println("Course '" + courseFullName + "' deleted successfully."); // Debugging statements
                        // Clear the text fields
                        clearFields();
                    } else {
                        System.out.println("Failed to delete student records for course '" + courseFullName + "'."); // Debugging statements
                    }
                } else {
                    System.out.println("Failed to delete course."); // Debugging statements
                }
                return success;
            } else {
                System.out.println("Course with name " + courseName + " not found."); // Debugging statements
                return false;
            }
        } catch (IOException e) {
            System.out.println("Error occurred while deleting course."); // Debugging statements
            e.printStackTrace();
            return false;
        }
    }

    /** Clears all text fields in the user interface.*/
    private void clearFields() {
        CourseNameField.clear();
        CollegeComboBoxEdit.getSelectionModel().clearSelection();
    }

    /** Handles the action when the user wants to navigate back to the table view.
     *  It retrieves the stage associated with the current scene and closes it,
     *  effectively returning to the previous scene.*/
    @FXML
    private void backToTable() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close(); // Close the stage to exit the current scene
    }
}
