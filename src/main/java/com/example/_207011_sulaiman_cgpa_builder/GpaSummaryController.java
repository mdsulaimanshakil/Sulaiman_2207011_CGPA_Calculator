package com.example._207011_sulaiman_cgpa_builder;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.util.List;

public class GpaSummaryController {
    @FXML private TableView<Course> gpaTable;
    @FXML private TableColumn<Course, String> nameCol, codeCol, teacher1Col, teacher2Col, gradeCol;
    @FXML private TableColumn<Course, Integer> creditCol;
    @FXML private Label gpaLabel;
    @FXML private Label certificateNameLabel;
    @FXML private Label targetCreditsLabel;
    @FXML private Button backButton;

    private List<Course> courses;
    private String userName = "Student";
    private int targetCredits = 12;

    public void setCourses(List<Course> courses, String name, int credits) {
        this.courses = courses;
        this.userName = name;
        this.targetCredits = credits;
        populateTable();
        double gpa = calculateGpa();
        gpaLabel.setText(String.format("%.2f", gpa));
        certificateNameLabel.setText(userName);
        targetCreditsLabel.setText(String.valueOf(targetCredits));
    }

    @FXML
    public void initialize() {
        if (backButton != null) {
            backButton.setOnAction(event -> goBack());
        }
    }

    private void populateTable() {
        gpaTable.setItems(FXCollections.observableArrayList(courses));
        nameCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().name()));
        codeCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().code()));
        creditCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().credit()).asObject());
        teacher1Col.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().teacher1()));
        teacher2Col.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().teacher2()));
        gradeCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().grade()));
    }

    private double calculateGpa() {
        if (courses == null || courses.isEmpty()) return 0.0;
        int totalCredits = courses.stream().mapToInt(Course::credit).sum();
        double totalPoints = courses.stream().mapToDouble(c -> c.credit() * c.getGradePoint()).sum();
        return totalCredits == 0 ? 0.0 : totalPoints / totalCredits;
    }

    @FXML
    private void goBack() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseEntry.fxml"));
            Parent root = loader.load();
            stage.setScene(new Scene(root));
        } catch (Exception ex) {
            showAlert("Failed to return to course entry.");
            ex.printStackTrace();
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }

}