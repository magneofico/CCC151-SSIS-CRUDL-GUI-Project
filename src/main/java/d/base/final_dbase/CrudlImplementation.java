package d.base.final_dbase;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * {@code CrudlImplementation} is the controller class responsible for handling/editing CRUD operations
 * (Create, Read, Update, Delete) related to student information in the application.
 * It manages the user interface elements defined in the corresponding FXML file,
 * such as text fields, combo boxes, and buttons, to interact with student data.
 * This class provides methods to initialize the UI components, fetch and display student data,
 * update student information, delete student records, and navigate back to the table view.
 * Additionally, it includes exception handling for potential errors that may occur during data operations.
 *
 * @author  Kristoffer Neo Senyahan | BSCS2 | kristofferneo.senyahan@g.msuiit.edu.ph | SSIS CCC151 Project
 */
public class CrudlImplementation {

    /** Annotated {@code @FXML} fields for edit student registration details part.
     *  These fields are specifically for managing student registration within the application.*/
    @FXML private ComboBox<String> CrudlCourseCode;
    @FXML private TextField firstNameField;
    @FXML private TextField sexField;
    @FXML private TextField lastNameField;
    @FXML private TextField middleNameField;
    @FXML private TextField studentIDField;
    @FXML private TextField yearLevelField;
    @FXML private TextField enrollmentStatus;
    @FXML private Button saveUpdatedStudentData;
    @FXML private Button deleteStudentData;
    @FXML private Button closeButton;

    /** Instance of the main application.*/
    public void setHelloApplication() {
    }

    /** Method to fetch student data based on the provided student ID and set it into text fields*/
    private void fetchAndSetStudentData(String studentID) {
        try {
            // Fetch student data based on the provided student ID
            Student student = CSVHandler.fetchStudentByID(studentID);
            if (student != null) {
                // Set the retrieved student data into the text fields and ComboBox
                firstNameField.setText(student.getsFirstname());
                lastNameField.setText(student.getsLastname());
                middleNameField.setText(student.getsMiddlename());
                sexField.setText(student.getsSex());
                yearLevelField.setText(student.getsYearLevel());
                CrudlCourseCode.setValue(student.getsCourse()); // get student's course
                enrollmentStatus.setText(student.getsStatus());

            } else {
                System.out.println("Student data not found for ID: " + studentID); // Debugging statements
            }
        } catch (IOException e) {
            // Handle the IOException
            e.printStackTrace();
        }
    }

    /**Initializes the student ID text field with the provided student ID and fetches and sets the corresponding
     * student data based on the provided student ID.
     *
     * @param studentID The student ID to initialize.*/
    public void initializeStudentID(String studentID) {
        // Set the provided student ID into the student ID text field
        studentIDField.setText(studentID);
        // Fetch and set student data based on the provided student ID
        fetchAndSetStudentData(studentID);
    }

    /** Initializes the controller when the FXML is loaded.*/
    @FXML
    private void initialize() {
        // Add a listener to the student ID text field
        studentIDField.textProperty().addListener((observable, oldValue, newValue) -> {
            fetchAndSetStudentData(newValue); // Fetch and set student data when the text field content changes
        });

        // Populate the course ComboBox from Course java class file
        Course.populateCourseComboBox(CrudlCourseCode, "/Users/kristofferneo/registration_dbase/src/course_registration.csv");
        CrudlCourseCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                CrudlCourseCode.getEditor().setText(newValue);
            }
        });

        saveUpdatedStudentData.setOnAction(event -> handleSaveUpdatedStudentData()); // Handles save updated student data action.
        deleteStudentData.setOnAction(event -> handleDeleteButton());  // Handles delete student data action.
        closeButton.setOnAction(event -> backToTable()); // Handles close scene action.
    }

    /** Handles the action when the user clicks the save button to update student information.
     *  It retrieves the entered data from the text fields, including the student ID, first name, last name,
     *  middle name, gender, year level, and selected course, and then updates the corresponding student record.*/
    @FXML
    private void handleSaveButton() {
        String studentID = studentIDField.getText().trim();
        if (studentID.isEmpty()) {
            System.out.println("Student ID is empty."); // Debugging statements
            return;
        }

        // Retrieve updated information from the text fields
        String updatedFirstName = firstNameField.getText().trim(); // Get updated firstname from text-field
        String updatedLastName = lastNameField.getText().trim(); // Get updated lastname from text-field
        String updatedMiddleName = middleNameField.getText().trim(); // Get updated middlename from text-field
        String updatedSex = sexField.getText().trim(); // Get updated sex from text-field
        String updatedYearLevel = yearLevelField.getText().trim(); // Get updated year level from text-field
        String updatedCourse = CrudlCourseCode.getValue(); // Get updated selected course from text-field

        // Update the student record
        boolean success = CSVHandler.updateStudent(studentID, updatedLastName, updatedFirstName, updatedMiddleName, updatedSex, updatedYearLevel, updatedCourse);
        if (success) {
            System.out.println("Student information updated successfully."); // Debugging statements
        } else {
            System.out.println("Failed to update student information."); // Debugging statements
        }
    }

    /** Handles the action when the user clicks the save updated student data button.*/
    @FXML
    private void handleSaveUpdatedStudentData() {
        System.out.println("Save button clicked."); // Add this line for debugging
        handleSaveButton(); // Call handleSaveButton when the saveUpdatedStudentData button is clicked
    }

    /** Handles the action when the 'delete' button is clicked.*/
    @FXML private void handleDeleteButton() {
        String studentID = studentIDField.getText().trim();
        if (studentID.isEmpty()) {
            System.out.println("Student ID is empty."); // Debugging statements
            return;
        }

        boolean success = deleteStudent(studentID); // Call a method to delete the student record
        if (success) {
            System.out.println("Student information deleted successfully."); // Debugging statements
            clearFields(); // Clear text-fields
        } else {
            System.out.println("Failed to delete student information."); // Debugging statements
        }
    }


    /**Deletes the student record corresponding to the given student ID.
     * It attempts to delete the student record from the CSV file.
     *
     * @param studentID The ID of the student to be deleted.
     * @return True if the student record is deleted successfully, false otherwise.*/
    private boolean deleteStudent(String studentID) {
        try {
            // Delete the student record from the CSV file
            boolean success = CSVHandler.deleteStudent(studentID);
            if (!success) {
                System.out.println("Student with ID " + studentID + " not found."); // Debugging statements
            }
            return success;
        } catch (IOException e) {
            System.out.println("Error occurred while deleting student data."); // Debugging statements
            e.printStackTrace();
            return false;
        }
    }

    /** Clears all text fields in the user interface.*/
    private void clearFields() {
        firstNameField.clear();
        lastNameField.clear();
        middleNameField.clear();
        sexField.clear();
        yearLevelField.clear();
        CrudlCourseCode.getSelectionModel().clearSelection();
        enrollmentStatus.clear();
    }

    /** Handles the action when the user wants to navigate back to the table view.
     *  It retrieves the stage associated with the current scene and closes it,
     *  effectively returning to the previous scene.*/
    @FXML private void backToTable() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();// Close the stage to exit the current scene
    }
}
