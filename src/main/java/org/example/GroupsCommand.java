package org.example;

import java.io.IOException;

public class GroupsCommand implements Command {
    @Override
    public void execute(String[] args) {

        String inputPath = args[1];
        String outputPath = inputPath.replace(".in", ".out");
        Main.handleGroups(inputPath, outputPath);
    }
}