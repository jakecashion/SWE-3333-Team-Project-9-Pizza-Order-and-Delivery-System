package com.stackunderflow.pizzasystem.ui.customer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class homepageApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(homepageApplication.class.getResource("homepage-View.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Mom & Pop's Pizzaria");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}