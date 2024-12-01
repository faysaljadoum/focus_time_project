package com.example.focus_time.Controllers;

import com.example.focus_time.Models.Goal;
import com.example.focus_time.utils.DBConnection;
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
import java.sql.Statement;

public class GoalsController {

    @FXML
    private TableView<Goal> goalsTable;

    @FXML
    private TableColumn<Goal, String> goalColumn;

    @FXML
    private TableColumn<Goal, String> descriptionColumn;

    @FXML
    private TableColumn<Goal, String> startDateColumn;

    @FXML
    private TableColumn<Goal, String> endDateColumn;

    @FXML
    private TableColumn<Goal, Void> actionColumn;

    @FXML
    public void initialize() {
        setupTableColumns();
        populateGoalsTable();
    }

    private void setupTableColumns() {
        goalColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateDebut"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateFin"));

        actionColumn.setCellFactory(column -> new TableCell<>() {
            private final Button editButton = createButton("/imgs/edit.png", 20, 20, this::handleEdit);
            private final Button deleteButton = createButton("/imgs/delete.png", 20, 20, this::handleDelete);
            private final Button viewTasksButton = createStyledButton("View Tasks", "#0078D7", "white", this::handleViewTasks);

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10, editButton, deleteButton, viewTasksButton);
                    setGraphic(hbox);
                }
            }

            private void handleEdit(ActionEvent event) {
                Goal goal = getTableView().getItems().get(getIndex());
                handleEditGoal(goal);
            }

            private void handleDelete(ActionEvent event) {
                Goal goal = getTableView().getItems().get(getIndex());
                handleDeleteGoal(goal);
            }

            private void handleViewTasks(ActionEvent event) {
                Goal goal = getTableView().getItems().get(getIndex());
                handleViewTasksButtonClick(goal);
            }
        });
    }

    private void populateGoalsTable() {
        ObservableList<Goal> goals = fetchGoals();
        goalsTable.setItems(goals);
    }

    private void handleViewTasksButtonClick(Goal goal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/task.fxml"));
            Parent root = loader.load();

            TaskController taskController = loader.getController();
            taskController.loadTasksForGoal(goal.getId());

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Manage Tasks for Goal: " + goal.getNom());
            stage.show();

        } catch (IOException e) {
            showError("Failed to load Task Management page.", e);
        }
    }

    private void handleEditGoal(Goal goal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/EditGoal.fxml"));
            Parent root = loader.load();

            EditGoalController controller = loader.getController();
            controller.initialize(goal);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 600, 500));
            stage.setTitle("Edit Goal");
            stage.show();

        } catch (IOException e) {
            showError("Failed to load EditGoal page.", e);
        }
    }

    @FXML
    private void handleAddGoalButtonClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/AddGoal.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Add New Goal");
        } catch (IOException e) {
            showError("Failed to load AddGoal page.", e);
        }
    }

    private void handleDeleteGoal(Goal goal) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Goal");
        alert.setHeaderText("Are you sure you want to delete this goal?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean isDeleted = deleteGoalFromDatabase(goal);
                if (isDeleted) {
                    goalsTable.getItems().remove(goal);
                }
            }
        });
    }

    private boolean deleteGoalFromDatabase(Goal goal) {
        String query = "DELETE FROM ObjectifProductivite WHERE id = ?";
        try (Connection conn = DBConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, goal.getId());
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            showError("Failed to delete goal from the database.", e);
            return false;
        }
    }

    private ObservableList<Goal> fetchGoals() {
        ObservableList<Goal> goalsList = FXCollections.observableArrayList();
        String query = "SELECT * FROM ObjectifProductivite";

        try (Connection conn = DBConnection.connect();
             Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                goalsList.add(new Goal(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("description"),
                        resultSet.getString("dateDebut"),
                        resultSet.getString("dateFin")
                ));
            }

        } catch (Exception e) {
            showError("Failed to fetch goals from the database.", e);
        }

        return goalsList;
    }

    private void showError(String message, Exception e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.setContentText(e != null ? e.getMessage() : "Unknown error occurred.");
        alert.showAndWait();
    }

    private Button createStyledButton(String text, String bgColor, String textColor, javafx.event.EventHandler<ActionEvent> handler) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + bgColor + "; -fx-text-fill: " + textColor + ";");
        button.setOnAction(handler);
        return button;
    }

    private Button createButton(String iconPath, int width, int height, javafx.event.EventHandler<ActionEvent> handler) {
        Button button = new Button();
        button.setStyle("-fx-background-color: transparent;");
        try {
            Image image = new Image(getClass().getResourceAsStream(iconPath));
            if (image.isError()) throw new NullPointerException("Image not found");
            ImageView icon = new ImageView(image);
            icon.setFitHeight(height);
            icon.setFitWidth(width);
            button.setGraphic(icon);
        } catch (Exception e) {
            System.err.println("Icon not found: " + iconPath);
            button.setText("X");
        }
        button.setOnAction(handler);
        return button;
    }
}
