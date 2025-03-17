package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        CommandRegistry registry = new CommandRegistry();
        registry.register("MUSEUMS", new MuseumsCommand());
        registry.register("GROUPS", new GroupsCommand());
        registry.register("LISTENER", new ListenerCommand());

        String commandType = args[0];
        Command command = registry.getCommand(commandType);

        if (command != null) {
            command.execute(args);
        } else {
            System.err.println("Error: Unknown command type: " + commandType);
        }
    }

    static void handleMuseums(String path) throws IOException {

        if (!path.endsWith(".in")) {
            path += ".in";
        }

        File inputFile = new File(path);
        File outputFile = new File(path.replace(".in", ".out"));

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {

            Database db = Database.getInstance();
            String line;

            br.readLine();

            while ((line = br.readLine()) != null) {
                try {
                    String[] parts = line.split("\\|", -1);

                    if (parts.length < 20) {
                        throw new IllegalArgumentException("Data is broken. ## (" + line + ")");
                    }

                    String command = parts[0];
                    if (!"ADD MUSEUM".equals(command)) {
                        throw new IllegalArgumentException("Invalid command: " + command);
                    }

                    long code = Long.parseLong(parts[1]);
                    String name = parts[2];
                    long supervisorCode = Long.parseLong(parts[14]);

                    // Construim locatia
                    Location location = new Location(parts[3], parseInteger(parts[16]));
                    location.setLocality(parts[4]);
                    location.setAdminUnit(parts[5]);
                    location.setAddress(parts[6]);
                    location.setLatitude(parseCoordinate(parts[18]));
                    location.setLongitude(parseCoordinate(parts[19]));

                    // Construim muzeul folosind Builder
                    Museum.MuseumBuilder builder = new Museum.MuseumBuilder(name, code, supervisorCode, location);

                    if (!parts[13].isEmpty()) {
                        builder.setManager(new Person(parts[13], "", "Manager"));
                    }
                    if (!parts[10].isEmpty()) {
                        builder.setFoundingYear(parseInteger(parts[10]));
                    }
                    if (!parts[7].isEmpty()) {
                        builder.setPhoneNumber(parts[7]);
                    }
                    if (!parts[8].isEmpty()) {
                        builder.setFax(parts[8]);
                    }
                    if (!parts[12].isEmpty()) {
                        builder.setEmail(parts[12]);
                    }
                    if (!parts[11].isEmpty()) {
                        builder.setUrl(parts[11]);
                    }
                    if (!parts[15].isEmpty()) {
                        builder.setProfile(parts[15]);
                    }

                    Museum museum = builder.build();

                    // Adaugam muzeul in baza de date si scriem in fisier
                    db.addMuseum(museum);
                    out.println(code + ": " + name);

                } catch (Exception e) {
                    out.println("Exception: Data is broken. ## (" + line + ")");
                    System.err.println("Error processing line: " + line + " -> " + e.getMessage());
                }
            }
        }
    }

    private static Integer parseCoordinate(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return Integer.parseInt(value.replace(",", ""));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid coordinate value: " + value);
        }
    }

    private static Integer parseInteger(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid integer value: " + value);
        }
    }

    static void handleGroups(String inputFilePath, String outputFilePath) {
        if (!inputFilePath.endsWith(".in")) {
            inputFilePath += ".in";
        }
        if (!outputFilePath.endsWith(".out")) {
            outputFilePath += ".out";
        }

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFilePath));
                PrintWriter writer = new PrintWriter(new FileWriter(outputFilePath))
        ) {
            Database db = Database.getInstance();
            db.reset();

            String header = reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = new String[0];
                try {
                    parts = line.split("\\|", -1);

                    String command = parts[0];
                    int museumCode = Integer.parseInt(parts[9]);
                    String timetable = parts[10];
                    String email = parts[5].isEmpty() ? "null" : parts[5];

                    Optional<Group> optionalGroup = db.getGroup(museumCode, timetable);

                    switch (command) {
                        case "ADD GUIDE" -> handleAddGuide(parts, writer, optionalGroup, db, museumCode, timetable);
                        case "REMOVE GUIDE" -> handleRemoveGuide(parts, writer, optionalGroup);
                        case "ADD MEMBER" -> handleAddMember(parts, writer, optionalGroup, db, email, museumCode, timetable);
                        case "REMOVE MEMBER" -> handleRemoveMember(parts, writer, optionalGroup, email);
                        case "FIND GUIDE" -> handleFindGuide(parts, writer, optionalGroup);
                        case "FIND MEMBER" -> handleFindMember(parts, writer, optionalGroup);
                        default -> throw new IllegalArgumentException("Unknown command: " + command);
                    }
                } catch (CustomExceptions.GuideTypeException |
                         CustomExceptions.GuideExistsException |
                         CustomExceptions.GroupNotExistsException |
                         CustomExceptions.PersonNotExistsException |
                         CustomExceptions.GuideNotExistsException |
                         CustomExceptions.GroupThresholdException e) {
                    writer.printf("%d ## %s ## %s%n", Integer.parseInt(parts[9]), parts[10], e.getMessage());
                } catch (Exception e) {
                    writer.printf("%d ## %s ## Exception: %s%n", Integer.parseInt(parts[9]), parts[10], e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error handling groups: " + e.getMessage(), e);
        }
    }

    private static void handleAddMember(String[] parts, PrintWriter writer, Optional<Group> optionalGroup, Database db, String email, int museumCode, String timetable) throws Exception {
        String surname = parts[1], name = parts[2], role = parts[8];

        Group group = optionalGroup.orElseThrow(() -> {
            String extraField = "student".equals(parts[3]) ? "studyYear=" + parts[7] : "experience=" + parts[7];
            return new CustomExceptions.GroupNotExistsException(
                    String.format(
                            "GroupNotExistsException: Group does not exist. ## (new member: surname=%s, name=%s, role=%s, age=%s, email=%s, school=%s, %s)",
                            surname, name, role, parts[4], email, parts[6], extraField
                    )
            );
        });

        if (group.getMembers().size() >= 10) {
            String extraField = "student".equals(parts[3]) ? "studyYear=" + parts[7] : "experience=" + parts[7];
            throw new CustomExceptions.GroupThresholdException(
                    String.format(
                            "GroupThresholdException: Group cannot have more than 10 members. ## (new member: surname=%s, name=%s, role=%s, age=%s, email=%s, school=%s, %s)",
                            surname, name, role, parts[4], email, parts[6], extraField
                    )
            );
        }

        Person member = "student".equals(parts[3])
                ? new Student(surname, name, role, parts[6], Integer.parseInt(parts[7]))
                : new Professor(surname, name, role, parts[6], Integer.parseInt(parts[7]));
        member.setAge(Integer.parseInt(parts[4]));
        member.setEmail(email);

        group.addMember(member);
        db.addGroup(group);

        writer.printf("%d ## %s ## new member: %s%n", museumCode, timetable, member);
    }

    private static void handleRemoveMember(String[] parts, PrintWriter writer, Optional<Group> optionalGroup, String email) throws Exception {
        String surname = parts[1], name = parts[2], role = parts[8];
        int museumCode = Integer.parseInt(parts[9]);
        String timetable = parts[10];

        Group group = optionalGroup.orElseThrow(() -> new CustomExceptions.GroupNotExistsException(
                String.format(
                        "GroupNotExistsException: Group does not exist. ## (removed member: surname=%s, name=%s, role=%s, age=%s, email=%s, school=%s, experience=%s)",
                        surname, name, role, parts[4], email, parts[6], parts[7]
                )
        ));

        Person memberToRemove = new Person(surname, name, role);

        Optional<Person> existingMember = group.findMember(memberToRemove);
        if (existingMember.isPresent()) {
            group.removeMember(existingMember.get());
            writer.printf("%d ## %s ## removed member: %s%n", museumCode, timetable, existingMember.get());
        } else {
            throw new CustomExceptions.PersonNotExistsException(
                    String.format(
                            "PersonNotExistsException: Person was not found in the group. ## (surname=%s, name=%s, role=%s, age=%s, email=%s, school=%s, experience=%s)",
                            surname, name, role, parts[4], email, parts[6], parts[7]
                    )
            );
        }
    }

    private static void handleAddGuide(String[] parts, PrintWriter writer, Optional<Group> optionalGroup, Database db, int museumCode, String timetable) throws Exception {
        String surname = parts[1], name = parts[2], role = parts[8];

        if (!"profesor".equals(parts[3])) {
            throw new CustomExceptions.GuideTypeException(
                    String.format(
                            "GuideTypeException: Guide must be a professor. ## (new guide: surname=%s, name=%s, role=%s, age=%s, email=%s, school=%s, studyYear=%s)",
                            surname, name, role, parts[4], parts[5], parts[6], parts[7]
                    )
            );
        }

        // Folosim Factory pentru a crea grupul
        Group group = optionalGroup.orElseGet(() -> {
            Group newGroup = Group.Factory.createGroup(museumCode, timetable);
            db.addGroup(newGroup);
            return newGroup;
        });

        Professor guide = new Professor(surname, name, role, parts[6], Integer.parseInt(parts[7]));
        guide.setAge(Integer.parseInt(parts[4]));
        guide.setEmail(parts[5]);

        if (group.getGuide() != null) {
            Professor existingGuide = group.getGuide();
            if (existingGuide.equals(guide)) {
                writer.printf("%d ## %s ## GuideExistsException: Guide already exists. ## (new guide: %s)%n", museumCode, timetable, existingGuide);
            } else {
                throw new CustomExceptions.GuideExistsException(
                        String.format(
                                "GuideExistsException: Guide already exists. ## (new guide: surname=%s, name=%s, role=%s, age=%d, email=%s, school=%s, experience=%d)",
                                existingGuide.getSurname(), existingGuide.getName(), existingGuide.getRole(),
                                existingGuide.getAge(), existingGuide.getEmail(), existingGuide.getSchool(), existingGuide.getExperience()
                        )
                );
            }
        } else {
            group.addGuide(guide);
            db.addGroup(group);
            writer.printf("%d ## %s ## new guide: %s%n", museumCode, timetable, guide);
        }
    }

    private static void handleRemoveGuide(String[] parts, PrintWriter writer, Optional<Group> optionalGroup) throws Exception {
        int museumCode = Integer.parseInt(parts[9]);
        String timetable = parts[10];

        Group group = optionalGroup.orElseThrow(() -> new CustomExceptions.GroupNotExistsException(
                String.format(
                        "GroupNotExistsException: Group does not exist. ## (removed guide: timetable=%s, museumCode=%d)", timetable, museumCode
                )
        ));

        Professor currentGuide = group.getGuide();
        if (currentGuide == null) {
            throw new CustomExceptions.GuideNotExistsException(
                    "GuideNotExistsException: No guide found to remove in the group."
            );
        } else {
            group.removeGuide();
            writer.printf("%d ## %s ## removed guide: %s%n", museumCode, timetable, currentGuide);
        }
    }

    private static void handleFindMember(String[] parts, PrintWriter writer, Optional<Group> optionalGroup) throws Exception {
        String surname = parts[1], name = parts[2], role = parts[8];
        int museumCode = Integer.parseInt(parts[9]);
        String timetable = parts[10];
        String email = parts[5] == null || parts[5].isEmpty() ? "null" : parts[5];
        String school = parts[6] == null || parts[6].isEmpty() ? "null" : parts[6];
        String extraField = "student".equals(parts[3]) ? "studyYear=" + parts[7] : "experience=" + parts[7];

        Group group = optionalGroup.orElseThrow(() ->
                new CustomExceptions.GroupNotExistsException(
                        String.format(
                                "GroupNotExistsException: Group does not exist. ## (find member: surname=%s, name=%s, role=%s, age=%s, email=%s, school=%s, %s)",
                                surname, name, role, parts[4], email, school, extraField
                        )
                )
        );

        Person personToFind = new Person(surname, name, role);
        Optional<Person> existingMember = group.findMember(personToFind);

        if (existingMember.isPresent()) {
            writer.printf("%d ## %s ## member found: %s%n", museumCode, timetable, existingMember.get());
        } else {
            writer.printf(
                    "%d ## %s ## member not exists: surname=%s, name=%s, role=%s, age=%s, email=%s, school=%s, %s%n",
                    museumCode, timetable, surname, name, role, parts[4], email, school, extraField
            );
        }
    }

    private static void handleFindGuide(String[] parts, PrintWriter writer, Optional<Group> optionalGroup) throws Exception {
        String surname = parts[1], name = parts[2], role = parts[8];
        int museumCode = Integer.parseInt(parts[9]);
        String timetable = parts[10];
        String email = parts[5] == null || parts[5].isEmpty() ? "null" : parts[5];
        String school = parts[6] == null || parts[6].isEmpty() ? "null" : parts[6];

        if (!"profesor".equals(parts[3])) {
            throw new CustomExceptions.GuideTypeException(
                    String.format(
                            "GuideTypeException: Guide must be a professor. ## (new guide: surname=%s, name=%s, role=%s, age=%s, email=%s, school=%s, experience=%s)",
                            surname, name, role, parts[4], email, school, parts[7]
                    )
            );
        }

        Group group = optionalGroup.orElseThrow(() ->
                new CustomExceptions.GroupNotExistsException(
                        String.format(
                                "GroupNotExistsException: Group does not exist. ## (find guide: surname=%s, name=%s, role=%s, age=%s, email=%s, school=%s, experience=%s)",
                                surname, name, role, parts[4], email, school, parts[7]
                        )
                )
        );

        Professor currentGuide = group.getGuide();
        if (currentGuide != null && currentGuide.getSurname().equals(surname) && currentGuide.getName().equals(name)) {
            writer.printf("%d ## %s ## guide found: %s%n", museumCode, timetable, currentGuide);
        } else {
            writer.printf(
                    "%d ## %s ## guide not exists: surname=%s, name=%s, role=%s, age=%s, email=%s, school=%s, experience=%s%n",
                    museumCode, timetable, surname, name, role, parts[4], email, school, parts[7]
            );
        }
    }


    static void handleEvents(String path) throws IOException {
        if (!path.endsWith(".in")) {
            path += ".in";
        }

        File inputFile = new File(path);
        File outputFile = new File(path.replace(".in", ".out"));

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {

            Database db = Database.getInstance();
            String header = reader.readLine();

            if (header == null || !header.contains("comanda")) {
                throw new IllegalArgumentException("Invalid header in input file.");
            }

            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    String[] parts = line.split("\\|", -1);

                    if (parts.length != 3) {
                        throw new IllegalArgumentException("Invalid event data format.");
                    }

                    String command = parts[0];
                    if (!"ADD EVENT".equals(command)) {
                        throw new IllegalArgumentException("Invalid command: " + command);
                    }

                    long museumCode = Long.parseLong(parts[1]);
                    String message = parts[2];

                    Museum museum = db.getMuseum(museumCode)
                            .orElseThrow(() -> new IllegalArgumentException("Museum not found for code: " + museumCode));

                    List<Group> groups = db.getGroupsByMuseumCode(museumCode);
                    if (groups.isEmpty()) {
                        continue;
                    }

                    for (Group group : groups) {
                        Professor guide = group.getGuide();
                        if (guide != null && guide.getEmail() != null) {
                            writer.printf("To: %s ## Message: %s (%d) %s%n",
                                    guide.getEmail(), museum.getName(), museumCode, message);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error processing line: " + line + " -> " + e.getMessage());
                }
            }
        }
    }
}
