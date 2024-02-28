package d.base.final_dbase;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class Course {
    private final StringProperty timestamp;
    private final StringProperty courseCode;
    private final StringProperty courseName;


    public Course(String timestamp, String courseCode, String courseName) {
        this.timestamp = new SimpleStringProperty(timestamp);
        this.courseCode = new SimpleStringProperty(courseCode);
        this.courseName = new SimpleStringProperty(courseName);
    }

    public static void populateCourseComboBox(ComboBox<String> comboBox, String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            HashSet<String> courseSet = new HashSet<>();
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue; // Skip the header
                }
                String[] parts = line.split(","); // Split the line by comma
                if (parts.length > 0) { // Check if the line has at least one part
                    courseSet.add(parts[2].trim()); // Add the course data to the set
                }
            }
            ObservableList<String> courseList = FXCollections.observableArrayList(courseSet);
            comboBox.setItems(courseList);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    // Property getters
    public StringProperty courseCodeProperty() {
        return courseCode;
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }


}
