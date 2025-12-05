package com.stackunderflow.pizzasystem.ui.customer;

import com.stackunderflow.pizzasystem.model.MenuItem;
import com.stackunderflow.pizzasystem.model.Pizza;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class ReceiptController {

    @FXML private Label orderIdLabel;
    @FXML private Label orderTypeLabel;
    @FXML private VBox itemsContainer;
    @FXML private Label totalLabel;
    
    private int currentOrderId;

    public void setOrderDetails(int orderId, String orderType, double total, List<MenuItem> items) {
        this.currentOrderId = orderId;
        orderIdLabel.setText("Order #" + orderId);
        orderTypeLabel.setText("Order Type: " + (orderType != null ? orderType : "Pickup"));
        totalLabel.setText("Total Paid: $" + String.format("%.2f", total));

        // Populate items list
        for (MenuItem item : items) {
            String displayText = item.getName();
            double price = item.getBasePrice();

            if (item instanceof Pizza) {
                displayText += " (" + ((Pizza) item).getSize() + ")";
                price = ((Pizza) item).calculatePrice();
            }

            Label itemLbl = new Label(displayText + " - $" + String.format("%.2f", price));
            itemLbl.setStyle("-fx-font-size: 14px; -fx-text-fill: #333;");
            itemsContainer.getChildren().add(itemLbl);
        }
    }

    @FXML
    private void handleHome() {
        try {
            Stage stage = (Stage) totalLabel.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/stackunderflow/pizzasystem/ui/fxml/homepage-View.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleTrackOrder() {
        try {
            // Use getClass().getResource() with the EXACT file name
            // Ensure "order-status-view.fxml" is in the same folder as ReceiptController.java
            FXMLLoader loader = new FXMLLoader(getClass().getResource("order-status-view.fxml"));
            Scene scene = new Scene(loader.load());
    
            OrderStatusController controller = loader.getController();
            controller.setOrderId(currentOrderId);
    
            Stage stage = (Stage) totalLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Order Status");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}