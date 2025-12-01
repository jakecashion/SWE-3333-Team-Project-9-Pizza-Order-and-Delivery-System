package com.stackunderflow.pizzasystem.ui.customer;

import com.stackunderflow.pizzasystem.model.MenuItem;
import com.stackunderflow.pizzasystem.data.MenuDataManager;
import com.stackunderflow.pizzasystem.model.Cart;
import com.stackunderflow.pizzasystem.model.Ingredient;
import com.stackunderflow.pizzasystem.model.Pizza;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class CustomPizzaController {

    @FXML private ComboBox<String> sizeCombo;
    @FXML private ComboBox<String> crustCombo;
    @FXML private ComboBox<String> sauceCombo;
    @FXML private VBox toppingsContainer;
    @FXML private Label priceLabel;

    private Pizza currentPizza;
    private List<Ingredient> allIngredients;

    @FXML
    public void initialize() {
        // 1. Initialize a base pizza
        currentPizza = new Pizza(0, "Custom Pizza", 0.0, "Medium", "Thin", "Marinara");

        // 2. Load Ingredients from DB
        MenuDataManager dataManager = new MenuDataManager();
        allIngredients = dataManager.loadToppingsAndCrusts();

        // 3. Populate Dropdowns
        setupDropdowns();

        // 4. Populate Toppings Checkboxes
        setupToppings();
        
        // 5. Initial Price Calc
        updatePrice();
    }

    private void setupDropdowns() {
        // Hardcoding sizes for simplicity, but could be DB driven
        sizeCombo.getItems().addAll("Small", "Medium", "Large", "Extra Large");
        sizeCombo.setValue("Medium"); // Default
        
        // Listen for changes
        sizeCombo.setOnAction(e -> updatePrice());
        
        // Filter Crusts and Sauces from the DB list
        for (Ingredient ing : allIngredients) {
            if ("Crust".equalsIgnoreCase(ing.getUnitType())) {
                crustCombo.getItems().add(ing.getName());
            } else if ("Sauce".equalsIgnoreCase(ing.getUnitType())) {
                sauceCombo.getItems().add(ing.getName());
            }
        }
        crustCombo.getSelectionModel().selectFirst();
        sauceCombo.getSelectionModel().selectFirst();
    }

    private void setupToppings() {
        for (Ingredient ing : allIngredients) {
            if ("Topping".equalsIgnoreCase(ing.getUnitType())) {
                CheckBox cb = new CheckBox(ing.getName() + " (+$" + String.format("%.2f", ing.getExtraCost()) + ")");
                
                // Add listener to update model and price when clicked
                cb.setOnAction(e -> {
                    if (cb.isSelected()) {
                        currentPizza.addTopping(ing);
                    } else {
                        currentPizza.removeTopping(ing);
                    }
                    updatePrice();
                });
                
                toppingsContainer.getChildren().add(cb);
            }
        }
    }

    private void updatePrice() {
        // Set base price based on size
        double basePrice = 0.0;
        switch (sizeCombo.getValue()) {
            case "Small": basePrice = 7.99; break;
            case "Medium": basePrice = 9.99; break;
            case "Large": basePrice = 11.99; break;
            case "Extra Large": basePrice = 13.99; break;
        }
        
        // We cheat a bit here by accessing the protected field or we add a setter in Pizza.java
        // Assuming you added setSize/setBasePrice to Pizza.java
        currentPizza = new Pizza(0, "Custom Pizza", basePrice, sizeCombo.getValue(), crustCombo.getValue(), sauceCombo.getValue());
        
        // Re-add selected toppings
        toppingsContainer.getChildren().filtered(n -> n instanceof CheckBox).forEach(n -> {
            CheckBox cb = (CheckBox) n;
            if (cb.isSelected()) {
                // Find the ingredient object
                String name = cb.getText().split(" \\(")[0];
                allIngredients.stream().filter(i -> i.getName().equals(name)).findFirst().ifPresent(currentPizza::addTopping);
            }
        });

        priceLabel.setText("Price: $" + String.format("%.2f", currentPizza.calculatePrice()));
    }
    public void setBasePizza(MenuItem item) {
        // 1. Create a new Pizza model using the item selected from the menu
        // We default to Medium/Thin/Marinara for now
        this.currentPizza = new Pizza(
            item.getItemId(), 
            item.getName(), 
            item.getBasePrice(), 
            "Medium", 
            "Thin", 
            "Marinara"
        );
        
        // 2. Update the price label immediately so it's not $0.00
        updatePrice();
    }
    @FXML
    private void handleAddToCart() {
        Cart.getInstance().addItem(currentPizza);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Custom Pizza Added!");
        alert.showAndWait();
        
        // Close window
        ((Stage) priceLabel.getScene().getWindow()).close();
    }

    @FXML
    private void handleCancel() {
        ((Stage) priceLabel.getScene().getWindow()).close();
    }
}