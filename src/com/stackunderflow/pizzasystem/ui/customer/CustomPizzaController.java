package com.stackunderflow.pizzasystem.ui.customer;

import java.util.Optional;
import com.stackunderflow.pizzasystem.data.MenuDataManager;
import com.stackunderflow.pizzasystem.model.Cart;
import com.stackunderflow.pizzasystem.model.Ingredient;
import com.stackunderflow.pizzasystem.model.MenuItem;
import com.stackunderflow.pizzasystem.model.Pizza;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CustomPizzaController {

    // FXML UI Components
    @FXML private ComboBox<String> sizeCombo;
    @FXML private ComboBox<String> crustCombo;
    @FXML private ComboBox<String> sauceCombo;
    @FXML private VBox toppingsContainer;
    @FXML private Label priceLabel;

    // Model and Data
    private Pizza currentPizza;
    private List<Ingredient> allIngredients;
    private List<Ingredient> selectedToppingObjects = new ArrayList<>(); // To store actual topping objects

    // This method is called by MenuController to start the customization process
    public void setBasePizza(MenuItem item) {
        // We use the base item's price and name, but customize the components
        currentPizza = new Pizza(item.getItemId(), item.getName(), item.getBasePrice(), 
                                 "Medium", "Thin", "Marinara");
        updatePrice();
    }

    @FXML
    public void initialize() {
        // 1. Load data from the database
        MenuDataManager dataManager = new MenuDataManager();
        allIngredients = dataManager.loadToppingsAndCrusts();

        // 2. Populate Dropdowns (Crust, Sauce) and Toppings Checkboxes
        setupDropdowns();
        setupToppings();

        // 3. Attach listeners to update the price whenever a base option changes
        sizeCombo.setOnAction(e -> updatePrice());
        crustCombo.setOnAction(e -> updatePrice());
        sauceCombo.setOnAction(e -> updatePrice());
    }

    private void setupDropdowns() {
        // Sizes (Hardcoded options that influence base price)
        sizeCombo.getItems().addAll("Small", "Medium", "Large", "Extra Large"); // Sizes from Requirements
        sizeCombo.setValue("Medium"); // Default size

        // Crusts (Filter DB results where Unit_Type is 'Crust')
        List<String> crusts = allIngredients.stream()
            .filter(ing -> "Crust".equalsIgnoreCase(ing.getUnitType()))
            .map(Ingredient::getName)
            .collect(Collectors.toList());
        crustCombo.setItems(FXCollections.observableArrayList(crusts));
        crustCombo.getSelectionModel().selectFirst();

        // Sauces (Filter DB results where Unit_Type is 'Sauce')
        List<String> sauces = allIngredients.stream()
            .filter(ing -> "Sauce".equalsIgnoreCase(ing.getUnitType()))
            .map(Ingredient::getName)
            .collect(Collectors.toList());
        sauceCombo.setItems(FXCollections.observableArrayList(sauces));
        sauceCombo.getSelectionModel().selectFirst();
    }

    private void setupToppings() {
        for (Ingredient ing : allIngredients) {
            if ("Topping".equalsIgnoreCase(ing.getUnitType())) {
                // Create checkbox with name and price (e.g., Pepperoni (+$1.50))
                CheckBox cb = new CheckBox(ing.getName() + " (+$" + String.format("%.2f", ing.getExtraCost()) + ")");

                // Attach listener to update model and price
                cb.setOnAction(e -> {
                    if (cb.isSelected()) {
                        selectedToppingObjects.add(ing);
                    } else {
                        selectedToppingObjects.remove(ing);
                    }
                    updatePrice();
                });

                toppingsContainer.getChildren().add(cb);
            }
        }
    }
    
    // Core Business Logic: Recalculates the price every time a selection is made
    private void updatePrice() {
        // 1. Get Base Price based on selected size
        double basePrice = 0.0;
        switch (sizeCombo.getValue()) {
            case "Small": basePrice = 7.99; break;
            case "Medium": basePrice = 9.99; break;
            case "Large": basePrice = 11.99; break;
            case "Extra Large": basePrice = 13.99; break;
        }

        // 2. Update the model with current options
        currentPizza.setBasePrice(basePrice);
        currentPizza.setSize(sizeCombo.getValue());
        currentPizza.setCrust(crustCombo.getValue());
        currentPizza.setSauce(sauceCombo.getValue());

        // 3. Reset toppings in the model and add the currently selected ones
        currentPizza.clearToppings(); // You must add this method to Pizza.java
        selectedToppingObjects.forEach(currentPizza::addTopping);

        // 4. Display final price
        priceLabel.setText("Price: $" + String.format("%.2f", currentPizza.calculatePrice()));
    }

@FXML
    private void handleAddToCart() {
        // 1. CHECK: Has the user selected Pickup or Delivery yet?
        // We check the global Cart instance to see if an Order Type is already set.
        if (Cart.getInstance().getOrderType() == null) {
            ChoiceDialog<String> dialog = new ChoiceDialog<>("Pickup", "Pickup", "Delivery");
            dialog.setTitle("Start Order");
            dialog.setHeaderText("How would you like to receive your order?");
            dialog.setContentText("Choose option:");

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                // Save the choice to the Cart so we don't ask again this session
                Cart.getInstance().setOrderType(result.get());
            } else {
                // User clicked Cancel, so stop the "Add" process and stay on screen
                return; 
            }
        }

        // 2. Proceed to add the pizza to the cart
        Cart.getInstance().addItem(currentPizza);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION, 
                                "Custom Pizza: " + currentPizza.getSize() + " added to order!");
        alert.showAndWait();
        
        // 3. Close the customization window
        ((Stage) priceLabel.getScene().getWindow()).close();
    }

    @FXML
    private void handleCancel() {
        ((Stage) priceLabel.getScene().getWindow()).close();
    }
}