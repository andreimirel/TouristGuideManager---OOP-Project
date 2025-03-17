package org.example;


import java.io.IOException;

public class MuseumsCommand implements Command {
    @Override
    public void execute(String[] args) {

        String path = args[1];
        try {
            Main.handleMuseums(path);
        } catch (IOException e) {
            System.err.println("Error processing museums: " + e.getMessage());
        }
    }
}