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

public class HelloController2 {

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField middleNameField;

    @FXML
    private TextField studentIDField;

    @FXML
    private ComboBox<String> yearLevelComboBox; // Changed from TextField to ComboBox

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private ComboBox<String> courseCodeCombo;

    @FXML
    private ComboBox<String> collegeComboBox;

    @FXML
    private Button registerThisStudent;


    /* These @FXML are for Course Registration Table. */
    @FXML
    private Button courseRegister;

    @FXML
    private TextField courseCodeField;

    @FXML
    private TextField courseNameField;

    @FXML
    private TableView<Course> tableView;

    @FXML
    private TableColumn<Course, String> tCourseCode;

    @FXML
    private TableColumn<Course, String> tCourseName;

    @FXML
    private TableColumn<Course, String> tCollege;

    /* These @FXML are for Student Registration Table. */
    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableColumn<Student, String> sStudentID;

    @FXML
    private TableColumn<Student, String> sLastname;

    @FXML
    private TableColumn<Student, String> sFirstname;

    @FXML
    private TableColumn<Student, String> sMiddlename;

    @FXML
    private TableColumn<Student, String> sSex;

    @FXML
    private TableColumn<Student, String> sYearLevel;

    @FXML
    private TableColumn<Student, String> sCourse;

    @FXML
    private TableColumn<Student, String> sStatus;


    @FXML
    private Button refresh;

    @FXML
    private TextField findStudentID;

    @FXML
    private Button findStudentIDSearch;



    @FXML
    private TextField findCourse;

    @FXML
    private Button findCourseSearch;

    @FXML
    private Button backButton;

    @FXML
    private Button windowSwitch3;

    @FXML
    private Button windowSwitch4;





    private static final String STUDENT_CSV_FILE_PATH = "/Users/kristofferneo/Downloads/SSIS-JavaFX-Final/Final_dbase/src/main/java/d/base/final_dbase/assets/studentData.csv";
    private static final String COURSE_CSV_FILE_PATH = "/Users/kristofferneo/Downloads/SSIS-JavaFX-Final/Final_dbase/src/main/java/d/base/final_dbase/assets/courseData.csv";

    // Assuming you have an instance of HelloApplication
    private HelloApplication helloApplication;

    public void setHelloApplication(HelloApplication helloApplication) {
        this.helloApplication = helloApplication;
    }

