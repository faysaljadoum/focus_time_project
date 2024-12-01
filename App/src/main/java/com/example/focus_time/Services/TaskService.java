package com.example.focus_time.Services;

import com.example.focus_time.Models.Task;
import com.example.focus_time.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskService {

    private Connection connection;

    public TaskService() {
        this.connection = DBConnection.connect();
    }

    // Add a new task to the database
    public void addTask(Task task) {
        String query = "INSERT INTO Task (nom, description, statut, objectifId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, task.getNom());
            stmt.setString(2, task.getDescription());
            stmt.setBoolean(3, task.isStatut());
            stmt.setInt(4, task.getObjectifId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update an existing task in the database
    public void updateTask(Task task) {
        String query = "UPDATE Task SET nom = ?, description = ?, statut = ?, objectifId = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, task.getNom());
            stmt.setString(2, task.getDescription());
            stmt.setBoolean(3, task.isStatut());
            stmt.setInt(4, task.getObjectifId());
            stmt.setInt(5, task.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a task by ID from the database
    public void deleteTask(int taskId) {
        String query = "DELETE FROM Task WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, taskId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get tasks by goal ID (fetch all tasks related to a specific goal)
    public List<Task> getTasksForGoal(int goalId) {
        List<Task> taskList = new ArrayList<>();
        String query = "SELECT * FROM Task WHERE objectifId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, goalId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Task task = new Task(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getBoolean("statut"),
                        rs.getInt("objectifId")
                );
                taskList.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return taskList;
    }
}
