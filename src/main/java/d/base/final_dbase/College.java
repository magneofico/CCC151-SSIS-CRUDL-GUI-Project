package d.base.final_dbase;

import java.util.List;

public record College(String code, String name) {
    @Override
    public String toString() {
        return "(" + code + ") " + name;
    }



    // Define a static method to initialize the list of colleges
    public static List<College> initializeColleges() {
        return List.of(
                new College("CASS", "College of Arts and Social Sciences"),
                new College("CCS", "College of Computer Studies"),
                new College("CED", "College of Education"),
                new College("CEBAA", "College of Economics–Business Administration–and Accountancy"),
                new College("COE", "College of Engineering"),
                new College("CON", "College of Nursing"),
                new College("CSM", "College of Science and Mathematics")
        );
    }
}
