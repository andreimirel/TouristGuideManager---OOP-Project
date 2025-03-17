# TouristGuideManager
# Birtia Andrei-Mirel  
**Group:** 331CB

## Project Overview
The project represents a system for managing museums, visitor groups, guides, and associated events.  
We can add, remove, or search for guides and members, load museums into the database, and generate customized messages for events. The program works with input files and produces output files that reflect the changes made to the database.

## Input File Reading
- The program retrieves the files for museums, groups, and events.
- These files are processed using commands specified in the file.

## Operations Management
- Each category (MUSEUMS, GROUPS, LISTENER) is handled using the **Command pattern**, which decouples the processing logic from the rest of the application.

## Database Updating
- The created objects (museums, groups, members) are saved in a single database, implemented as a **Singleton**.

## Design Patterns Used
- **Singleton (Database):**
  - Ensures the existence of a single global instance of the database that manages museums, groups, and events.
- **Builder (Museum):**
  - Allows flexible and intuitive creation of museums by configuring properties step by step.
- **Factory (Group):**
  - Uses a static method to create groups with valid data, simplifying the instantiation process.
- **Command (Main):**
  - Decouples the logic of management operations (MUSEUMS, GROUPS, LISTENER) through dedicated commands for each type of action.

These patterns were selected to improve the structure and modularity of the application:
- **Singleton:** Eliminates database redundancy and allows centralized access to the application's data.
- **Builder:** Simplifies the configuration of complex objects such as museums while ensuring data consistency.
- **Factory:** Provides a safe and structured way to create groups, reducing the risk of initialization errors.
- **Command:** Organizes the flow of main operations, facilitating the expansion of the application by adding new types of commands.

## OOP Principles Used
1. **Inheritance:**
   - The `Professor` and `Student` classes inherit from the base class `Person`.
   - This allows using a common type for guides and members, as well as defining behavior specific to each subclass.
2. **Polymorphism:**
   - `Person` is used as a common type for both `Student` and `Professor`.
   - Through polymorphism, a `Person` can be manipulated regardless of whether they are a `Student` or a `Professor`.
3. **Generics:**
   - The use of collections such as `List<Person>` in the `Group` class allows storing different types of people in a safe and flexible manner.
4. **Error Handling:**
   - Special situations are managed using custom exception classes (from `CustomExceptions`), for example, if a guide is already assigned to a group, a specific exception (`GuideExistsException`) is thrown.
   - **Try-catch:** In methods like `handleGroups` and `handleMuseums`, try-catch blocks allow handling of invalid or incomplete data lines from input files.
5. **Encapsulation:**
   - All class attributes are private, and access to them is done through getters and setters.
