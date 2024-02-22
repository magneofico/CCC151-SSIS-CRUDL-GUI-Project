package d.base.final_dbase;



import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class CrudlImplementation {

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

    // Instance variable to store the HelloApplication instance
    private HelloApplication helloApplication;

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
                // Set the retrieved student data into the text fields
                firstNameField.setText(student.getsFirstname());
                lastNameField.setText(student.getsLastname());
                middleNameField.setText(student.getsMiddlename());
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
    }

    @FXML
    private void backToTable() {
        helloApplication.switchToScene2();
    }
}
