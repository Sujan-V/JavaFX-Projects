import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.FileWriter;
import java.io.IOException;

public class d3 extends Application {
    private ObservableList<Student> studentList;
    public class DuplicateName extends Exception {
        public DuplicateName(String message) {
            super(message);
        }
    }

    private static final String[] ROOM_TYPES = {
            "2 Bed(NA) Apartment-Type",
            "4 Bed(NA) Apartment-Type",
            "2 Bed(NA) Roomtype",
            "4 Bed(NA) Roomtype",
            "2 Bed(A) Apartment-Type",
            "4 Bed(A) Apartment-Type",
            "2 Bed(A) Roomtype",
            "4 Bed(A) Roomtype"
    };

    private static final String[] MESS_CATEGORIES = {
            "Vegetarian Mess",
            "Non-Vegetarian Mess",
            "Special Mess"
    };

    private static final String[] HOSTEL_NAME = {
            "MENS HOSTEL 1",
            "MENS HOSTEL 2",
            "MENS HOSTEL 3",
            "MENS HOSTEL 4",
            "LADIES HOSTEL 1",
            "LADIES HOSTEL 2",
    };

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        studentList = FXCollections.observableArrayList();

        GridPane gridPane = createGridPane();
        addFadeInTransition(gridPane);
        gridPane.setStyle("-fx-background-color: #2D2545;");

