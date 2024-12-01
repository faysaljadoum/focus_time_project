package com.example.focus_time.Controllers;

import com.example.focus_time.utils.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddGoalController {

    @FXML
    private TextField goalNameField;

    @FXML
    private TextArea goalDescriptionField;

    @FXML
    private DatePicker startDateField;

    @FXML
    private DatePicker endDateField;

    @FXML
    private VBox tasksContainer;

    @FXML
    private TextField initialTaskField;

    @FXML
    private TextArea initialTaskDescription;

    /**
     * This method is called when the "Add Task" button is clicked.
     * It dynamically creates a new task row with input fields for task name and description.
     */
    @FXML
    private void handleAddTask(ActionEvent event) {
        // Create a new HBox for the task input fields
        HBox taskRow = new HBox();
        taskRow.setSpacing(10.0);

        // Create new TextField for the task name
        TextField taskNameField = new TextField();
        taskNameField.setPromptText("Écrire une tâche...");
        taskNameField.setPrefWidth(350);


        // Create new TextArea for the task description
        TextArea taskDescriptionField = new TextArea();
        taskDescriptionField.setPromptText("Description de la tâche");
        taskDescriptionField.setPrefWidth(350);
        taskDescriptionField.setPrefHeight(100);

        // Add the input fields to the HBox
        taskRow.getChildren().addAll(taskNameField, taskDescriptionField);

        // Add the HBox to the tasks container
        tasksContainer.getChildren().add(taskRow);
    }

    /**
     * This method is called when the "Save Goal" button is clicked.
     * It saves the goal and its associated tasks into the database.
     */
    @FXML
    private void handleSaveGoal(ActionEvent event) {
        String goalName = goalNameField.getText();
        String goalDescription = goalDescriptionField.getText();
        LocalDate startDate = startDateField.getValue();
        LocalDate endDate = endDateField.getValue();

        if (goalName.isEmpty() || goalDescription.isEmpty() || startDate == null || endDate == null) {
            showError("All fields are required!");
            return;
        }

        Connection connection = null;
        try {
            connection = DBConnection.connect();

            // Insert into ObjectifProductivite table
            String insertGoalQuery = "INSERT INTO ObjectifProductivite (nom, description, dateDebut, dateFin, application_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement goalStatement = connection.prepareStatement(insertGoalQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            goalStatement.setString(1, goalName);
            goalStatement.setString(2, goalDescription);
            goalStatement.setObject(3, startDate);
            goalStatement.setObject(4, endDate);
            goalStatement.setInt(5, 1); // Assuming application_id = 1 for now
            goalStatement.executeUpdate();

            // Retrieve the generated ID for the goal
            var generatedKeys = goalStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int goalId = generatedKeys.getInt(1);

                // Insert each task into Tache table
                for (int i = 0; i < tasksContainer.getChildren().size(); i++) {
                    HBox taskRow = (HBox) tasksContainer.getChildren().get(i);
                    TextField taskNameField = (TextField) taskRow.getChildren().get(0);
                    TextArea taskDescriptionField = (TextArea) taskRow.getChildren().get(1);

                    String taskName = taskNameField.getText();
                    String taskDescription = taskDescriptionField.getText();

                    if (!taskName.isEmpty()) {
                        String insertTaskQuery = "INSERT INTO Tache (nom, description, statut, objectif_id) VALUES (?, ?, ?, ?)";
                        PreparedStatement taskStatement = connection.prepareStatement(insertTaskQuery);
                        taskStatement.setString(1, taskName);
                        taskStatement.setString(2, taskDescription);
                        taskStatement.setBoolean(3, false); // Default status is false
                        taskStatement.setInt(4, goalId);
                        taskStatement.executeUpdate();
                    }
                }
            }

            showSuccess("Goal and tasks saved successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Error saving goal: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Method to show an error message in an alert dialog.
     */
    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error occurred");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Method to show a success message in an alert dialog.
     */
    private void showSuccess(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Operation successful");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * This method is called when the "Back" button is clicked.
     * It navigates back to the Goals page.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            // Load the Goals.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Goals.fxml"));
            Parent root = loader.load();

            // Set the scene
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setTitle("Goals");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Failed to load the Goals page.");
        }
    }
}
