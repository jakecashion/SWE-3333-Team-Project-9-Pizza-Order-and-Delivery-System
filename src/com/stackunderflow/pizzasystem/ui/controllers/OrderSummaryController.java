package com.stackunderflow.pizzasystem.ui.controllers;

import com.stackunderflow.pizzasystem.data.OrderDataManager;
import com.stackunderflow.pizzasystem.model.Pizza;
import java.util.List;
import com.stackunderflow.pizzasystem.model.Cart;
import com.stackunderflow.pizzasystem.model.Ingredient;
import com.stackunderflow.pizzasystem.model.MenuItem;
import com.stackunderflow.pizzasystem.model.Pizza;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;

public class OrderSummaryController {

    @FXML private VBox receiptContainer;
    @FXML private Label subtotalLabel;
    @FXML private Label taxLabel;
    @FXML private Label totalLabel;

    @FXML
    public void initialize() {
        loadCartItems();
        calculateTotals();
    }

    

    private void loadCartItems() {
        Cart cart = Cart.getInstance();
        
        if (cart.getItems().isEmpty()) {
            Label empty = new Label("Your cart is empty.");
            receiptContainer.getChildren().add(empty);
            return;
        }

        for (MenuItem item : cart.getItems()) {
            // 1. Create Main Item Row
            HBox itemRow = new HBox();
            Label nameLbl = new Label(item.getName());
            nameLbl.setStyle("-fx-font-weight: bold;");
            
            // Dynamic Price check (Pizza vs Regular)
            double price = (item instanceof Pizza) ? ((Pizza) item).calculatePrice() : item.getBasePrice();
            Label priceLbl = new Label("$" + String.format("%.2f", price));
            
            // Spacer to push price to the right
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            itemRow.getChildren().addAll(nameLbl, spacer, priceLbl);
            receiptContainer.getChildren().add(itemRow);

            // 2. If Pizza, list the customizations
            if (item instanceof Pizza) {
                Pizza pizza = (Pizza) item;
                VBox detailsBox = new VBox();
                detailsBox.setStyle("-fx-padding: 0 0 0 10; -fx-text-fill: gray;");
                
                Label sizeLbl = new Label("- Size: " + pizza.getSize());
                Label crustLbl = new Label("- Crust: " + pizza.getCrust());
                Label sauceLbl = new Label("- Sauce: " + pizza.getSauce());
                detailsBox.getChildren().addAll(sizeLbl, crustLbl, sauceLbl);

                for (Ingredient topping : pizza.getToppings()) {
                    Label topLbl = new Label("+ " + topping.getName());
                    detailsBox.getChildren().add(topLbl);
                }
                receiptContainer.getChildren().add(detailsBox);
            }
            
            // Add a small separator space
            Region rowSpace = new Region();
            rowSpace.setPrefHeight(10);
            receiptContainer.getChildren().add(rowSpace);
        }
    }

    private void calculateTotals() {
        Cart cart = Cart.getInstance();
        double subtotal = cart.calculateSubtotal();
        double tax = cart.calculateTax();
        double total = cart.calculateGrandTotal();

        subtotalLabel.setText("$" + String.format("%.2f", subtotal));
        taxLabel.setText("$" + String.format("%.2f", tax));
        totalLabel.setText("$" + String.format("%.2f", total));
    }

    @FXML
    private void handlePlaceOrder() {
        Cart cart = Cart.getInstance();
        if (cart.getItems().isEmpty()) return;

        OrderDataManager orderManager = new OrderDataManager();
        
        // 1. Calculate final total
        double total = cart.calculateGrandTotal();
        
        // TODO: Replace '1' with the actual logged-in Customer ID from a UserSession class
        int customerId = 1; 

        // 2. Save the main Order
        int orderId = orderManager.createOrder(customerId, total);
        
        if (orderId != -1) {
            // 3. Save each item in the cart
            for (MenuItem item : cart.getItems()) {
                int orderItemId = orderManager.createOrderItem(orderId, item);
                
                // 4. If it's a Pizza, save the customizations (toppings)
                if (item instanceof Pizza && orderItemId != -1) {
                    Pizza pizza = (Pizza) item;
                    for (Ingredient topping : pizza.getToppings()) {
                        orderManager.createOrderCustomization(orderItemId, topping);
                    }
                }
            }

            // Success UI
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Order Placed");
            alert.setHeaderText("Thank you for your order!");
            alert.setContentText("Your order #" + orderId + " has been sent to the kitchen.");
            alert.showAndWait();

            // Clear Cart and Go Home
            cart.getItems().clear();
            handleBack();
            
        } else {
            // Failure UI
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Order Failed");
            alert.setContentText("There was a problem saving your order to the database.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) receiptContainer.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}