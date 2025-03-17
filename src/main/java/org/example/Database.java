package org.example;

import java.util.*;

public class Database {
    private static volatile Database instance;

    private final Set<Museum> museums;
    private final Set<Group> groups;
    private final List<Event> events;

    private Database() {
        this.museums = new HashSet<>();
        this.groups = new HashSet<>();
        this.events = new ArrayList<>();
    }

    public static Database getInstance() {
        if (instance == null) {
            synchronized (Database.class) {
                if (instance == null) {
                    instance = new Database();
                }
            }
        }
        return instance;
    }

    public void reset() {
        this.museums.clear();
        this.groups.clear();
        this.events.clear();
    }


    public void addMuseum(Museum museum) {
        this.museums.add(museum);
    }

    public Set<Museum> getMuseums() {
        return Collections.unmodifiableSet(museums);
    }

    public Optional<Museum> getMuseum(long museumCode) {
        return museums.stream()
                .filter(m -> m.getCode() == museumCode)
                .findFirst();
    }

    public void addGroup(Group group) {
        this.groups.removeIf(g -> g.getMuseumCode() == group.getMuseumCode() && g.getTimetable().equals(group.getTimetable()));
        this.groups.add(group);
    }

    public Optional<Group> getGroup(int museumCode, String timetable) {
        return groups.stream()
                .filter(group -> group.getMuseumCode() == museumCode && group.getTimetable().equals(timetable))
                .findFirst();
    }

    public Set<Group> getGroups() {
        return Collections.unmodifiableSet(groups);
    }

    public List<Group> getGroupsByMuseumCode(long museumCode) {
        return groups.stream()
                .filter(group -> group.getMuseumCode() == museumCode)
                .toList();
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }
}
