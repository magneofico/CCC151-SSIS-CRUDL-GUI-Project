//package d.base.final_dbase;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Scanner;
//
//public class StudentHashmap {
//    private HashMap<String, String> studentData;
//
//    // Constructor
//    public StudentHashmap() {
//        this.studentData = new HashMap<>();
//    }
//
//    // Setter for studentData
//    public void setStudentData(HashMap<String, String> studentData) {
//        this.studentData = studentData;
//    }
//
//    // Getter for studentData
//    public HashMap<String, String> getStudentData() {
//        return studentData;
//    }
//
//    // Method to read data from CSV file and populate studentData
//    public void readDataFromCSV(String csvFilePath) {
//        String line;
//        String csvSplitBy = ",";
//
//        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
//            // Skip the header if present
//            br.readLine();
//
//            // Read each line of the CSV file
//            while ((line = br.readLine()) != null) {
//                // Split the line by comma
//                String[] data = line.split(csvSplitBy);
//
//                // Assuming data length is appropriate
//                if (data.length >= 8) {
//                    // Construct the value string by concatenating columns three to eight.
//                    StringBuilder valueBuilder = new StringBuilder();
//                    for (int i = 2; i < 8; i++) {
//                        valueBuilder.append(data[i]);
//                        // Add a delimiter if not the last column
//                        if (i < 7)
//                            valueBuilder.append(",");
//                    }
//                    String value = valueBuilder.toString();
//
//                    // Add the pair to the HashMap
//                    studentData.put(data[1], value);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void main(String[] args) {
//        // Create an instance of StudentRegistration
//        StudentHashmap registration = new StudentHashmap();
//
//        // Path to the CSV file
//        String csvFile = "/Users/kristofferneo/registration_dbase/src/student_registration.csv";
//
//        // Read data from CSV and populate studentData
//        registration.readDataFromCSV(csvFile);
//
//        // Prompt user for input
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter Student ID to search: ");
//        String searchId = scanner.nextLine();
//
//        // Search for the student ID in the HashMap
//        String searchData = registration.getStudentData().get(searchId);
//
//        // Display the result
//        if (searchData != null) {
//            System.out.println("\nStudent ID: " + searchId + "\nStudent Data: " + searchData);
//        } else {
//            System.out.println("Student ID not found.");
//        }
//
//        scanner.close();
//    }
//}
//
////
////        // Print the HashMap
////        for (String key : studentData.keySet()) {
////            System.out.println("Student ID: " + key + ", Student: " + studentData.get(key));
////        }
//