        Label nameLabel = new Label("Name:");
        nameLabel.setTextFill(Color.WHITE);
        TextField nameField = new TextField();
        Label regLabel = new Label("Registration Number:");
        regLabel.setTextFill(Color.WHITE);
        TextField regField = new TextField();
        Label messCatLabel = new Label("Mess Category:");
        messCatLabel.setTextFill(Color.WHITE);
        ComboBox<String> messCatComboBox = new ComboBox<>(FXCollections.observableArrayList(MESS_CATEGORIES));
        messCatComboBox.setStyle("-fx-background-color: #424978;");
        Label hostelNameLabel = new Label("Hostel Name:");
        hostelNameLabel.setTextFill(Color.WHITE);
        ComboBox<String> hostelNameComboBox = new ComboBox<>(FXCollections.observableArrayList(HOSTEL_NAME));
        hostelNameComboBox.setStyle("-fx-background-color: #424978;");
        Label roomNumLabel = new Label("Room Number:");
        roomNumLabel.setTextFill(Color.WHITE);
        TextField roomNumField = new TextField();
        Label roomTypeLabel = new Label("Room Type:");
        roomTypeLabel.setTextFill(Color.WHITE);
        ComboBox<String> roomTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(ROOM_TYPES));
        roomTypeComboBox.setStyle("-fx-background-color: #424978;");
        Button addButton = new Button("Add Student");
        addButton.setStyle("-fx-background-color: #7624FF;");
        Button searchButton = new Button("Search");
        searchButton.setStyle("-fx-background-color: #7624FF;");
        Button showAllButton = new Button("Show All Students");
        showAllButton.setStyle("-fx-background-color: #7624FF;");
        Label countLabel = new Label();
        countLabel.setTextFill(Color.WHITE);
        Label searchResultLabel = new Label();

        addButton.setOnAction(e -> {
            try {
                validateInput(
                        nameField.getText(),
                        regField.getText(),
                        messCatComboBox.getValue(),
                        hostelNameComboBox.getValue(),
                        roomNumField.getText(),
                        roomTypeComboBox.getValue());

                String name = nameField.getText().toUpperCase();
                String regNumber = regField.getText().toUpperCase();
                String messCategory = messCatComboBox.getValue();
                String hostelName = hostelNameComboBox.getValue();
                String roomNumber = roomNumField.getText();
                String roomType = roomTypeComboBox.getValue();

                duplicateStudent(regNumber);

                studentList.add(new Student(name, regNumber, messCategory, hostelName, roomNumber, roomType));
                nameField.clear();
                regField.clear();
                messCatComboBox.getSelectionModel().clearSelection();
                hostelNameComboBox.getSelectionModel().clearSelection();
                roomNumField.clear();
                roomTypeComboBox.getSelectionModel().clearSelection();
                countLabel.setText("Number of students: " + studentList.size());

                displayInformationAlert("Success", null, "Student added successfully.");
                saveStudentInfo("Info.txt", name, regNumber, messCategory, hostelName, roomNumber, roomType);
            } catch (IllegalArgumentException ex) {
                displayErrorAlert("Error", null, "Please fill in all student details.");
            } catch (IOException ex) {
                displayErrorAlert("Error", null, "Failed to save student information to file.");
            } catch (DuplicateName ex) {
                displayErrorAlert("Error", null, "Registration Number exists. Please Update instead.");
            }
        });

        searchButton.setOnAction(e -> {
            Stage searchStage = new Stage();
            searchStage.setTitle("Search Student");
            GridPane searchGridPane = createGridPane();
            addFadeInTransition(searchGridPane);
            searchGridPane.setStyle("-fx-background-color: #2D2545;");

            Label regSearchLabel = new Label("Enter Registration Number:");
            regSearchLabel.setTextFill(Color.WHITE);
            TextField regSearchField = new TextField();
            Button searchDetailsButton = new Button("Search");
            searchDetailsButton.setStyle("-fx-background-color: #7624FF;");

            searchDetailsButton.setOnAction(event -> {
                try {
                    validateInput(regSearchField.getText());

                    String regNumber = regSearchField.getText().toUpperCase();
                    Student student = searchStudent(regNumber);

                    if (student != null) {
                        showStudentDetails(student);
                    } else {
                        displayInformationAlert("Search Result", null, "Student not found.");
                    }
                } catch (IllegalArgumentException ex) {
                    displayErrorAlert("Error", null, "Please enter registration number for search.");
                }
            });

            searchGridPane.add(regSearchLabel, 0, 0);
            searchGridPane.add(regSearchField, 1, 0);
            searchGridPane.add(searchDetailsButton, 0, 1, 2, 1);

            Scene searchScene = new Scene(searchGridPane, 400, 100);
            searchStage.setScene(searchScene);
            searchStage.show();
        });

        showAllButton.setOnAction(e -> {
            showAllStudents();
        });

        gridPane.add(nameLabel, 0, 0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(regLabel, 0, 1);
        gridPane.add(regField, 1, 1);
        gridPane.add(messCatLabel, 0, 2);
        gridPane.add(messCatComboBox, 1, 2);
        gridPane.add(hostelNameLabel, 0, 3);
        gridPane.add(hostelNameComboBox, 1, 3);
        gridPane.add(roomNumLabel, 0, 4);
        gridPane.add(roomNumField, 1, 4);
        gridPane.add(roomTypeLabel, 0, 5);
        gridPane.add(roomTypeComboBox, 1, 5);
        gridPane.add(addButton, 0, 6);
        gridPane.add(searchButton, 1, 6);
        gridPane.add(showAllButton, 0, 7);
        gridPane.add(countLabel, 1, 7);
        gridPane.add(searchResultLabel, 0, 9, 2, 1);

        Scene scene = new Scene(gridPane, 410, 340);
        scene.setFill(Color.web("#2D2545"));
        primaryStage.setTitle("Student List");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        return gridPane;
    }

    private void addFadeInTransition(GridPane gridPane) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5), gridPane);
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.play();
    }

    private void validateInput(String... values) {
        for (String value : values) {
            if (value == null || value.isEmpty()) {
                throw new IllegalArgumentException();
            }
        }
    }

    private Student searchStudent(String regNumber) {
        for (Student student : studentList) {
            if (student.getRegNumber().equals(regNumber)) {
                return student;
            }
        }
        return null;
    }

    private Student duplicateStudent(String regNumber) throws DuplicateName {
        for (Student student : studentList) {
            if (student.getRegNumber().equals(regNumber)) {
                throw new DuplicateName("Error");
            }
        }
        return null;
    }

    private void showStudentDetails(Student student) {
        VBox vbox = new VBox();
        vbox.setFillWidth(true);
        vbox.setPrefHeight(100);
        vbox.setStyle("-fx-background-color: #2D2545;");
        vbox.setPadding(new Insets(15));

        Label nameLabel = new Label("Name: " + student.getName());
        nameLabel.setTextFill(Color.WHITE);
        Label regLabel = new Label("Registration Number: " + student.getRegNumber());
        regLabel.setTextFill(Color.WHITE);
        Label messCatLabel = new Label("Mess Category: " + student.getMessCategory());
        messCatLabel.setTextFill(Color.WHITE);
        Label hostelNameLabel = new Label("Hostel Name: " + student.getHostelName());
        hostelNameLabel.setTextFill(Color.WHITE);
        Label roomNumLabel = new Label("Room Number: " + student.getRoomNumber());
        roomNumLabel.setTextFill(Color.WHITE);
        Label roomTypeLabel = new Label("Room Type: " + student.getRoomType());
        roomTypeLabel.setTextFill(Color.WHITE);

        Label emptyLabel = new Label("");

        Button updateButton = new Button("Update");
        updateButton.setStyle("-fx-background-color: #7624FF;");
        updateButton.setTextFill(Color.WHITE);
        updateButton.setOnAction(event -> {
            updateStudents(student);
        });

        vbox.getChildren().addAll(nameLabel, regLabel, messCatLabel, hostelNameLabel, roomNumLabel, roomTypeLabel,
                emptyLabel, updateButton);

        Stage detailsStage = new Stage();
        detailsStage.setTitle("Student Details");
        detailsStage.setScene(new Scene(vbox, 300, 210));
        detailsStage.show();
    }

    private void updateStudents(Student student) {
        Stage updateStage = new Stage();
        updateStage.setTitle("Updating Student Details of: " + student.regNumber);
        GridPane updateGridPane = createGridPane();
        addFadeInTransition(updateGridPane);
        updateGridPane.setStyle("-fx-background-color: #2D2545;");

        Label nameLabel = new Label("Name:");
        nameLabel.setTextFill(Color.WHITE);
        TextField nameField = new TextField();
        Label messCatLabel = new Label("Mess Category:");
        messCatLabel.setTextFill(Color.WHITE);
        ComboBox<String> messCatComboBox = new ComboBox<>(FXCollections.observableArrayList(MESS_CATEGORIES));
        messCatComboBox.setStyle("-fx-background-color: #424978;");
        Label hostelNameLabel = new Label("Hostel Name:");
        hostelNameLabel.setTextFill(Color.WHITE);
        ComboBox<String> hostelNameComboBox = new ComboBox<>(FXCollections.observableArrayList(HOSTEL_NAME));
        hostelNameComboBox.setStyle("-fx-background-color: #424978;");
        Label roomNumLabel = new Label("Room Number:");
        roomNumLabel.setTextFill(Color.WHITE);
        TextField roomNumField = new TextField();
        Label roomTypeLabel = new Label("Room Type:");
        roomTypeLabel.setTextFill(Color.WHITE);
        ComboBox<String> roomTypeComboBox = new ComboBox<>(FXCollections.observableArrayList(ROOM_TYPES));
        roomTypeComboBox.setStyle("-fx-background-color: #424978;");

        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #7624FF;");
        saveButton.setTextFill(Color.WHITE);

        saveButton.setOnAction(e -> {
            String name = nameField.getText();
            String messCat = messCatComboBox.getValue();
            String hostelName = hostelNameComboBox.getValue();
            String roomNum = roomNumField.getText();
            String roomType = roomTypeComboBox.getValue();

            if (name != null) {
                student.setName(name);
                updateStage.close();
            }

            if (messCat != null) {
                student.setMessCategory(messCat);
                updateStage.close();
            }

            if (hostelName != null) {
                student.sethostelName(hostelName);
                updateStage.close();
            }

            if (roomNum != null) {
                student.setroomNumber(roomNum);
                updateStage.close();
            }

            if (roomType != null) {
                student.setRoomType(roomType);
                updateStage.close();
            }
        });

        updateGridPane.add(nameLabel, 0, 1);
        updateGridPane.add(nameField, 1, 1);
        updateGridPane.add(messCatLabel, 0, 2);
        updateGridPane.add(messCatComboBox, 1, 2);
        updateGridPane.add(hostelNameLabel, 0, 3);
        updateGridPane.add(hostelNameComboBox, 1, 3);
        updateGridPane.add(roomNumLabel, 0, 4);
        updateGridPane.add(roomNumField, 1, 4);
        updateGridPane.add(roomTypeLabel, 0, 5);
        updateGridPane.add(roomTypeComboBox, 1, 5);
        updateGridPane.add(saveButton, 0, 6, 2, 1);

        Scene updateScene = new Scene(updateGridPane, 420, 300);
        updateScene.setFill(Color.web("#2D2545"));
        updateStage.setScene(updateScene);
        updateStage.show();
    }

    private void showAllStudents() {
        VBox vbox = new VBox();
        vbox.setPrefWidth(400);
        vbox.setPrefHeight(200);
        vbox.setSpacing(5);
        vbox.setStyle("-fx-background-color: #2D2545;");

        for (Student student : studentList) {
            Label nameLabel = new Label("\n" + "Name: " + student.getName());
            nameLabel.setTextFill(Color.WHITE);
            Label regLabel = new Label("Registration Number: " + student.getRegNumber());
            regLabel.setTextFill(Color.WHITE);
            Label messCatLabel = new Label("Mess Category: " + student.getMessCategory());
            messCatLabel.setTextFill(Color.WHITE);
            Label hostelNameLabel = new Label("Hostel Name: " + student.getHostelName());
            hostelNameLabel.setTextFill(Color.WHITE);
            Label roomNumLabel = new Label("Room Number: " + student.getRoomNumber());
            roomNumLabel.setTextFill(Color.WHITE);
            Label roomTypeLabel = new Label("Room Type: " + student.getRoomType());
            roomTypeLabel.setTextFill(Color.WHITE);

            Label emptyLabel = new Label("");

            vbox.getChildren().addAll(nameLabel, regLabel, messCatLabel, hostelNameLabel, roomNumLabel,
                    roomTypeLabel, emptyLabel);
            vbox.getChildren().add(new Separator());
        }

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPadding(new Insets(15));
        scrollPane.setBackground(new Background(new BackgroundFill(Color.web("#2D2545"), null, null)));

        Scene sscene = new Scene(scrollPane, 400, 300);
        sscene.setFill(Color.web("#2D2545"));

        Stage stage = new Stage();
        stage.setTitle("All Students");
        stage.setScene(sscene);
        stage.show();

    }

    private void displayErrorAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #2D2545;");
        dialogPane.getStyleClass().add("custom-alert");

        Label contentLabel = new Label(contentText);
        contentLabel.setTextFill(Color.WHITE);
        dialogPane.setContent(contentLabel);

        alert.showAndWait();
    }

    private void displayInformationAlert(String title, String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #2D2545;");
        dialogPane.getStyleClass().add("custom-alert");
        Label contentLabel = new Label(contentText);
        contentLabel.setTextFill(Color.WHITE);
        dialogPane.setContent(contentLabel);

        alert.showAndWait();
    }

    private void saveStudentInfo(
            String fileName,
            String name,
            String regNumber,
            String messCategory,
            String hostelName,
            String roomNumber,
            String roomType) throws IOException {
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName, true);
            writer.write("Name: " + name + "\n");
            writer.write("Registration Number: " + regNumber + "\n");
            writer.write("Mess Category: " + messCategory + "\n");
            writer.write("Hostel Name: " + hostelName + "\n");
            writer.write("Room Number: " + roomNumber + "\n");
            writer.write("Room Type: " + roomType + "\n");
            writer.write("--------------------\n");
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private static class Student {
        private String name;
        private String regNumber;
        private String messCategory;
        private String hostelName;
        private String roomNumber;
        private String roomType;

        public Student(
                String name,
                String regNumber,
                String messCategory,
                String hostelName,
                String roomNumber,
                String roomType) {
            this.name = name;
            this.regNumber = regNumber;
            this.messCategory = messCategory;
            this.hostelName = hostelName;
            this.roomNumber = roomNumber;
            this.roomType = roomType;
        }

        public String getName() {
            return name;
        }

        public String getRegNumber() {
            return regNumber;
        }

        public String getMessCategory() {
            return messCategory;
        }

        public String getHostelName() {
            return hostelName;
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        public String getRoomType() {
            return roomType;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setMessCategory(String messCategory) {
            this.messCategory = messCategory;
        }

        public void sethostelName(String hostelName) {
            this.hostelName = hostelName;
        }

        public void setroomNumber(String roomNumber) {
            this.roomNumber = roomNumber;
        }

        public void setRoomType(String roomType) {
            this.roomType = roomType;
        }

    }
}