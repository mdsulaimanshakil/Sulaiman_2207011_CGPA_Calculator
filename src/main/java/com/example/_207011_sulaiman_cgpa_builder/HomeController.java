package com.example._207011_sulaiman_cgpa_builder;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class HomeController {
    @FXML
    private Button startButton;

    @FXML
    private Text welcomeText;

    @FXML
    private TextField nameInput;

    @FXML
    public void initialize() {
        startButton.setOnAction(e -> {
            try {
                String name = nameInput.getText().trim();

                if (name.isEmpty()) {
                    showAlert("Please enter your name.");
                    return;
                }

                FXMLLoader loader = new FXMLLoader(getClass().getResource("CourseEntry.fxml"));
                Parent root = loader.load();
                CourseEntryController controller = loader.getController();
                controller.setUserName(name);

                Stage stage = (Stage) startButton.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (Exception ex) {
                showAlert("Error loading course entry screen.");
                ex.printStackTrace();
            }
        });
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        alert.showAndWait();
    }
}