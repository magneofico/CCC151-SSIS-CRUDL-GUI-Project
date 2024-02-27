package d.base.final_dbase;



import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CrudlImplementation {

    // Instance variable to store the HelloApplication instance
    private HelloApplication helloApplication;

    // FXML fields
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField genderField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField middleNameField;

    @FXML
    private TextField studentIDField;

    @FXML
    private TextField yearLevelField;

    @FXML
    private ComboBox<String> CrudlCourseCode;

    @FXML
    private Button saveUpdatedStudentData;

    // Method to set the HelloApplication instance
    public void setHelloApplication(HelloApplication helloApplication) {
        this.helloApplication = helloApplication;
    }

    // Method to fetch student data based on the provided student ID and set it into text fields
    private void fetchAndSetStudentData(String studentID) {
        try {
            // Fetch student data based on the provided student ID
            Student student = CSVHandler.fetchStudentByID(studentID);
            if (student != null) {
                // Set the retrieved student data into the text fields and ComboBox
                firstNameField.setText(student.getsFirstname());
                lastNameField.setText(student.getsLastname());
                middleNameField.setText(student.getsMiddlename());
                genderField.setText(student.getsSex());
                yearLevelField.setText(student.getsYearLevel());
                CrudlCourseCode.setValue(student.getsCourse()); // Set student's course
            } else {
                // Handle the case where student data is not found
                System.out.println("Student data not found for ID: " + studentID);
            }
        } catch (IOException e) {
            // Handle the IOException
            e.printStackTrace();
        }
    }


    // Method to initialize the student ID field with the provided student ID
    public void initializeStudentID(String studentID) {
        // Set the provided student ID into the student ID text field
        studentIDField.setText(studentID);
        // Fetch and set student data based on the provided student ID
        fetchAndSetStudentData(studentID);
    }

    // Initialize method called when the FXML is loaded
    @FXML
    private void initialize() {
        // Add a listener to the student ID text field
        studentIDField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Fetch and set student data when the text field content changes
            fetchAndSetStudentData(newValue);
        });

        // Populate the course ComboBox
        Course.populateCourseComboBox(CrudlCourseCode, "/Users/kristofferneo/registration_dbase/src/course_registration.csv");
        CrudlCourseCode.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                CrudlCourseCode.getEditor().setText(newValue);
            }
        });

        saveUpdatedStudentData.setOnAction(event -> handleSaveUpdatedStudentData());
    }

    @FXML
    private void handleSaveButton() {
        String studentID = studentIDField.getText().trim();
        if (studentID.isEmpty()) {
            System.out.println("Student ID is empty.");
            return;
        }

        // Retrieve updated information from the text fields
        String updatedFirstName = firstNameField.getText().trim();
        String updatedLastName = lastNameField.getText().trim();
        String updatedMiddleName = middleNameField.getText().trim();
        String updatedGender = genderField.getText().trim();
        String updatedYearLevel = yearLevelField.getText().trim();

        // Update the student record
        boolean success = CSVHandler.updateStudent(studentID, updatedFirstName, updatedLastName, updatedMiddleName, updatedGender, updatedYearLevel);
        if (success) {
            System.out.println("Student information updated successfully.");
        } else {
            System.out.println("Failed to update student information.");
        }
    }




    @FXML
    private void handleSaveUpdatedStudentData() {
        System.out.println("Save button clicked."); // Add this line for debugging
        handleSaveButton(); // Call handleSaveButton when the saveUpdatedStudentData button is clicked
    }

    @FXML
    private void backToTable() {
        helloApplication.switchToScene2();
    }
}
