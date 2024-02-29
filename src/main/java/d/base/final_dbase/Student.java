package d.base.final_dbase;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student {
    private final StringProperty sTimestamp;
    private final StringProperty sStudentID;
    private final StringProperty sLastname;
    private final StringProperty sFirstname;
    private final StringProperty sMiddlename;
    private final StringProperty sSex;
    private final StringProperty sYearLevel;
    private final StringProperty sCourse;
    private final StringProperty sStatus;

    public Student(String sTimestamp, String sStudentID, String sLastname, String sFirstname, String sMiddlename, String sSex, String sYearLevel, String sCourse, String sStatus) {
        this.sTimestamp = new SimpleStringProperty(sTimestamp);
        this.sStudentID = new SimpleStringProperty(sStudentID);
        this.sLastname= new SimpleStringProperty(sLastname);
        this.sFirstname= new SimpleStringProperty(sFirstname);
        this.sMiddlename= new SimpleStringProperty(sMiddlename);
        this.sSex= new SimpleStringProperty(sSex);
        this.sYearLevel= new SimpleStringProperty(sYearLevel);
        this.sCourse= new SimpleStringProperty(sCourse);
//        this.sStatus = new SimpleStringProperty(sStatus);
        this.sStatus = determineStatus(sCourse);

    }

    public String getsCourse() {
        return sCourse.get();
    }

    public String getsTimestamp() {
        return sTimestamp.get();
    }

    public String getsStudentID() {
        return sStudentID.get();
    }

    public String getsLastname() {
        return sLastname.get();
    }

    public String getsFirstname() {
        return sFirstname.get();
    }

    public String getsMiddlename() {
        return sMiddlename.get();
    }

    public String getsSex() {
        return sSex.get();
    }

    public String getsYearLevel() {
        return sYearLevel.get();
    }

    public String getsStatus() {
        return sStatus.get();
    }




    public StringProperty sTimestampProperty() {
        return sTimestamp;
    }

    public StringProperty sStudentIDProperty() {
        return sStudentID;
    }

    public StringProperty sLastnameProperty() {
        return sLastname;
    }

    public StringProperty sFirstnameProperty() {
        return sFirstname;
    }

    public StringProperty sMiddlenameProperty() {
        return sMiddlename;
    }

    public StringProperty sSexProperty() {
        return sSex;
    }

    public StringProperty sYearLevelProperty() {
        return sYearLevel;
    }

    public StringProperty sCourseProperty() {
        return sCourse;
    }
    public StringProperty sStatusProperty() {
        return sStatus;
    }

    private StringProperty determineStatus(String sCourse) {
        if (sCourse == null || sCourse.isEmpty()) {
            return new SimpleStringProperty("NOT ENROLLED");
        } else {
            return new SimpleStringProperty("ENROLLED");
        }
    }



}
