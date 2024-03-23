package d.base.final_dbase;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * {@code HelloController2.java} is the Java class controller of Scene 2.
 *The main responsibilities of this class include:
 * <p>
 * <ul>
 *     <li> This class handles the user interactions and functionalities related to registering students and courses.
 *     <li> It facilitates the retrieval and storage of necessary details for student and course registration in the school database.
 *     <li> This class also manages the reading and writing of data to and from the database, which are the .csv files.
 *     <li> Additionally, it provides detailed exception handling to ensure robustness during data operations, such as retrieving desired details.
 * </ul>
 * </p>

 * <p>
 * This class also manages the functionality of various buttons used in Scene 2:
 * <ul>
 *     <li>{@code backButton}: Handles the action of navigating back to the previous scene.</li>
 *     <li>{@code refreshButton}: Handles the action of refreshing the view with updated data.</li>
 *     <li>{@code deleteButton}: Handles the action of deleting selected data entries.</li>
 *     <li>{@code registrationButton}: Handles the action of registering new students or courses.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Additionally, this class populates the table view with the retrieved data, displaying information
 * about registered students and courses in a structured format for user interaction and management.
 * </p>
 *
 * @author  Kristoffer Neo Senyahan | BSCS2 | kristofferneo.senyahan@g.msuiit.edu.ph | SSIS CCC151 Project
 */

public class HelloController2 {

    /** Annotated {@code @FXML} fields for student registration part.
     *  These fields are specifically for managing student registration within the application.*/
    @FXML private Button registerThisStudent;
    @FXML private TextField lastNameField;
    @FXML private TextField firstNameField;
    @FXML private TextField middleNameField;
    @FXML private TextField studentIDField;
    @FXML private ComboBox<String> yearLevelComboBox;
    @FXML private ComboBox<String> sexComboBox;
    @FXML private ComboBox<String> courseCodeCombo;
    @FXML private ComboBox<String> collegeComboBox;

    /** Annotated {@code @FXML} fields for course registration part.
     *  These fields are specifically for managing course registration within the application.*/
    @FXML private Button registerThisCourse;
    @FXML private TextField courseCodeField;
    @FXML private TextField courseNameField;
    @FXML private TableView<Course> tableView;
    @FXML private TableColumn<Course, String> tCourseCode;
    @FXML private TableColumn<Course, String> tCourseName;
    @FXML private TableColumn<Course, String> tCollege;

    /** Annotated {@code @FXML} fields for displaying registered students and courses information in the TableView.
     *  These fields are specifically for populating the TableView with information about registered students and courses in the GUI.*/
    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> sStudentID;
    @FXML private TableColumn<Student, String> sLastname;
    @FXML private TableColumn<Student, String> sFirstname;
    @FXML private TableColumn<Student, String> sMiddlename;
    @FXML private TableColumn<Student, String> sSex;
    @FXML private TableColumn<Student, String> sYearLevel;
    @FXML private TableColumn<Student, String> sCourse;
    @FXML private TableColumn<Student, String> sStatus; // ENROLLED or NOT ENROLLED

    /** Annotated {@code @FXML} fields for UI elements in the application.
     *  These fields are used for user interaction and navigation within the application.
     *  Primarily for searching and editing course and student purposes.*/
    @FXML private TextField findStudentID;
    @FXML private Button findStudentIDSearch;
    @FXML private TextField findCourse;
    @FXML private Button findCourseSearch;
    @FXML private Button backButton;// Button to go back to full list view of all the students after a specific search of information.
    @FXML private Button goToStudentDataEdit;//Button switch scene to student information edit.
    @FXML private Button goToCourseDataEdit;//Button switch scene to course information edit.
    @FXML private Button refresh;// Button to refresh, re-fetch and rewrite data in the TableView.


    /** Path to the CSV file containing student data.*/
    private static final String STUDENT_CSV_FILE_PATH = "/Users/kristofferneo/Downloads/SSIS-JavaFX-Final/Final_dbase/src/main/java/d/base/final_dbase/assets/studentData.csv";

