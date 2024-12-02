package com.example.focus_time.Controllers;


import com.example.focus_time.utils.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddTaskController {
        @FXML
        private TextField taskNameField;

        @FXML
        private TextArea taskDescriptionField;

        @FXML
        private ComboBox<String> statusComboBox;

        private int goalId;
    private TaskController taskController;  // Field to hold the reference to TaskController

    public void setTaskController(TaskController taskController) {
        this.taskController = taskController;  // Set the TaskController reference
    }

        // Method to set the goal ID
        public void setGoalId(int goalId) {
            this.goalId = goalId;
        }

        // Handle the save button click
        @FXML
        public void handleSaveButtonClick() {
            String taskName = taskNameField.getText();
            String taskDescription = taskDescriptionField.getText();
            String status = statusComboBox.getValue();

            if (taskName.isEmpty() || taskDescription.isEmpty() || status == null) {
                showError("Please fill in all fields.");
                return;
            }

            boolean isDone = status.equals("Done");

            String query = "INSERT INTO Tache (nom, description, statut, objectif_id) VALUES (?, ?, ?, ?)";
            try (Connection conn = DBConnection.connect();
                 PreparedStatement preparedStatement = conn.prepareStatement(query)) {

                preparedStatement.setString(1, taskName);
                preparedStatement.setString(2, taskDescription);
                preparedStatement.setBoolean(3, isDone);
                preparedStatement.setInt(4, goalId);  // Use the passed goal ID

                preparedStatement.executeUpdate();
                showSuccess("Task added successfully!");
                closeWindow();

            } catch (SQLException e) {
                showError("Failed to add task to the database.");
            }
        }

        // Cancel button action
        @FXML
        public void handleCancelButtonClick() {
            closeWindow();
        }

        // Close the current window
        private void closeWindow() {
            Stage stage = (Stage) taskNameField.getScene().getWindow();
            stage.close();
        }

        // Show error alert
        private void showError(String message) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Task Error");
            alert.setContentText(message);
            alert.showAndWait();
        }

        // Show success alert
        private void showSuccess(String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Task Added");
            alert.setContentText(message);
            alert.showAndWait();
        }
    }
