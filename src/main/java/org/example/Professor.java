package org.example;

public class Professor extends Person {
    private String school;
    private int experience;

    public Professor(String surname, String name, String role, String school, int experience) {
        super(surname, name, role);
        this.school = school;
        this.experience = experience;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", school=" + school +
                ", experience=" + experience;
    }
}
