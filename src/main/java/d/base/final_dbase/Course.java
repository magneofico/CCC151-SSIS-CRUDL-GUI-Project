package d.base.final_dbase;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Course {
    private final StringProperty timestamp;
    private final StringProperty courseCode;
    private final StringProperty courseName;

    public Course(String timestamp, String courseCode, String courseName) {
        this.timestamp = new SimpleStringProperty(timestamp);
        this.courseCode = new SimpleStringProperty(courseCode);
        this.courseName = new SimpleStringProperty(courseName);
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
    public StringProperty timestampProperty() {
        return timestamp;
    }

    public StringProperty courseCodeProperty() {
        return courseCode;
    }

    public StringProperty courseNameProperty() {
        return courseName;
    }
}
