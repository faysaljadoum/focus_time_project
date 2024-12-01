package com.example.focus_time.Controllers;

import com.example.focus_time.Classes.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private Label pageTitle;  // Label to show the page title

    @FXML
    public void initialize() {
        // Set the title when this page is loaded
        pageTitle.setText("Dashboard");
    }

    @FXML
    private void goToGoals(ActionEvent event) {
        // Navigate to the Goals page
        SceneManager.switchScene("Goals.fxml");
    }
}
