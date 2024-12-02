package com.example.focus_time;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Views/goals.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            stage.setTitle("Focus Time - Dashboard");
            stage.setScene(scene);

            // Add CSS styles
            String stylesheet = getClass().getResource("/styles/dashboard.css").toExternalForm();
            if (stylesheet != null) {
                stage.getScene().getStylesheets().add(stylesheet);
            } else {
                System.err.println("Stylesheet not found: /styles/dashboard.css");
            }

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}