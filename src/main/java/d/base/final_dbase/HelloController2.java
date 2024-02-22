package d.base.final_dbase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;


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
    private ComboBox<College> collegeComboBox;

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
    private TableColumn<Course, String> timestamp;

    @FXML
    private TableColumn<Course, String> tCourseCode;

    @FXML
    private TableColumn<Course, String> tCourseName;

    /* These @FXML are for Student Registration Table. */
    @FXML
    private TableView<Student> studentTable;

    @FXML
    private TableColumn<Student, String> sTimestamp;

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






    private static final String STUDENT_CSV_FILE_PATH = "/Users/kristofferneo/registration_dbase/src/student_registration.csv";
    private static final String COURSE_CSV_FILE_PATH = "/Users/kristofferneo/registration_dbase/src/course_registration.csv";
    private static final int COURSE_COLUMN_INDEX = 2; // Assuming the course data is in the second column(0-based index)

    // Assuming you have an instance of HelloApplication
    private HelloApplication helloApplication;

    public void setHelloApplication(HelloApplication helloApplication) {
        this.helloApplication = helloApplication;
    }

    @FXML
    private void initialize() {
        yearLevelComboBox.getItems().addAll("1st", "2nd", "3rd", "4th", "5th");
        genderComboBox.getItems().addAll("Male", "Female");

        populateCourseComboBox();

        collegeComboBox.getItems().addAll(College.initializeColleges());

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



//        timestamp.setCellValueFactory(data -> data.getValue().timestampProperty());
        tCourseCode.setCellValueFactory(data -> data.getValue().courseCodeProperty());
        tCourseName.setCellValueFactory(data -> data.getValue().courseNameProperty());

        try {
            tableView.setItems(CSVHandler.getCoursesAsObservableList(COURSE_CSV_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

//        sTimestamp.setCellValueFactory(data -> data.getValue().sTimestampProperty());
        sStudentID.setCellValueFactory(data -> data.getValue().sStudentIDProperty());
        sLastname.setCellValueFactory(data -> data.getValue().sLastnameProperty());
        sFirstname.setCellValueFactory(data -> data.getValue().sFirstnameProperty());
        sMiddlename.setCellValueFactory(data -> data.getValue().sMiddlenameProperty());
        sSex.setCellValueFactory(data -> data.getValue().sSexProperty());
        sYearLevel.setCellValueFactory(data -> data.getValue().sYearLevelProperty());
        sCourse.setCellValueFactory(data -> data.getValue().sCourseProperty());

        try {
            studentTable.setItems(CSVHandler.getStudentsAsObservableList(STUDENT_CSV_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        courseRegister.setOnAction(event -> addCourse());
        registerThisStudent.setOnAction(event -> addStudent());


        findStudentIDSearch.setOnAction(event -> handleStudentIDSearchButtonAction());
        findCourseSearch.setOnAction(event -> handleCourseSearchButtonAction());

        backButton.setOnAction(event -> handleBackButtonAction());

        windowSwitch3.setOnAction(event -> findStudentIDEdit());


    }

    private void populateCourseComboBox() {
        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_CSV_FILE_PATH))) {
            String line;
            HashSet<String> courseSet = new HashSet<>();
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue; // Skip the header
                }
                String[] parts = line.split(","); // Split the line by comma
                if (parts.length > COURSE_COLUMN_INDEX) { // Check if the line has the desired column
                    courseSet.add(parts[COURSE_COLUMN_INDEX].trim()); // Add the course data to the set
                }
            }
            ObservableList<String> courseList = FXCollections.observableArrayList(courseSet);
            courseCodeCombo.setItems(courseList);
        } catch (IOException e) {
            e.printStackTrace();
        }


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

        String sLastname = capitalizeFirstLetter(lastNameField.getText());
        String sFirstname = capitalizeFirstLetter(firstNameField.getText());
        String sMiddlename = capitalizeFirstLetter(middleNameField.getText());
        String sYearLevel = yearLevelComboBox.getValue();
        String sSex = genderComboBox.getValue().equals("Male") ? "M" : "F";
        String sCourse = courseCodeCombo.getValue(); // Get the selected course

        // Get current timestamp
        LocalDateTime sTimestamp = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
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
                        studentInfoWriter.write("Registration Time,Student ID,Last Name,First Name,Middle Name,Sex,Year Level,Course");
                        studentInfoWriter.newLine();
                    }

                    // Write the student information along with the timestamp to the CSV file
                    String studentInfo = String.join(",", formattedTimestamp2, sStudentID, sLastname, sFirstname, sMiddlename, sSex, sYearLevel, sCourse);
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

        populateCourseComboBox();

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
        String selectedCollegeName = String.valueOf(collegeComboBox.getValue()); // Retrieve the selected college name as a String

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
        Course newCourse = new Course(formattedTimestamp, courseCode, courseName);

        // Write the course information to the CSV file
        try (BufferedWriter courseInfoWriter = new BufferedWriter(new FileWriter(COURSE_CSV_FILE_PATH, true))) {
            // Check if the CSV file already exists
            if (!Files.exists(Paths.get(COURSE_CSV_FILE_PATH))) {
                // If not, write the header
                courseInfoWriter.write("Registration Time,Course Code,Course Name,College");
                courseInfoWriter.newLine();
            }

            // Write the course information to the CSV file
            String courseInfo = String.join(",", newCourse.getTimestamp(), courseCode, courseName, selectedCollegeName);
            courseInfoWriter.write(courseInfo);
            courseInfoWriter.newLine();
            System.out.println("Course added: (" + courseCode + ") " + courseName + " - " + selectedCollegeName);

            // Clear the text fields
            clearTextFields();

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Update the combo box with the new course
        populateCourseComboBox();

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
        String courseCode = findCourse.getText().trim();
        if (!courseCode.isEmpty()) {
            try {
                // Replace "students.csv" with the actual file path
                Map<String, List<Student>> studentsByCourse = CSVHandler.getStudentsByCourse(STUDENT_CSV_FILE_PATH, courseCode);
                if (studentsByCourse != null) {
                    // Clear previous data in the table
                    studentTable.getItems().clear();

                    // Populate the table with students enrolled in the specified course
                    for (Student student : studentsByCourse.get(courseCode)) {
                        studentTable.getItems().add(student);
                    }
                } else {
                    // Display an alert indicating that no students are enrolled in the specified course
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information");
                    alert.setHeaderText(null);
                    alert.setContentText("No students are enrolled in the specified course.");
                    alert.showAndWait();
                }
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception accordingly
            }
        } else {
            // Display an alert indicating that the course code is empty
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a course code.");
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

    @FXML
    private void findStudentIDEdit() {
        String studentID = findStudentID.getText().trim();
        if (studentID.isEmpty()) {
            // Handle the case where the text field is empty (this should not happen due to your existing validation)
            return;
        }

        // Open Scene 3 for editing student details
        helloApplication.switchToScene3(studentID);
    }
}
