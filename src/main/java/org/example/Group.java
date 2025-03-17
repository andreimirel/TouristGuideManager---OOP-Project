package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Group {
    private final int museumCode;
    private final String timetable;
    private Professor guide;
    private final List<Person> members;

    // Constructor privat - instantiere prin Factory Method
    private Group(int museumCode, String timetable) {
        this.museumCode = museumCode;
        this.timetable = timetable;
        this.members = new ArrayList<>();
    }

    public int getMuseumCode() {
        return museumCode;
    }

    public String getTimetable() {
        return timetable;
    }

    public Professor getGuide() {
        return guide;
    }

    public void addGuide(Professor guide) {
        if (this.guide != null) {
            throw new IllegalArgumentException("Guide already exists for this group.");
        }
        this.guide = guide;
    }

    public void removeGuide() {
        if (this.guide == null) {
            throw new IllegalArgumentException("No guide exists for this group.");
        }
        this.guide = null;
    }

    public List<Person> getMembers() {
        return new ArrayList<>(members);
    }

    public boolean addMember(Person member) {
        if (members.size() >= 10) {
            throw new IllegalArgumentException("Group cannot have more than 10 members.");
        }
        return members.add(member);
    }

    public boolean removeMember(Person member) {
        return members.removeIf(m -> m.equals(member));
    }

    public Optional<Person> findMember(Person memberToRemove) {
        return members.stream()
                .filter(m -> m.equals(memberToRemove))
                .findFirst();
    }

    @Override
    public String toString() {
        return "Group{" +
                "museumCode=" + museumCode +
                ", timetable='" + timetable + '\'' +
                ", guide=" + guide +
                ", members=" + members +
                '}';
    }

    // Factory pentru a crea grupuri
    public static class Factory {
        public static Group createGroup(int museumCode, String timetable) {
            if (timetable == null || timetable.isEmpty()) {
                throw new IllegalArgumentException("Timetable cannot be null or empty.");
            }
            return new Group(museumCode, timetable);
        }
    }
}
