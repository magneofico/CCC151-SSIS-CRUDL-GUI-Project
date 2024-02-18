package d.base.final_dbase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVHandler {

    public static ObservableList<Course> readCoursesFromCSV(String filePath) throws IOException {
        ObservableList<Course> courses = FXCollections.observableArrayList();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue; // Skip the header
                }
                String[] parts = line.split(","); // Split the line by comma
                if (parts.length >= 3) { // Check if the line has the desired number of columns
                    String timestamp = parts[0].trim();
                    String courseCode = parts[1].trim();
                    String courseName = parts[2].trim();
                    courses.add(new Course(timestamp, courseCode, courseName));
                }
            }
        }

        return courses;
    }
}
