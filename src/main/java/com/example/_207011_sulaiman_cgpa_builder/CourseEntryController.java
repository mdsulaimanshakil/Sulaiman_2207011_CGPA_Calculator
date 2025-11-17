package com.example._207011_sulaiman_cgpa_builder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.util.List;

public class CourseEntryController {
    @FXML private TextField nameField, codeField, creditField, teacher1Field, teacher2Field;
    @FXML private TextField targetCreditsField;
    @FXML private ComboBox<String> gradeBox;
    @FXML private Button addButton, resetButton, calculateButton, setCreditsButton;
    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, String> nameCol, codeCol, teacher1Col, teacher2Col, gradeCol;
    @FXML private TableColumn<Course, Integer> creditCol;
    @FXML private Label targetCreditsLabel, takenCreditsLabel, remainingCreditsLabel, progressPercentageLabel;
    @FXML private ProgressBar creditProgressBar;

    private final ObservableList<Course> courses = FXCollections.observableArrayList();
    private static final int DEFAULT_CREDITS = 12;

    private String userName = "Student";
    private int targetCredits = DEFAULT_CREDITS;
    private boolean creditsSet = false;

    @FXML
    public void initialize() {
        gradeBox.setItems(FXCollections.observableArrayList("A+","A","A-","B+","B","B-","C+","C","F"));
        courseTable.setItems(courses);

        nameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().name()));
        codeCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().code()));
        creditCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().credit()).asObject());
        teacher1Col.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().teacher1()));
        teacher2Col.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().teacher2()));
        gradeCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().grade()));

        courses.addListener((javafx.collections.ListChangeListener<Course>) c -> checkCredits());

        addButton.setOnAction(e -> addCourse());
        resetButton.setOnAction(e -> resetCourses());
        calculateButton.setOnAction(e -> showGpaSummary());
        setCreditsButton.setOnAction(e -> setTargetCredits());


        addButton.setDisable(true);
        calculateButton.setDisable(true);
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    @FXML
    private void setTargetCredits() {
        String creditsStr = targetCreditsField.getText().trim();

        if (creditsStr.isEmpty()) {
            showAlert("Please enter target credits.");
            return;
        }

        try {
            int credits = Integer.parseInt(creditsStr);
            if (credits <= 0) {
                showAlert("Target credits must be greater than 0.");
                return;
            }

            this.targetCredits = credits;
            this.creditsSet = true;
            addButton.setDisable(false);
            targetCreditsField.setDisable(true);
            setCreditsButton.setDisable(true);

            targetCreditsLabel.setText(String.valueOf(credits));
            updateProgressDisplay();

            showAlert("Target credits set to " + credits + ". You can now add courses.");
            checkCredits();

        } catch (NumberFormatException ex) {
            showAlert("Target credits must be a valid number.");
        }
    }

    @FXML
    private void addCourse() {
        try {
            String name = nameField.getText().trim(), code = codeField.getText().trim();
            int credit = Integer.parseInt(creditField.getText().trim());
            String teacher1 = teacher1Field.getText().trim(), teacher2 = teacher2Field.getText().trim();
            String grade = gradeBox.getValue();
            if (name.isEmpty() || code.isEmpty() || grade == null) {
                showAlert("Please fill all required fields.");
                return;
            }
            courses.add(new Course(name, code, credit, teacher1, teacher2, grade));
            clearFields();
            showAlert("Course added successfully!");
            checkCredits();
        } catch (NumberFormatException ex) {
            showAlert("Course credit must be a number.");
        }
    }

    @FXML
    private void clearFields() {
        nameField.clear();
        codeField.clear();
        creditField.clear();
        teacher1Field.clear();
        teacher2Field.clear();
        gradeBox.setValue(null);
    }

    @FXML
    private void resetCourses() {
        courses.clear();
        clearFields();
        targetCreditsField.clear();
        targetCreditsField.setDisable(false);
        setCreditsButton.setDisable(false);
        addButton.setDisable(true);
        calculateButton.setDisable(true);
        creditsSet = false;
        targetCredits = DEFAULT_CREDITS;
        targetCreditsLabel.setText("--");
        takenCreditsLabel.setText("0");
        remainingCreditsLabel.setText("--");
        progressPercentageLabel.setText("0%");
        creditProgressBar.setProgress(0);

        checkCredits();
    }

    private void checkCredits() {
        int totalCredits = courses.stream().mapToInt(Course::credit).sum();
        calculateButton.setDisable(totalCredits < targetCredits);
        updateProgressDisplay();
    }

    private void updateProgressDisplay() {
        int totalCredits = courses.stream().mapToInt(Course::credit).sum();
        int remaining = targetCredits - totalCredits;

        takenCreditsLabel.setText(String.valueOf(totalCredits));
        remainingCreditsLabel.setText(remaining > 0 ? String.valueOf(remaining) : "0");

        double progress = targetCredits > 0 ? (double) totalCredits / targetCredits : 0;
        progress = Math.min(progress, 1.0);
        creditProgressBar.setProgress(progress);

        int percentage = (int) (progress * 100);
        progressPercentageLabel.setText(percentage + "%");
    }

    @FXML
    private void showGpaSummary() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("GpaSummary.fxml"));
            Parent root = loader.load();
            GpaSummaryController summaryController = loader.getController();
            summaryController.setCourses(List.copyOf(courses), userName, targetCredits);
            Stage stage = (Stage) calculateButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception ex) {
            showAlert("Failed to switch to GPA Summary view.");
            ex.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }

}