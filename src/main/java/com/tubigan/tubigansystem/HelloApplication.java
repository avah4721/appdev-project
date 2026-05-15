package com.tubigan.tubigansystem;

import com.tubigan.tubigansystem.database.DatabaseManager; // Import your new database class
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // --- THE FIX: Initialize the database as soon as the app starts ---
        DatabaseManager.getInstance();
        // ------------------------------------------------------------------

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Tubigan System"); // Updated title
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}