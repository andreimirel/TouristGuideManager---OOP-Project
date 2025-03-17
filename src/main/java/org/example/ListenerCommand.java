package org.example;


import java.io.IOException;

public class ListenerCommand implements Command {
    @Override
    public void execute(String[] args) {

        String museumPath = args[1];
        String groupPath = args[2];
        String eventPath = args[3];
        try {
            Main.handleMuseums(museumPath);
            Main.handleGroups(groupPath, groupPath.replace(".in", ".out"));
            Main.handleEvents(eventPath);
        } catch (IOException e) {
            System.err.println("Error processing listener files: " + e.getMessage());
        }
    }
}