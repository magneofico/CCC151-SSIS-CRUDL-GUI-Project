package d.base.final_dbase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVHandler {

    private static final String STUDENT_CSV_FILE_PATH = "/Users/kristofferneo/registration_dbase/src/student_registration.csv";
    public static final String COURSE_CSV_FILE_PATH = "/Users/kristofferneo/registration_dbase/src/course_registration.csv";

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
                    String collegeCName = parts[3].trim();
                    courses.add(new Course(timestamp, courseCode, courseName, collegeCName));
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
                if (parts.length >= 9) {
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
                                parts[7].trim(),
                                parts[8].trim()
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
            String sStatus = data[8];
            students.add(new Student(timestamp, sStudentID, sLastname, sFirstname, sMiddlename, sSex, sLevel, sCourse, sStatus));
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
                if (parts.length >= 9) {
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
                        String sStatus = parts[8].trim();
                        return new Student(stimestamp, sStudentID, sLastname, sFirstname, sMiddlename, sSex, sLevel, sCourse, sStatus);
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
                if (parts.length >= 9) {
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
                        String sStatus = parts[8].trim();
                        Student student = new Student(stimestamp, sStudentID, sLastname, sFirstname, sMiddlename, sSex, sLevel, sCourseForStudent, sStatus);

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

    public static Map<String, List<Student>> getStudentsByCourseName(String filePath, String searchInput) throws IOException {
        Map<String, List<Student>> studentsByCourse = new HashMap<>();

        // Convert the search input to lowercase (or uppercase)
        searchInput = searchInput.toLowerCase(); // or searchInput.toUpperCase()

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean headerSkipped = false;
            while ((line = br.readLine()) != null) {
                if (!headerSkipped) {
                    headerSkipped = true;
                    continue; // Skip the header
                }
                String[] parts = line.split(",");
                if (parts.length >= 8) { // Assuming course name is at least present
                    String courseName = parts[7].trim().toLowerCase(); // or uppercase()

                    // Check if the lowercase (or uppercase)
                    // course name contains the lowercase (or uppercase) search input
                    if (courseName.contains(searchInput)) {
                        // Student details are assumed to be in the subsequent columns
                        String timestamp = parts[0].trim();
                        String studentID = parts[1].trim();
                        String lastName = parts[2].trim();
                        String firstName = parts[3].trim();
                        String middleName = parts[4].trim();
                        String gender = parts[5].trim();
                        String yearLevel = parts[6].trim();
                        String courseCode = parts[7].trim();
                        String status = parts[8].trim(); // Assuming status is at index 8

                        // Create a Student object
                        Student student = new Student(timestamp,studentID, lastName, firstName, middleName, gender, yearLevel, courseCode, status);

                        // Add the student to the corresponding list based on the course code
                        List<Student> students = studentsByCourse.getOrDefault(courseCode, new ArrayList<>());
                        students.add(student);
                        studentsByCourse.put(courseCode, students);
                    }
                } else {
                    System.err.println("Invalid line format: " + line); // Print error message for debugging
                }
            }
        }

        return studentsByCourse;
    }


    public static Student fetchStudentByID(String studentID) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(STUDENT_CSV_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) { // Assuming student ID is at least present
                    String currentStudentID = parts[1].trim(); // Assuming student ID is at index 1
                    if (currentStudentID.equals(studentID)) {
                        // Create and return the Student object
                        return new Student(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim(), parts[5].trim(), parts[6].trim(), parts[7].trim(), parts[8].trim());
                    }
                }
            }
        }
        // If no student with the specified ID is found, return null
        return null;
    }

    // Method to update student information in the CSV file
    // To add parameter String updatedCourse
    public static boolean updateStudent(String studentID, String updatedLastName, String updatedFirstName, String updatedMiddleName, String updatedSex, String updatedYearLevel, String updatedCourse) {
        try {
            // Load all students from the CSV file
            List<String> fileLines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(STUDENT_CSV_FILE_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileLines.add(line);
                }
            }

            // Find the student with the provided student ID
            boolean studentFound = false;
            for (int i = 0; i < fileLines.size(); i++) {
                String[] parts = fileLines.get(i).split(",");
                if (parts.length >= 8) {
                    String currentStudentID = parts[1].trim();
                    if (currentStudentID.equals(studentID)) {
                        // Update the student's information
                        parts[2] = updatedLastName;
                        parts[3] = updatedFirstName;
                        parts[4] = updatedMiddleName;
                        parts[5] = updatedSex;
                        parts[6] = updatedYearLevel;
                        parts[7] = updatedCourse;
                        fileLines.set(i, String.join(",", parts));
                        studentFound = true;
                        break;
                    }
                }
            }

            // If the student was found and updated, rewrite the file with the new data
            if (studentFound) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(STUDENT_CSV_FILE_PATH))) {
                    for (String line : fileLines) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
                return true; // Update successful
            } else {
                return false; // Student with provided ID isn't found
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Update failed due to IO error
        }
    }




    // Method to delete student information from the CSV file
    public static boolean deleteStudent(String studentID) throws IOException {
        // Read the student CSV file
        File file = new File(STUDENT_CSV_FILE_PATH);
        File tempFile = new File("temp.csv");
        BufferedReader br = new BufferedReader(new FileReader(file));
        BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));

        boolean found = false;
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length >= 2) { // Assuming student ID is at least present
                String currentStudentID = parts[1].trim(); // Assuming student ID is at index 1
                if (!currentStudentID.equals(studentID)) {
                    // If current student ID does not match the one to delete, write the line to temp file
                    bw.write(line + "\n");
                } else {
                    found = true;
                }
            }
        }

        br.close();
        bw.close();

        // Delete the original file
        if (!file.delete()) {
            System.out.println("Failed to delete the original file.");
            return false;
        }

        // Rename the temp file to the original file name
        if (!tempFile.renameTo(file)) {
            System.out.println("Failed to rename the temp file.");
            return false;
        }

        return found;
    }


    public static Course fetchCourseName(String courseName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(COURSE_CSV_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) { // Assuming student ID is at least present
                    String currentCourseName = parts[2].trim(); // Assuming student ID is at index 1
                    if (currentCourseName.equals(courseName)) {
                        // Create and return the Student object
                        return new Course(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim());
                    }
                }
            }
        }
        // If no student with the specified ID is found, return null
        return null;
    }

    public static boolean updateCourse(String courseName, String updatedCourseCode, String updatedCollegeName) {
        try {
            // Load all courses from the CSV file
            List<String> fileLines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(COURSE_CSV_FILE_PATH))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    fileLines.add(line);
                }
            }

            // Find the course with the provided course name
            boolean courseFound = false;
            for (int i = 0; i < fileLines.size(); i++) {
                String[] parts = fileLines.get(i).split(",");
                if (parts.length >= 4) {
                    String currentCourseName = parts[2].trim();
                    if (currentCourseName.equalsIgnoreCase(courseName)) {
                        // Update the course information
                        parts[1] = updatedCourseCode;
                        parts[3] = updatedCollegeName;
                        fileLines.set(i, String.join(",", parts));
                        courseFound = true;
                        break;
                    }
                }
            }

            // If the course was found and updated, rewrite the file with the new data
            if (courseFound) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(COURSE_CSV_FILE_PATH))) {
                    for (String line : fileLines) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
                return true; // Update successful
            } else {
                return false; // Course with provided name isn't found
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Update failed due to IO error
        }
    }



    public static boolean deleteCourse(String courseName) throws IOException {
        // Path to the course registration CSV file
        String courseFilePath = COURSE_CSV_FILE_PATH;

        try (BufferedReader br = new BufferedReader(new FileReader(courseFilePath))) {
            // Create a temporary file to store the updated course registrations
            File tempFile = new File("temp_course_registration.csv");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            boolean courseFound = false;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3 && parts[2].trim().equalsIgnoreCase(courseName)) {
                    courseFound = true;
                    continue; // Skip writing this line to the temporary file
                }
                writer.write(line);
                writer.newLine();
            }

            writer.close(); // Close the writer

            // Delete the original file
            boolean deleteSuccess = new File(courseFilePath).delete();
            if (!deleteSuccess) {
                System.out.println("Failed to delete original course registration file.");
                return false;
            }

            // Rename the temporary file to the original file name
            boolean renameSuccess = tempFile.renameTo(new File(courseFilePath));
            if (!renameSuccess) {
                System.out.println("Failed to rename temporary file to original file name.");
                return false;
            }

            return courseFound;
        }
    }



    public static boolean deleteStudentRecordsByCourse(String courseFullName) throws IOException {
        String[] parts = courseFullName.split("\\s+\\("); // Split by space followed by (
        if (parts.length >= 2) {
            String courseName = parts[0]; // Extract the course name
            String courseCode = parts[1].replaceAll("[()]", ""); // Remove parentheses
            File file = new File(STUDENT_CSV_FILE_PATH);
            File tempFile = new File("temp.csv");
            try (BufferedReader reader = new BufferedReader(new FileReader(file));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                String line;
                boolean foundCourse = false;
                while ((line = reader.readLine()) != null) {
                    // Check if the line contains the courseFullName and courseCode
                    if (line.contains(courseFullName) && line.contains(courseCode)) {
                        // Modify the line to remove the course
                        line = line.replace(courseFullName, "").trim();
                        line = line.replace(courseCode, "").trim();
                        foundCourse = true;
                    }
                    // Write the modified or unmodified line to the temp file
                    if (!line.isEmpty()) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
                // Check if the course records were found and deleted
                if (foundCourse) {
                    // Rename the temp file to the original file
                    tempFile.renameTo(file);
                    return true;
                } else {
                    // If the course was not found, delete the temp file
                    tempFile.delete();
                    return false;
                }
            }
        } else {
            System.out.println("Invalid course name format: " + courseFullName);
            return false;
        }
    }


}