package com.stackunderflow.pizzasystem.ui.controllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomepageApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        System.out.println("Attempting to load FXML from: " + getClass().getResource("/com/stackunderflow/pizzasystem/ui/fxml/homepage-View.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/stackunderflow/pizzasystem/ui/fxml/homepage-View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Mom & Pop's Pizzaria");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}