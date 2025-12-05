package com.stackunderflow.pizzasystem.ui.customer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import java.io.IOException;

public class OrderStatusController {

    @FXML private Label orderIdLabel;
    @FXML private ProgressBar statusProgress;
    
    @FXML private Label statusReceived;
    @FXML private Label statusOven;
    @FXML private Label statusQuality;
    @FXML private Label statusReady;

    public void setOrderId(int orderId) {
        orderIdLabel.setText("Order #" + orderId);
        // Status is hardcoded in FXML for this prototype, but you could update it here dynamically
    }

    @FXML
    private void handleBackHome() {
        try {
            Stage stage = (Stage) orderIdLabel.getScene().getWindow();
            // Assuming homepage-View.fxml is in the same folder
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("homepage-View.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}