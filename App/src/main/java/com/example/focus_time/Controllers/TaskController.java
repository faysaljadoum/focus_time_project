package com.example.focus_time.Controllers;

import com.example.focus_time.Models.DBConnection;
import com.example.focus_time.Models.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaskController {

    @FXML
    private TableView<Task> tasksTable;

    @FXML
    private TableColumn<Task, String> taskNameColumn;

    @FXML
    private TableColumn<Task, String> descriptionColumn;

    @FXML
    private TableColumn<Task, Boolean> statusColumn;

    @FXML
    private TableColumn<Task, Void> actionColumn;

    @FXML
    private TextField taskNameField;

    @FXML
    private TextArea taskDescriptionField;

    @FXML
    private Button saveButton;

    private ObservableList<Task> tasksList = FXCollections.observableArrayList();

    private Task taskToEdit;

    private int currentGoalId;  // Variable to store the goal ID for the task

    @FXML
    public void initialize() {
        setupTableColumns();
    }

    // Method to handle Add Task button click
    public void handleAddTaskButtonClick(ActionEvent event) {
        try {
            // Open the Add Task dialog and pass the currentGoalId
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/AddTask.fxml"));
            Parent addTaskPane = loader.load();

            AddTaskController addTaskController = loader.getController();
            addTaskController.setGoalId(currentGoalId);  // Pass the current goal ID

            Stage stage = new Stage();
            stage.setTitle("Add Task");
            stage.setScene(new Scene(addTaskPane));
            stage.show();

        } catch (IOException e) {
            showError("Failed to load Add Task dialog.", e);
        }
    }

    private void setupTableColumns() {
        // Configure task columns
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Configure status column with ComboBox for toggling between statuses
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statutProperty().asObject());
        statusColumn.setCellFactory(column -> new TableCell<>() {
            private final ComboBox<String> comboBox = new ComboBox<>(FXCollections.observableArrayList("In Progress", "Done"));

            {
                comboBox.setOnAction(event -> {
                    Task task = getTableView().getItems().get(getIndex());
                    if (task != null) {
                        boolean newStatus = comboBox.getValue().equals("Done");
                        task.setStatut(newStatus);
                        updateTaskStatus(task);
                    }
                });
            }

            @Override
            protected void updateItem(Boolean status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                } else {
                    comboBox.setValue(status ? "Done" : "In Progress");
                    setGraphic(comboBox);
                }
            }
        });

        // Configure action column for editing and deleting tasks
        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = createButton("/imgs/edit.png", 20, 20, this::handleEdit);
            private final Button deleteButton = createButton("/imgs/delete.png", 20, 20, this::handleDelete);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10, editButton, deleteButton);
                    setGraphic(hbox);
                }
            }

            private void handleEdit(ActionEvent event) {
                Task task = getTableView().getItems().get(getIndex());
                if (task != null) {
                    editTask(task);
                }
            }

            private void handleDelete(ActionEvent event) {
                Task task = getTableView().getItems().get(getIndex());
                if (task != null) {
                    deleteTask(task);
                }
            }
        });
    }

    public void loadTasksForGoal(int goalId) {
        this.currentGoalId = goalId;  // Set the goalId for task-related actions
        tasksList.clear();
        String query = "SELECT * FROM Tache WHERE objectif_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setInt(1, goalId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                tasksList.add(new Task(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("description"),
                        resultSet.getBoolean("statut"),
                        resultSet.getInt("objectif_id")
                ));
            }

        } catch (SQLException e) {
            showError("Failed to load tasks.", e);
        }

        tasksTable.setItems(tasksList);
    }

    private void deleteTask(Task task) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Task");
        alert.setHeaderText("Are you sure you want to delete this task?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String query = "DELETE FROM Tache WHERE id = ?";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(query)) {

                    stmt.setInt(1, task.getId());
                    stmt.executeUpdate();
                    tasksList.remove(task);

                } catch (SQLException e) {
                    showError("Failed to delete task from the database.", e);
                }
            }
        });
    }

    private void updateTaskStatus(Task task) {
        String query = "UPDATE Tache SET statut = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            preparedStatement.setBoolean(1, task.isStatut());
            preparedStatement.setInt(2, task.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            showError("Failed to update task status in the database.", e);
        }
    }

    private void showError(String message, Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    private Button createButton(String iconPath, int width, int height, javafx.event.EventHandler<ActionEvent> handler) {
        Button button = new Button();
        button.setStyle("-fx-background-color: transparent;");
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        icon.setFitHeight(height);
        icon.setFitWidth(width);
        button.setGraphic(icon);
        button.setOnAction(handler);
        return button;
    }

    public void editTask(Task task) {
        try {
            this.taskToEdit = task;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/EditTask.fxml"));
            Parent editTaskPane = loader.load();

            EditTaskController editTaskController = loader.getController();
            editTaskController.setTask(task);

            Stage stage = new Stage();
            stage.setTitle("Edit Task");
            stage.setScene(new Scene(editTaskPane));
            stage.show();
        } catch (IOException e) {
            showError("Failed to load the Edit Task UI.", e);
        }
    }
}
