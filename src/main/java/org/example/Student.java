package org.example;

public class Student extends Person {
    private String school;
    private int studyYear;

    public Student(String surname, String name, String role, String school, int studyYear) {
        super(surname, name, role);
        this.school = school;
        this.studyYear = studyYear;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getStudyYear() {
        return studyYear;
    }

    public void setStudyYear(int studyYear) {
        this.studyYear = studyYear;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", school=" + school +
                ", studyYear=" + studyYear;
    }
}
