                                        Student Management System

This JavaFX application is designed to manage student information for a hostel. It allows users to add, search, update, and display student details such as name, registration number, mess category, hostel name, room number, and room type. The application features a user-friendly interface with a dark theme, enhanced by animations and alerts.

Features

- Add Student: Input student details and add them to the list. The application checks for duplicate registration numbers and alerts the user if a duplicate is found.
- Search Student: Search for a student by registration number. If the student is found, their details are displayed in a new window.
- Update Student: Update a student's details after searching for them.
- Show All Students: Display a list of all students added to the system.
- File Storage: Save student information to a text file (`Info.txt`) for future reference.

 Installation Prerequisites

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) 8 or later
- [JavaFX](https://openjfx.io/) SDK

For  running JAVAFX Project Files kindly see this: 👇

https://www.youtube.com/watch?v=ombuBCzClzo

Build and Run

1. Using an IDE (e.g., IntelliJ IDEA, Eclipse):

   - Open the project in your preferred IDE.
   - Ensure that the JavaFX library is configured correctly.
   - Run the `d3` class as a Java application.

2. Using Command Line:

   - Compile the application:  javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml d3.java


   - Run the application:  java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml d3
     

 Usage

- Add Student: Fill in the required fields and click "Add Student". The application will validate the input and add the student to the list.
- Search Student: Click "Search" and enter the registration number to find the student's details.
- Update Student: After finding a student, click "Update" to modify their details.
- Show All Students: Click "Show All Students" to view the list of all students.

Code Structure

- d3.java: The main application class containing the user interface and logic for managing students.
- Student: A static inner class representing a student with fields for name, registration number, mess category, hostel name, room number, and room type.
- DuplicateName: A custom exception class to handle duplicate registration numbers.