    /** Path to the CSV file containing course data.*/
    private static final String COURSE_CSV_FILE_PATH = "/Users/kristofferneo/Downloads/SSIS-JavaFX-Final/Final_dbase/src/main/java/d/base/final_dbase/assets/courseData.csv";

    /** Instance of the main application.*/
    private HelloApplication helloApplication;

    public void setHelloApplication(HelloApplication helloApplication) {
        this.helloApplication = helloApplication;
    }

    /** Initializes UI components, populates combo boxes, sets up event listeners,
     *  and populates table views with data from CSV files. */
    @FXML private void initialize() {
        yearLevelComboBox.getItems().addAll("1st", "2nd", "3rd", "4th", "5th"); //student year-level combobox options.
        sexComboBox.getItems().addAll("Male", "Female"); //student sexes combobox options.

        Course.populateCourseComboBox(courseCodeCombo, COURSE_CSV_FILE_PATH); //Populates course combobox from Course java file class.
        courseCodeCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                courseCodeCombo.getEditor().setText(newValue);
            }
        });

        Course.populateCollegeComboBox(collegeComboBox); //Populates college combobox from Course java file class.

        yearLevelComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (newValue.matches("\\d+")) { // Check if the input is numeric
                    int index = Integer.parseInt(newValue) - 1;
                    if (index >= 0 && index < yearLevelComboBox.getItems().size()) {
                        yearLevelComboBox.getSelectionModel().select(index);
                    }
                } else {
                    String text = newValue.toLowerCase();
                    for (int i = 0; i < yearLevelComboBox.getItems().size(); i++) {
                        if (yearLevelComboBox.getItems().get(i).toLowerCase().startsWith(text)) {
                            yearLevelComboBox.getSelectionModel().select(i);
                            break;
                        }
                    }
                }
            }
        });

        sexComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                String text = newValue.toLowerCase();
                for (int i = 0; i < sexComboBox.getItems().size(); i++) {
                    if (sexComboBox.getItems().get(i).toLowerCase().startsWith(text)) {
                        sexComboBox.getSelectionModel().select(i);
                        break;
                    }
                }
            }
        });

        try {
            tableView.setItems(CSVHandler.getCoursesAsObservableList(COURSE_CSV_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            studentTable.setItems(CSVHandler.getStudentsAsObservableList(STUDENT_CSV_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setupStudentTableView(); // Set-up and enables display of registered student information in the GUI TableView.
        setupCourseTableView(); // Set-up Enables display of registered courses information in the GUI TableView.


        backButton.setOnAction(event -> handleBackButtonAction()); // Handles back action.
        refresh.setOnAction(event -> handleRefreshButtonAction()); // Handles refresh action.

        registerThisStudent.setOnAction(event -> addStudent()); // Handles register student action.
        registerThisCourse.setOnAction(event -> addCourse()); // Handles register course action.

        findStudentIDSearch.setOnAction(event -> handleStudentIDSearchButtonAction()); // Handles search studentID action.
        findCourseSearch.setOnAction(event -> handleCourseSearchButtonAction()); // Handles search course action.

        // Handles edit student data action.
        goToStudentDataEdit.setOnAction(event -> {
            goToStudentDataEditClicked();
            findStudentIDEdit();
        });

        // Handles edit course data action.
        goToCourseDataEdit.setOnAction(event -> {
            goToCourseDataEditClicked();
            findCourseEdit();
        });

    }

    /** Sets up the table view for displaying course information.
     *  Configures table columns and populates the table view with data.*/
    private void setupCourseTableView() {
        tCourseCode.setCellValueFactory(data -> data.getValue().courseCodeProperty());
        tCourseName.setCellValueFactory(data -> data.getValue().courseNameProperty());
        tCollege.setCellValueFactory(data -> data.getValue().collegeProperty());

        // Populate table view with data
        refreshCourseTableView();
    }

    /** Sets up the table view for displaying student information.
     *  Configures table columns and populates the table view with data.*/
    private void setupStudentTableView() {
        // Configure table columns
        sStudentID.setCellValueFactory(data -> data.getValue().sStudentIDProperty());
        sLastname.setCellValueFactory(data -> data.getValue().sLastnameProperty());
        sFirstname.setCellValueFactory(data -> data.getValue().sFirstnameProperty());
        sMiddlename.setCellValueFactory(data -> data.getValue().sMiddlenameProperty());
        sSex.setCellValueFactory(data -> data.getValue().sSexProperty());
        sYearLevel.setCellValueFactory(data -> data.getValue().sYearLevelProperty());
        sCourse.setCellValueFactory(data -> data.getValue().sCourseProperty());
        sStatus.setCellValueFactory(data -> data.getValue().sStatusProperty());

        // Populate table view with data
        refreshStudentTableView();
    }

    /** Adds a new student to the student database based on user input.
     *  Validates input fields, checks for duplicate student ID, and writes student information to CSV file.
     *  Displays appropriate error messages if validation fails or if the student already exists.*/
    private void addStudent() {
        String sStudentID = studentIDField.getText().trim();
        String sLastname = capitalizeFirstLetter(lastNameField.getText().trim());
        String sFirstname = capitalizeFirstLetter(firstNameField.getText().trim());
        String sMiddlename = capitalizeFirstLetter(middleNameField.getText().trim());
        String sYearLevel = yearLevelComboBox.getValue();
        String sSex = sexComboBox.getValue() != null ? (sexComboBox.getValue().equals("Male") ? "M" : "F") : "";
        String sCourse = courseCodeCombo.getValue();
        String sStatus = sCourse != null ? "ENROLLED" : "NOT ENROLLED";

        // Validate fields and construct an error message
        String errorMessage = validateFields(sStudentID, sLastname, sFirstname, sYearLevel, sSex, sCourse);
        if (errorMessage != null) {
            displayErrorAlert(errorMessage);
            return;
        }

        // Validate student ID format
        if (!sStudentID.matches("\\d{4}-\\d{4}")) {
            displayErrorAlert("Student ID must follow the format ####-#### (e.g., 1234-5678)");
            return;
        }

        // Check if student already exists
        try (BufferedReader brS = new BufferedReader(new FileReader(STUDENT_CSV_FILE_PATH))) {
            String line;
            boolean studentExists = false;
            while ((line = brS.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String existingStudentID = parts[1].trim();
                    if (existingStudentID.equals(sStudentID)) {
                        studentExists = true;
                        break;
                    }
                }
            }

            if (studentExists) {
                displayErrorAlert("A student with the same ID already exists.");
                clearTextFields();
            } else {
                // Add student information to the CSV file
                try (BufferedWriter studentInfoWriter = new BufferedWriter(new FileWriter(STUDENT_CSV_FILE_PATH, true))) {
                    // Check if the CSV file already exists
                    if (!Files.exists(Paths.get(STUDENT_CSV_FILE_PATH))) {
                        studentInfoWriter.write("Registration Time,Student ID,Last Name,First Name,Middle Name,Sex,Year Level,Course,Status");
                        studentInfoWriter.newLine();
                    }

                    // Get current timestamp
                    LocalDateTime sTimestamp = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
                    String formattedTimestamp = sTimestamp.format(formatter);

                    // Write student information to the CSV file
                    String studentInfo = String.join(",", formattedTimestamp, sStudentID, sLastname, sFirstname, sMiddlename, sSex, sYearLevel, sCourse, sStatus);
                    studentInfoWriter.write(studentInfo);
                    studentInfoWriter.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Populate course code combo box
        populateCourseComboBox();

        // Set items in the student table view with data from the CSV file
        try {
            studentTable.setItems(CSVHandler.getStudentsAsObservableList(STUDENT_CSV_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Clear text fields after successful addition
        clearTextFields();
    }

    private String validateFields(String sStudentID, String sLastname, String sFirstname, String sYearLevel, String sSex, String sCourse) {
        StringBuilder errorMessage = new StringBuilder("Please fill out all required fields;\n");
        if (sStudentID.isEmpty()) {errorMessage.append("Student ID, ");}
        if (sLastname.isEmpty()) {errorMessage.append("Lastname, ");}
        if (sFirstname.isEmpty()) {errorMessage.append("Firstname, ");}
        if (sYearLevel == null) {errorMessage.append("Year Level, ");}
        if (sSex.isEmpty()) {errorMessage.append("Sex, ");}
        if (sCourse == null) {errorMessage.append("Course, ");}

        // Remove the trailing comma and space
        if (errorMessage.charAt(errorMessage.length() - 2) == ',') {
            errorMessage.deleteCharAt(errorMessage.length() - 2);
        }

        return errorMessage.length() > 36 ? errorMessage.toString() : null;
    }

    private void populateCourseComboBox() {
        Course.populateCourseComboBox(courseCodeCombo, COURSE_CSV_FILE_PATH);
    }








    /** Adds a new course to the student database based on user input.
     *  Validates input fields, checks for duplicate student ID, and writes course information to CSV file.
     *  Displays appropriate error messages if validation fails or if the course already exists.*/
    private void addCourse() {
        // Get user input for course code, course name, and college assigned
        String courseCode = courseCodeField.getText().trim();
        String courseName = capitalizeFirstLetter(courseNameField.getText().trim());
        String collegeAssigned = collegeComboBox.getValue() != null ? collegeComboBox.getValue().trim() : "";

        // Check if any of the fields are null or empty
        if (collegeAssigned.isEmpty() || courseCode.isEmpty() || courseName.isEmpty()) {
            // Construct a message to specify which fields are empty
            StringBuilder errorMessage = new StringBuilder("Please fill out all required fields;\n");

            if (collegeAssigned.isEmpty()) {
                errorMessage.append("College, ");
            }
            if (courseCode.isEmpty()) {
                errorMessage.append("Course Code, ");
            }
            if (courseName.isEmpty()) {
                errorMessage.append("Course Name, ");
            }

            // Remove the trailing comma and space
            if (errorMessage.charAt(errorMessage.length() - 2) == ',') {
                errorMessage.deleteCharAt(errorMessage.length() - 2);
            }

            // Display the error alert with the constructed message
            displayErrorAlert(errorMessage.toString());
            return; // Exit the method
        }

        // Check if the course code already exists in the CSV file
        if (courseExists(courseCode)) {
            displayErrorAlert("A course with the same code already exists.");
            clearTextFields();
            return;
        }

        // Get current timestamp
        String formattedTimestamp = getFormattedTimestamp();

        // Write the course information to the CSV file
        writeCourseToCSV(formattedTimestamp, courseCode, courseName, collegeAssigned);

        // Update UI components
        updateUIComponents();
    }

    private boolean courseExists(String courseCode) {
        try (BufferedReader brC = new BufferedReader(new FileReader(COURSE_CSV_FILE_PATH))) {
            String line;
            while ((line = brC.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) { // Assuming course code is at least present
                    String existingCourseCode = parts[1].trim();
                    if (existingCourseCode.equals(courseCode)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getFormattedTimestamp() {
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }

    private void writeCourseToCSV(String formattedTimestamp, String courseCode, String courseName, String collegeAssigned) {
        try (BufferedWriter courseInfoWriter = new BufferedWriter(new FileWriter(COURSE_CSV_FILE_PATH, true))) {
            // Check if the CSV file already exists
            if (!Files.exists(Paths.get(COURSE_CSV_FILE_PATH))) {
                // If not, write the header
                courseInfoWriter.write("Registration Time,Course Code,Course Name,College");
                courseInfoWriter.newLine();
            }

            // Write the course information to the CSV file
            String courseInfo = String.join(",", formattedTimestamp, courseCode, courseName, collegeAssigned);
            courseInfoWriter.write(courseInfo);
            courseInfoWriter.newLine();

            clearTextFields(); // Clear text-fields
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateUIComponents() {
        // Update the combo box with the new course
        Course.populateCourseComboBox(courseCodeCombo, COURSE_CSV_FILE_PATH);
        courseCodeCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                courseCodeCombo.getEditor().setText(newValue);
            }
        });

        // Update the table view with the new data
        try {
            tableView.setItems(CSVHandler.getCoursesAsObservableList(COURSE_CSV_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayErrorAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }




    /**
     * Capitalizes the first letter of each word in the given string.
     * Words "of" and "in" are not capitalized unless they appear at the beginning of the string.
     *
     * @param str the string to capitalize
     * @return the string with the first letter of each word capitalized
     */
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        String[] words = str.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (!word.equalsIgnoreCase("of") && !word.equalsIgnoreCase("in")) {
                result.append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase()).append(" ");
            } else {
                result.append(word.toLowerCase()).append(" ");
            }
        }

        return result.toString().trim();
    }

    /**
     * Handles the action when the back button is clicked.
     * Clears the search fields for both students and courses.
     * Clears the table view of students.
     * Optionally, repopulates the student table with all students from the CSV file.
     */
    private void handleBackButtonAction() {
        findCourse.clear(); // Clear the search field
        findStudentID.clear(); // Assuming you have a TextField named findStudentID for searching students

        // Clear the table
        studentTable.getItems().clear();

        // Optionally, you can populate the student table with all students again
        try {
            studentTable.setItems(CSVHandler.getStudentsAsObservableList(STUDENT_CSV_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the action when the refresh button is clicked.
     * Reloads data from CSV files for both courses and students.
     * Updates table views with new data.
     */
    @FXML private void handleRefreshButtonAction() {
        try {
            // Reload data from CSV files
            List<Course> courses = CSVHandler.getCoursesAsObservableList(COURSE_CSV_FILE_PATH);
            List<Student> students = CSVHandler.getStudentsAsObservableList(STUDENT_CSV_FILE_PATH);

            // Update table views with new data
            tableView.setItems(FXCollections.observableArrayList(courses));
            studentTable.setItems(FXCollections.observableArrayList(students));
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception accordingly
        }
    }

    /**
     * Handles the action when the student ID search button is clicked.
     * Retrieves student information based on the entered student ID and updates the table view.
     * Displays appropriate alerts for invalid input, a student not found, or errors during the process.
     */
    private void handleStudentIDSearchButtonAction() {
        // Handle button click event here
        String studentID = findStudentID.getText().trim();
        if (!studentID.isEmpty()) {
            try {
                // Replace "students.csv" with the actual file path
                Student student = CSVHandler.getStudentByID(STUDENT_CSV_FILE_PATH, studentID);
                if (student != null) {
                    // Clear previous data in the table
                    studentTable.getItems().clear();

                    // Add the found student to the table
                    studentTable.getItems().add(student);
                } else {
                    // Display an alert indicating that the student ID was not found
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Student ID not found.");
                    alert.showAndWait();
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception accordingly
                // Print the stack trace to the console for debugging purposes

                // Display an alert indicating the error
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("An error occurred. Please try again.");
                errorAlert.showAndWait();

                // Check if the user clicked OK
                errorAlert.getResult();// If OK clicked, return without further action
            }
        } else {
            // Display an alert indicating that the student ID is empty
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a student ID.");
            alert.showAndWait();
        }
    }

    /**
     * Handles the action when the course search button is clicked.
     * Retrieves students enrolled in the specified course or matching course names based on the entered search input.
     * Updates the table view with the retrieved student information.
     * Displays appropriate alerts for empty search input or when no students are found for the specified course.
     */
    private void handleCourseSearchButtonAction() {
        String searchInput = findCourse.getText().trim();
        if (!searchInput.isEmpty()) {
            try {
                // Replace "students.csv" with the actual file path
                Map<String, List<Student>> studentsByCourse = CSVHandler.getStudentsByCourse(STUDENT_CSV_FILE_PATH, searchInput);
                if (!studentsByCourse.isEmpty()) {
                    // Clear previous data in the table
                    studentTable.getItems().clear();

                    // Populate the table with students enrolled in the specified course
                    for (Student student : studentsByCourse.get(searchInput)) {
                        studentTable.getItems().add(student);
                    }
                } else {
                    // Search for substrings in course names
                    Map<String, List<Student>> studentsByCourseName = CSVHandler.getStudentsByCourseName(STUDENT_CSV_FILE_PATH, searchInput);
                    if (!studentsByCourseName.isEmpty()) {
                        // Clear previous data in the table
                        studentTable.getItems().clear();

                        // Populate the table with students enrolled in the specified course
                        for (Map.Entry<String, List<Student>> entry : studentsByCourseName.entrySet()) {
                            for (Student student : entry.getValue()) {
                                studentTable.getItems().add(student);
                            }
                        }
                    } else {
                        // Display an alert indicating that no students are enrolled in the specified course
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information");
                        alert.setHeaderText(null);
                        alert.setContentText("No students are enrolled in the specified course.");
                        alert.showAndWait();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception accordingly
            }
        } else {
            // Display an alert indicating that the search input is empty
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a search input.");
            alert.showAndWait();
        }
    }

    /** Refreshes the table view of courses by reloading data from the CSV file.*/
    private void refreshCourseTableView() {
        try {
            ObservableList<Course> courses = CSVHandler.getCoursesAsObservableList(COURSE_CSV_FILE_PATH);
            Platform.runLater(() -> tableView.setItems(courses));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Refreshes the table view of students by reloading data from the CSV file.*/
    private void refreshStudentTableView() {
        try {
            ObservableList<Student> students = CSVHandler.getStudentsAsObservableList(STUDENT_CSV_FILE_PATH);
            Platform.runLater(() -> studentTable.setItems(students));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Clears all text fields and resets combo box selections to null.*/
    private void clearTextFields() {
        lastNameField.clear();
        firstNameField.clear();
        middleNameField.clear();
        studentIDField.clear();
        courseNameField.clear();
        courseCodeField.clear();
        sexComboBox.setValue(null);
        yearLevelComboBox.setValue(null);
        courseCodeCombo.setValue(null);
        collegeComboBox.setValue(null);
    }

    // Switch back to scene 1 Login/SignIn pane.
    @FXML private void switchToScene1() {
        helloApplication.switchToScene1();
    }

    private boolean goToStudentDataEditClicked;

    /**Edit registered student information implementation.*/
    @FXML private void findStudentIDEdit() {
        String studentID = findStudentID.getText().trim();
        if (studentID.isEmpty() || !studentID.matches("\\d{4}-\\d{4}")) {
            // Display an alert indicating the error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            if (studentID.isEmpty()) {
                alert.setContentText("Student ID cannot be empty.");
            } else {
                alert.setContentText("Student ID must follow the format ####-#### (e.g., 1234-5678)");
            }
            alert.showAndWait();
            return;
        }

        if (goToStudentDataEditClicked) {
            // Open Scene 3 for editing student details
            helloApplication.switchToScene3(studentID);
        } else {
            // Handle the case where the windowSwitch3 button is not clicked
            System.out.println("windowSwitch3 button is not clicked.");
        }
    }

    @FXML private void goToStudentDataEditClicked() {
        // Set the flag to indicate that the windowSwitch3 button is clicked
        goToStudentDataEditClicked = true;
    }


    private boolean goToCourseDataEditClicked;

    /**Edit registered course information implementation.*/
    @FXML private void findCourseEdit() {
        String courseCode = findCourse.getText().trim();
        if (courseCode.isEmpty()) {
            // Handle the case where the text field is empty (this should not happen due to your existing validation)
            return;
        }

        if (goToCourseDataEditClicked) {
            // Open Scene 4 for editing course details
            helloApplication.switchToScene4(courseCode);
        } else {
            // Display a message indicating that the windowSwitch4 button must be clicked
            System.out.println("Cannot open Scene 4. Please click the windowSwitch4 button.");
        }
    }

    @FXML private void goToCourseDataEditClicked() {
        // Set the flag to indicate that the windowSwitch4 button is clicked
        goToCourseDataEditClicked = true;
    }
}