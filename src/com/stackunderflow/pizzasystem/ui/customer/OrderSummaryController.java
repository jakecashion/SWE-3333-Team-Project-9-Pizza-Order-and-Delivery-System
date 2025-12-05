package com.stackunderflow.pizzasystem.ui.customer;

import com.stackunderflow.pizzasystem.data.OrderDataManager;
import com.stackunderflow.pizzasystem.model.Cart;
import com.stackunderflow.pizzasystem.model.Ingredient;
import com.stackunderflow.pizzasystem.model.MenuItem;
import com.stackunderflow.pizzasystem.model.Pizza;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderSummaryController {

    @FXML private VBox receiptContainer;
    @FXML private Label subtotalLabel;
    @FXML private Label taxLabel;
    @FXML private Label totalLabel;

    // Payment UI Components
    @FXML private RadioButton cashRadio;
    @FXML private RadioButton creditRadio;
    @FXML private ToggleGroup paymentGroup;
    @FXML private VBox creditCardDetails;
    @FXML private TextField cardNumberField;
    @FXML private TextField expiryField;
    @FXML private TextField cvvField;

    @FXML
    public void initialize() {
        refreshReceipt();
        setupPaymentLogic();
    }

    private void setupPaymentLogic() {
        creditCardDetails.managedProperty().bind(creditCardDetails.visibleProperty());
        paymentGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == creditRadio) {
                creditCardDetails.setVisible(true);
            } else {
                creditCardDetails.setVisible(false);
            }
        });
    }

    private void refreshReceipt() {
        receiptContainer.getChildren().clear();
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

        // --- GROUPING LOGIC ---
        List<CartGroup> groups = new ArrayList<>();
        
        for (MenuItem item : cart.getItems()) {
            boolean found = false;
            for (CartGroup group : groups) {
                if (areItemsEqual(group.item, item)) {
                    group.quantity++;
                    group.instances.add(item);
                    found = true;
                    break;
                }
            }
            if (!found) {
                groups.add(new CartGroup(item));
            }
        }

        for (CartGroup group : groups) {
            MenuItem item = group.item;
            
            HBox itemRow = new HBox(10); 
            itemRow.setAlignment(Pos.CENTER_LEFT);

            // Quantity Buttons
            Button minusBtn = new Button("-");
            minusBtn.setStyle("-fx-min-width: 30px; -fx-background-color: #e0e0e0; -fx-cursor: hand;");
            minusBtn.setOnAction(e -> handleRemoveItem(group.instances.get(0)));

            Button plusBtn = new Button("+");
            plusBtn.setStyle("-fx-min-width: 30px; -fx-background-color: #e0e0e0; -fx-cursor: hand;");
            plusBtn.setOnAction(e -> handleDuplicateItem(item));

            // Name Label (e.g. "Pepperoni Pizza x 2")
            String nameText = item.getName();
            if (group.quantity > 1) {
                nameText += " x " + group.quantity;
            }
            Label nameLbl = new Label(nameText);
            nameLbl.setStyle("-fx-font-weight: bold;");
            
            double unitPrice = (item instanceof Pizza) ? ((Pizza) item).calculatePrice() : item.getBasePrice();
            double groupTotal = unitPrice * group.quantity;
            Label priceLbl = new Label("$" + String.format("%.2f", groupTotal));
            
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            
            itemRow.getChildren().addAll(minusBtn, plusBtn, nameLbl, spacer, priceLbl);
            receiptContainer.getChildren().add(itemRow);

            if (item instanceof Pizza) {
                Pizza pizza = (Pizza) item;
                VBox detailsBox = new VBox();
                detailsBox.setStyle("-fx-padding: 0 0 0 80; -fx-text-fill: gray;");
                
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
            
            Region rowSpace = new Region();
            rowSpace.setPrefHeight(10);
            receiptContainer.getChildren().add(rowSpace);
        }
    }

    private static class CartGroup {
        MenuItem item;
        int quantity;
        List<MenuItem> instances;

        CartGroup(MenuItem item) {
            this.item = item;
            this.quantity = 1;
            this.instances = new ArrayList<>();
            this.instances.add(item);
        }
    }

    private boolean areItemsEqual(MenuItem a, MenuItem b) {
        if (a.getClass() != b.getClass()) return false;
        if (a.getItemId() != b.getItemId()) return false;
        if (!a.getName().equals(b.getName())) return false;

        if (a instanceof Pizza) {
            Pizza pA = (Pizza) a;
            Pizza pB = (Pizza) b;
            
            if (!pA.getSize().equals(pB.getSize())) return false;
            if (!pA.getCrust().equals(pB.getCrust())) return false;
            if (!pA.getSauce().equals(pB.getSauce())) return false;

            Set<Integer> tA = pA.getToppings().stream().map(Ingredient::getIngredientId).collect(Collectors.toSet());
            Set<Integer> tB = pB.getToppings().stream().map(Ingredient::getIngredientId).collect(Collectors.toSet());
            
            return tA.equals(tB);
        }
        return true;
    }

    private void handleDuplicateItem(MenuItem item) {
        if (item instanceof Pizza) {
            Pizza original = (Pizza) item;
            Pizza copy = new Pizza(
                original.getItemId(), 
                original.getName(), 
                original.getBasePrice(), 
                original.getSize(), 
                original.getCrust(), 
                original.getSauce()
            );
            for (Ingredient ing : original.getToppings()) {
                copy.addTopping(ing);
            }
            Cart.getInstance().addItem(copy);
        } else {
            MenuItem copy = new MenuItem(
                item.getItemId(), 
                item.getName(), 
                item.getBasePrice(), 
                item.getCategory()
            );
            Cart.getInstance().addItem(copy);
        }
        refreshReceipt();
    }

    private void handleRemoveItem(MenuItem item) {
        Cart.getInstance().removeItem(item);
        refreshReceipt();
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
        if (cart.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Your cart is empty!");
            alert.showAndWait();
            return;
        }

        if (creditRadio.isSelected()) {
            if (cardNumberField.getText().isEmpty() || expiryField.getText().isEmpty() || cvvField.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all credit card details.");
                alert.showAndWait();
                return;
            }
        }

        OrderDataManager orderManager = new OrderDataManager();
        double total = cart.calculateGrandTotal();
        int customerId = 1; 

        String orderType = cart.getOrderType(); 
        
        // This line requires the updated OrderDataManager from Step 1
        int orderId = orderManager.createOrder(customerId, total, orderType);
        
        if (orderId != -1) {
            for (MenuItem item : cart.getItems()) {
                int orderItemId = orderManager.createOrderItem(orderId, item);
                if (item instanceof Pizza && orderItemId != -1) {
                    Pizza pizza = (Pizza) item;
                    for (Ingredient topping : pizza.getToppings()) {
                        orderManager.createOrderCustomization(orderItemId, topping);
                    }
                }
            }

            try {
                // Navigate to Receipt View
                FXMLLoader loader = new FXMLLoader(getClass().getResource("receipt-view.fxml"));
                Scene scene = new Scene(loader.load());
                
                ReceiptController controller = loader.getController();
                controller.setOrderDetails(orderId, orderType, total, new java.util.ArrayList<>(cart.getItems()));
                
                Stage stage = (Stage) totalLabel.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Receipt");
                
                cart.getItems().clear();
                cart.setOrderType(null);

            } catch (IOException e) {
                e.printStackTrace();
            }
            
        } else {
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