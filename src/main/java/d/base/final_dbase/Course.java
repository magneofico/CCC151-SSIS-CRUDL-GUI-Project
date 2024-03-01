package d.base.final_dbase;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Course {
    private final StringProperty timestamp;
    private final StringProperty courseCode;
    private final StringProperty courseName;
    private final StringProperty collegeCName;


    // Declare and initialize the colleges HashMap
    private static final Map<String, String> colleges = new HashMap<>();
    private static final Map<String, String> courses = new HashMap<>();


    static {
        // Initialize the colleges HashMap
        colleges.put("CASS", "College of Arts and Social Sciences");
        colleges.put("CCS", "College of Computer Studies");
        colleges.put("CED", "College of Education");
        colleges.put("CEBAA", "College of Economics–Business Administration–and Accountancy");
        colleges.put("COE", "College of Engineering");
        colleges.put("CON", "College of Nursing");
        colleges.put("CSM", "College of Science and Mathematics");
    }

    public Course(String timestamp, String courseCode, String courseName, String collegeName) {
        this.timestamp = new SimpleStringProperty(timestamp);
        this.courseCode = new SimpleStringProperty(courseCode);
        this.courseName = new SimpleStringProperty(courseName);
        this.collegeCName = new SimpleStringProperty(collegeName);
    }

    public static void populateCourseComboBox(ComboBox<String> comboBox, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue; // Skip the header
                }
                String[] parts = line.split(","); // Split the line by comma
                if (parts.length >= 3) { // Check if the line has at least three parts
                    String courseCode = parts[1].trim();
                    String courseName = parts[2].trim();
                    courses.put(courseCode, courseName); // Add the course data to the map
                } else {
                    System.err.println("Invalid line format: " + line); // Print error message for debugging
                }
            }
            ObservableList<String> courseList = FXCollections.observableArrayList();
            for (Map.Entry<String, String> entry : courses.entrySet()) {
                String formattedCourse = entry.getValue() + " (" + entry.getKey() + ") ";
                courseList.add(formattedCourse);
            }
            comboBox.setItems(courseList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void populateCollegeComboBox(ComboBox<String> comboBox) {
        comboBox.getItems().clear(); // Clear existing items
        for (Map.Entry<String, String> entry : colleges.entrySet()) {
            comboBox.getItems().add("(" + entry.getKey() + ") " + entry.getValue());
        }
    }

    // Method to get the colleges HashMap
    public static Map<String, String> getCollegesHashMap() {
        return colleges;
    }
    // Method to get the courses HashMap
    public static Map<String, String> getCoursesHashMap() {
        return courses;
    }

    // Getters
    public String getTimestamp() {
        return timestamp.get();
    }

    public String getCourseCode() {
        return courseCode.get();
    }

    public String getCourseName() {
        return courseName.get();
    }

    public String getCollegeName() {
        return collegeCName.get();
    }

    // Property getters
    public StringProperty courseCodeProperty() {
        return courseCode;
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }

    public StringProperty collegeProperty() {
        return collegeCName;
    }
}
