package com.example.focus_time.Controllers;

import com.example.focus_time.Models.Task;
import com.example.focus_time.Models.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditTaskController {

    @FXML
    private TextField taskNameField;
    @FXML
    private TextArea taskDescriptionArea;
    @FXML
    private ComboBox<String> taskStatusComboBox;

    private Task task;  // Store the task to be edited
    private TaskController taskController;  // Field to hold the reference to TaskController

    public void setTaskController(TaskController taskController) {
        this.taskController = taskController;  // Set the TaskController reference
    }


    // This method is called when the FXML is loaded. It's called automatically
    // by the FXMLLoader after the FXML has been loaded and the controller has been initialized.
    public void initialize() {
        if (task != null) {
            // Set the old values in the UI controls
            taskNameField.setText(task.getNom());
            taskDescriptionArea.setText(task.getDescription());

            // Set the combo box value (Done or In Progress)
            // Default to "In Progress" if the task's status is false
            taskStatusComboBox.setValue(task.isStatut() ? "Done" : "In Progress");
        } else {
            // Set default value when no task is loaded
            taskStatusComboBox.setValue("In Progress");
        }
    }


    // This method is called from TaskController to pass the Task to be edited
    public void setTask(Task task) {
        this.task = task;
    }

    // Save the changes made to the task and update the database
    @FXML
    private void saveTask() {
        // Get the new values from the fields
        String name = taskNameField.getText();
        String description = taskDescriptionArea.getText();
        String status = taskStatusComboBox.getValue();

        // Update the task object
        task.setNom(name);
        task.setDescription(description);
        task.setStatut(status.equals("Done"));

        // Update the task in the database
        updateTaskInDatabase(task);

        // Close the edit window
        Stage stage = (Stage) taskNameField.getScene().getWindow();
        stage.close();
    }

    // Cancel the editing and close the window without saving
    @FXML
    private void cancelEdit() {
        Stage stage = (Stage) taskNameField.getScene().getWindow();
        stage.close();
    }

    // Update the task in the database
    private void updateTaskInDatabase(Task task) {
        String sql = "UPDATE Tache SET nom = ?, description = ?, statut = ? WHERE id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, task.getNom());
            stmt.setString(2, task.getDescription());
            stmt.setBoolean(3, task.isStatut());
            stmt.setInt(4, task.getId());

            stmt.executeUpdate();  // Execute the update query
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