    @FXML
    private void initialize() {
        yearLevelComboBox.getItems().addAll("1st", "2nd", "3rd", "4th", "5th");
        genderComboBox.getItems().addAll("Male", "Female");

        Course.populateCourseComboBox(courseCodeCombo, COURSE_CSV_FILE_PATH);
        courseCodeCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                courseCodeCombo.getEditor().setText(newValue);
            }
        });

        Course.populateCollegeComboBox(collegeComboBox);

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

        genderComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                String text = newValue.toLowerCase();
                for (int i = 0; i < genderComboBox.getItems().size(); i++) {
                    if (genderComboBox.getItems().get(i).toLowerCase().startsWith(text)) {
                        genderComboBox.getSelectionModel().select(i);
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

        // Set up table views
        setupCourseTableView();
        setupStudentTableView();

        refresh.setOnAction(event -> handleRefreshButtonAction());

        courseRegister.setOnAction(event -> addCourse());
        registerThisStudent.setOnAction(event -> addStudent());


        findStudentIDSearch.setOnAction(event -> handleStudentIDSearchButtonAction());
        findCourseSearch.setOnAction(event -> handleCourseSearchButtonAction());

        backButton.setOnAction(event -> handleBackButtonAction());

        windowSwitch3.setOnAction(event -> {
            windowSwitch3Clicked();
            findStudentIDEdit();
        });

        windowSwitch4.setOnAction(event -> {
            windowSwitch4Clicked();
            findCourseEdit();
        });

    }

    private void setupCourseTableView() {
        tCourseCode.setCellValueFactory(data -> data.getValue().courseCodeProperty());
        tCourseName.setCellValueFactory(data -> data.getValue().courseNameProperty());
        tCollege.setCellValueFactory(data -> data.getValue().collegeProperty());

        // Populate table view with data
        refreshCourseTableView();
    }

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

    private void addStudent() {
        // Get user input for last name
        String sStudentID = studentIDField.getText();

        // Check if student ID is null or empty
        if (sStudentID.trim().isEmpty()) {
            System.out.println("Student ID is empty"); // Debugging statement
            // Display an alert indicating that student ID is required
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Student ID cannot be empty.");
            alert.showAndWait();
            return;
        }

        if (!sStudentID.matches("\\d{4}-\\d{4}")) {
            // Display an alert indicating the error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Student ID must follow the format ####-#### (e.g., 1234-5678)");
            alert.showAndWait();

            clearTextFields();
            return;
        }

        String sLastname = capitalizeFirstLetter(lastNameField.getText());
        String sFirstname = capitalizeFirstLetter(firstNameField.getText());
        String sMiddlename = capitalizeFirstLetter(middleNameField.getText());
        String sYearLevel = yearLevelComboBox.getValue();
        String sSex = genderComboBox.getValue().equals("Male") ? "M" : "F";
        String sCourse = courseCodeCombo.getValue(); // Get the selected course
        String sStatus = sCourse != null ? "ENROLLED" : "NOT ENROLLED"; // Set status based on course selection


        // Get current timestamp
        LocalDateTime sTimestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        String formattedTimestamp2 = sTimestamp.format(formatter);

        try (BufferedReader brS = new BufferedReader(new FileReader(STUDENT_CSV_FILE_PATH))) {
            String line;
            boolean studentExists = false;
            while ((line = brS.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) { // Assuming student ID is at least present
                    String existingStudentID = parts[1].trim();
                    if (existingStudentID.equals(sStudentID)) {
                        studentExists = true;
                        break;
                    }
                }
            }
            if (studentExists) {
                // Display an alert indicating that the student already exists
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("A student with the same ID already exists.");
                alert.showAndWait();

                // You can clear the text fields if needed
                clearTextFields();

            } else {
                // If the student does not exist, add it to the CSV file
                try (BufferedWriter studentInfoWriter = new BufferedWriter(new FileWriter(STUDENT_CSV_FILE_PATH, true))) {
                    // Check if the CSV file already exists
                    if (!Files.exists(Paths.get(STUDENT_CSV_FILE_PATH))) {
                        // If not, write the header
                        studentInfoWriter.write("Registration Time,Student ID,Last Name,First Name,Middle Name,Sex,Year Level,Course,Status");
                        studentInfoWriter.newLine();
                    }

                    // Write the student information along with the timestamp to the CSV file
                    String studentInfo = String.join(",", formattedTimestamp2, sStudentID, sLastname, sFirstname, sMiddlename, sSex, sYearLevel, sCourse, sStatus);
                    studentInfoWriter.write(studentInfo);
                    studentInfoWriter.newLine();
                    System.out.println("Student added: " + sStudentID + " : (" + sCourse + ")" + sLastname + ", " + sFirstname + " " + sMiddlename);

                    // You can clear the text fields if needed
                    clearTextFields();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Course.populateCourseComboBox(courseCodeCombo, COURSE_CSV_FILE_PATH);
        courseCodeCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                courseCodeCombo.getEditor().setText(newValue);
            }
        });

        try {
            studentTable.setItems(CSVHandler.getStudentsAsObservableList(STUDENT_CSV_FILE_PATH));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addCourse() {
        // Get user input for course code and course name
        String courseCode = courseCodeField.getText();
        String courseName = capitalizeFirstLetter(courseNameField.getText());

        // Check if the course code text-field is empty.
        if (courseCode.isEmpty()) {
            // Display an alert indicating that the course code cannot be empty
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Course code cannot be empty.");
            alert.showAndWait();
            return; // Exit the method
        }

        // Get selected college name from ComboBox
        String collegeAssigned = collegeComboBox.getValue() != null ? collegeComboBox.getValue() : "";

        // Get current timestamp
        LocalDateTime timestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = timestamp.format(formatter);

        // Check if the course code already exists in the CSV file
        try (BufferedReader brC = new BufferedReader(new FileReader(COURSE_CSV_FILE_PATH))) {
            String line;
            while ((line = brC.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) { // Assuming course code is at least present
                    String existingCourseCode = parts[1].trim();
                    if (existingCourseCode.equals(courseCode)) {

                        // Display an alert indicating that the course already exists
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("A course with the same code already exists.");
                        alert.showAndWait();
                        clearTextFields();
                        return; // Exit the method
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a new Course object using the user input
        Course newCourse = new Course(formattedTimestamp, courseCode, courseName, collegeAssigned);

        // Write the course information to the CSV file
        try (BufferedWriter courseInfoWriter = new BufferedWriter(new FileWriter(COURSE_CSV_FILE_PATH, true))) {
            // Check if the CSV file already exists
            if (!Files.exists(Paths.get(COURSE_CSV_FILE_PATH))) {
                // If not, write the header
                courseInfoWriter.write("Registration Time,Course Code,Course Name,College");
                courseInfoWriter.newLine();
            }

            // Write the course information to the CSV file
            String courseInfo = String.join(",", newCourse.getTimestamp(), courseCode, courseName, collegeAssigned);
            courseInfoWriter.write(courseInfo);
            courseInfoWriter.newLine();
            System.out.println("Course added: (" + courseCode + ") " + courseName + " - " + collegeAssigned);

            // Clear the text fields
            clearTextFields();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update the combo box with the new course
        Course.populateCourseComboBox(courseCodeCombo, COURSE_CSV_FILE_PATH);
        courseCodeCombo.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                courseCodeCombo.getEditor().setText(newValue);
            }
        });

        try {
            // Update the table view with the new data
            tableView.setItems(CSVHandler.getCoursesAsObservableList(COURSE_CSV_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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


    private void handleBackButtonAction() {
        // Clear the search field
        findCourse.clear();
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

    private void refreshCourseTableView() {
        try {
            ObservableList<Course> courses = CSVHandler.getCoursesAsObservableList(COURSE_CSV_FILE_PATH);
            Platform.runLater(() -> tableView.setItems(courses));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshStudentTableView() {
        try {
            ObservableList<Student> students = CSVHandler.getStudentsAsObservableList(STUDENT_CSV_FILE_PATH);
            Platform.runLater(() -> studentTable.setItems(students));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefreshButtonAction() {
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



























    private void clearTextFields() {
        lastNameField.clear();
        firstNameField.clear();
        middleNameField.clear();
        studentIDField.clear();
        yearLevelComboBox.setValue(null);
        genderComboBox.setValue(null);
        courseCodeCombo.setValue(null);
        courseNameField.clear();
        courseCodeField.clear();
        collegeComboBox.setValue(null);
    }

    @FXML
    private void switchToScene1() {
        helloApplication.switchToScene1();
    }


    private boolean windowSwitch3Clicked;

    @FXML
    private void findStudentIDEdit() {
        String studentID = findStudentID.getText().trim();
        if (studentID.isEmpty()) {
            // Handle the case where the text field is empty (this should not happen due to your existing validation)
            return;
        }

        if (windowSwitch3Clicked) {
            // Open Scene 3 for editing student details
            helloApplication.switchToScene3(studentID);
        } else {
            // Handle the case where the windowSwitch3 button is not clicked
            System.out.println("windowSwitch3 button is not clicked.");
        }
    }

    @FXML
    private void windowSwitch3Clicked() {
        // Set the flag to indicate that the windowSwitch3 button is clicked
        windowSwitch3Clicked = true;
    }















    private boolean windowSwitch4Clicked;

    @FXML
    private void findCourseEdit() {
        String courseCode = findCourse.getText().trim();
        if (courseCode.isEmpty()) {
            // Handle the case where the text field is empty (this should not happen due to your existing validation)
            return;
        }

        if (windowSwitch4Clicked) {
            // Open Scene 4 for editing course details
            helloApplication.switchToScene4(courseCode);
        } else {
            // Display a message indicating that the windowSwitch4 button must be clicked
            System.out.println("Cannot open Scene 4. Please click the windowSwitch4 button.");
        }
    }

    @FXML
    private void windowSwitch4Clicked() {
        // Set the flag to indicate that the windowSwitch4 button is clicked
        windowSwitch4Clicked = true;
    }





}
