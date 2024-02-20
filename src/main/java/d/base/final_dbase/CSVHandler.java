package d.base.final_dbase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVHandler {

    public static HashMap<String, String> readCoursesFromCSV(String filePath) throws IOException {
        HashMap<String, String> courseMap = new HashMap<>();

        try (BufferedReader brC = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerSkipped = false;
            while ((line = brC.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue; // Skip the header
                }
                String[] parts = line.split(","); // Split the line by comma
                if (parts.length >= 3) { // Check if the line has the desired number of columns
                    String courseCode = parts[1].trim();
                    String courseName = parts[2].trim();
                    if (courseMap.containsKey(courseCode)) {
                        throw new IllegalArgumentException("Course with code '" + courseCode + "' already registered.");
                    } else {
                        courseMap.put(courseCode, courseName);
                    }
                }
            }
        }

        return courseMap;
    }

    public static ObservableList<Course> getCoursesAsObservableList(String filePath) throws IOException {
        ObservableList<Course> courses = FXCollections.observableArrayList();

        try (BufferedReader brC = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerSkipped = false;
            while ((line = brC.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue; // Skip the header
                }
                String[] parts = line.split(","); // Split the line by comma
                if (parts.length >= 3) { // Check if the line has the desired number of columns
                    String timestamp = parts[0].trim(); // Extract timestamp
                    String courseCode = parts[1].trim();
                    String courseName = parts[2].trim();
                    courses.add(new Course(timestamp, courseCode, courseName));
                }
            }
        }

        return courses;
    }



    /* Performs reading student.csv file and creating a hashmap method where each
     * student information element is linked to a unique studentID number. */
    public static HashMap<String, String[]> readStudentsFromCSV(String filePath) throws IOException {
        HashMap<String, String[]> studentMap = new HashMap<>();

        try (BufferedReader brS = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerSkipped = false;
            while ((line = brS.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    String sStudentID = parts[1].trim();
                    if (!studentMap.containsKey(sStudentID)) {
                        String[] studentData = {
                                parts[0].trim(),
                                parts[1].trim(),
                                parts[2].trim(),
                                parts[3].trim(),
                                parts[4].trim(),
                                parts[5].trim(),
                                parts[6].trim(),
                                parts[7].trim()
                        };
                        studentMap.put(sStudentID, studentData);
                    }
                }
            }
        }

        return studentMap;
    }

    public static ObservableList<Student> getStudentsAsObservableList(String filePath) throws IOException {
        ObservableList<Student> students = FXCollections.observableArrayList();

        HashMap<String, String[]> studentMap = readStudentsFromCSV(filePath);
        for (String studentID : studentMap.keySet()) {
            String[] data = studentMap.get(studentID);
            String timestamp = data[0];
            String sStudentID = data[1];
            String sLastname = data[2];
            String sFirstname = data[3];
            String sMiddlename = data[4];
            String sSex = data[5];
            String sLevel = data[6];
            String sCourse = data[7];
            students.add(new Student(timestamp, sStudentID, sLastname, sFirstname, sMiddlename, sSex, sLevel, sCourse));
        }

        return students;
    }

    public static Student getStudentByID(String studentFilePath, String studentID) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            boolean headerSkipped = false;
            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue; // Skip the header
                }
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    String sStudentID = parts[1].trim();
                    if (sStudentID.equals(studentID)) {
                        // Create a new Student object with the retrieved data
                        String stimestamp = parts[0].trim();
                        String sLastname = parts[2].trim();
                        String sFirstname = parts[3].trim();
                        String sMiddlename = parts[4].trim();
                        String sSex = parts[5].trim();
                        String sLevel = parts[6].trim();
                        String sCourse = parts[7].trim();
                        return new Student(stimestamp, sStudentID, sLastname, sFirstname, sMiddlename, sSex, sLevel, sCourse);
                    }
                }
            }
        }
        // If student ID is not found, return null
        return null;
    }

    public static Map<String, List<Student>> getStudentsByCourse(String studentFilePath, String courseCode) throws IOException {
        Map<String, List<Student>> studentsByCourse = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(studentFilePath))) {
            String line;
            boolean headerSkipped = false;
            while ((line = reader.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue; // Skip the header
                }
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    String sStudentID = parts[1].trim();
                    String sCourse = parts[7].trim();
                    if (sCourse.equals(courseCode)) {
                        // Create a new Student object with the retrieved data
                        String stimestamp = parts[0].trim();
                        String sLastname = parts[2].trim();
                        String sFirstname = parts[3].trim();
                        String sMiddlename = parts[4].trim();
                        String sSex = parts[5].trim();
                        String sLevel = parts[6].trim();
                        String sCourseForStudent = parts[7].trim();
                        Student student = new Student(stimestamp, sStudentID, sLastname, sFirstname, sMiddlename, sSex, sLevel, sCourseForStudent);

                        // Add the student to the list of students for the course
                        if (!studentsByCourse.containsKey(courseCode)) {
                            studentsByCourse.put(courseCode, new ArrayList<>());
                        }
                        studentsByCourse.get(courseCode).add(student);
                    }
                }
            }
        }
        return studentsByCourse;
    }



}

















//
//
//    public static HashMap<String, List<String>> getEnrolledStudentsByCourse(String courseFilePath, String studentFilePath) throws IOException {
//        // Read courses
//        HashMap<String, String> courseMap = readCoursesFromCSV(courseFilePath);
//
//        // Read students
//        HashMap<String, String[]> studentMap = readStudentsFromCSV(studentFilePath);
//
//        // Initialize HashMap to store enrolled students by course
//        HashMap<String, List<String>> enrolledStudentsByCourse = new HashMap<>();
//
//        // Iterate through courses
//        for (String courseCode : courseMap.keySet()) {
//            String courseName = courseMap.get(courseCode);
//            // Initialize list for enrolled students in this course
//            List<String> enrolledStudents = new ArrayList<>();
//            // Iterate through students
//            for (String studentID : studentMap.keySet()) {
//                String[] studentData = studentMap.get(studentID);
//                // Check if the student is enrolled in this course
//                if (studentData[7].equals(courseCode)) { // Assuming course code is at index 7
//                    enrolledStudents.add(studentID);
//                }
//            }
//            // Add enrolled students to the HashMap
//            enrolledStudentsByCourse.put(courseName, enrolledStudents);
//        }
//
//        return enrolledStudentsByCourse;
//    }