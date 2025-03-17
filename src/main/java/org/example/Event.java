package org.example;

public class Event {
    private final int id;
    private final String name;
    private final String description;
    private final int museumCode;

    public Event(int id, String name, String description, int museumCode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.museumCode = museumCode;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMuseumCode() {
        return museumCode;
    }
}
