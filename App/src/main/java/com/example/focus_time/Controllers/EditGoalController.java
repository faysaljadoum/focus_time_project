package com.example.focus_time.Controllers;

import com.example.focus_time.Models.Goal;
import com.example.focus_time.utils.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditGoalController {

    @FXML
    private TextField goalNameField;

    @FXML
    private TextArea goalDescriptionField;

    @FXML
    private DatePicker startDateField;

    @FXML
    private DatePicker endDateField;

    @FXML
    private VBox tasksContainer; // You can add task editing features here

    private Goal currentGoal;



// Inside your EditGoalController

    public void initialize(Goal goal) {
        this.currentGoal = goal;

        // Populate the fields with the current goal data
        goalNameField.setText(goal.getNom());
        goalDescriptionField.setText(goal.getDescription());

        // Convert String dates to LocalDate (from a String with a time part)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Adjust pattern for datetime
        LocalDateTime startDateTime = LocalDateTime.parse(goal.getDateDebut(), formatter);
        LocalDateTime endDateTime = LocalDateTime.parse(goal.getDateFin(), formatter);

        // Extract the LocalDate part and set to the DatePicker
        startDateField.setValue(startDateTime.toLocalDate());
        endDateField.setValue(endDateTime.toLocalDate());
    }


    @FXML
    private void handleSaveChanges(ActionEvent event) {
        String newName = goalNameField.getText();
        String newDescription = goalDescriptionField.getText();
        String newStartDate = startDateField.getValue().toString();
        String newEndDate = endDateField.getValue().toString();

        // Update the goal in the database
        Connection conn = DBConnection.connect();
        String updateQuery = "UPDATE ObjectifProductivite SET nom = ?, description = ?, dateDebut = ?, dateFin = ? WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setString(1, newName);
            stmt.setString(2, newDescription);
            stmt.setString(3, newStartDate);
            stmt.setString(4, newEndDate);
            stmt.setInt(5, currentGoal.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                // Close the window
                Stage stage = (Stage) goalNameField.getScene().getWindow();
                stage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save changes.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while saving changes.");
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        // Close the window without saving changes
        Stage stage = (Stage) goalNameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleDeleteGoal(ActionEvent event) {
        // Confirm deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Goal");
        alert.setHeaderText("Are you sure you want to delete this goal?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Delete the goal from the database
                Connection conn = DBConnection.connect();
                String deleteQuery = "DELETE FROM ObjectifProductivite WHERE id = ?";
                try {
                    PreparedStatement stmt = conn.prepareStatement(deleteQuery);
                    stmt.setInt(1, currentGoal.getId());

                    int affectedRows = stmt.executeUpdate();
                    if (affectedRows > 0) {
                        // Close the window after deletion
                        Stage stage = (Stage) goalNameField.getScene().getWindow();
                        stage.close();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete goal.");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the goal.");
                }
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}
