package org.example;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private final Map<String, Command> commands = new HashMap<>();

    public void register(String name, Command command) {
        commands.put(name.toUpperCase(), command);
    }

    public Command getCommand(String name) {
        return commands.get(name.toUpperCase());
    }
}

