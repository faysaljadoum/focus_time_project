package com.example.focus_time.Controllers;

import com.example.focus_time.Models.dao.ApplicationDAOImp;
import com.example.focus_time.Models.dao.UserDAOImp;
import com.example.focus_time.Models.domain.Application;
import com.example.focus_time.Models.domain.User;
import com.example.focus_time.utils.PowerShellExecutor;
import com.example.focus_time.utils.SceneManager;
import com.example.focus_time.utils.SerialNumberUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML
    private AnchorPane parent;  // Label to show the page title

    @FXML
    private Label applicationsCountLabel; // The label to display the number of applications

    private final ApplicationDAOImp applicationDAO;

    public DashboardController() {
        applicationDAO = new ApplicationDAOImp();
    }

    public void initialize() {
        String userSN = SerialNumberUtil.getPCSerialNumber();

        User user = new User(userSN);

        // Pass the connection from the ApplicationDAOImp instance
        int applicationsCount = ApplicationDAOImp.getApplicationsCount(applicationDAO.getConnection(), user);
        applicationsCountLabel.setText(String.valueOf(applicationsCount)); // Set the count in the label
    }
    @FXML
    private void goToApplications(ActionEvent event) {
        try {
            // Load the Applications.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Applications.fxml"));
            Parent applicationsRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(applicationsRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToAppBlock(ActionEvent event) {
        try {
            // Load the Applications.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/BlockedApp.fxml"));
            Parent applicationsRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(applicationsRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToAppLim(ActionEvent event) {
        try {
            // Load the Applications.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/LimitedApp.fxml"));
            Parent applicationsRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(applicationsRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void goToNotf(ActionEvent event) {
        try {
            // Load the Applications.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/Notifications.fxml"));
            Parent applicationsRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(applicationsRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
