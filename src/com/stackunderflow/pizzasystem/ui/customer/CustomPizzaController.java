package com.stackunderflow.pizzasystem.ui.customer;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for the Pizza Customization View.
 */
public class CustomPizzaController {

    @FXML private ComboBox<String> sizeCombo;
    @FXML private ComboBox<String> crustCombo;
    @FXML private ComboBox<String> sauceCombo;
    @FXML private VBox toppingsContainer;
    @FXML private Label priceLabel;

    private Pizza currentPizza;
    private List<Ingredient> allIngredients;
    private List<Ingredient> selectedToppingObjects = new ArrayList<>(); 
    
    // --- FIX: Store the extra cost of this specialty pizza ---
    private double basePriceOffset = 0.0;

    /**
     * Initializes the customization screen with a base pizza item.
     */
    public void setBasePizza(MenuItem item) {
        // 1. Calculate the "Premium" markup
        // Standard Medium Cheese is $9.99. If this item is $11.99, the offset is $2.00.
        this.basePriceOffset = item.getBasePrice() - 9.99;
        
        // Safety check to prevent negative numbers
        if (this.basePriceOffset < 0) this.basePriceOffset = 0;

        // 2. Create the pizza object
        currentPizza = new Pizza(item.getItemId(), item.getName(), item.getBasePrice(), 
                                 "Medium", "Thin", "Marinara");
        
        // 3. Force a price update immediately so it shows $11.99, not $9.99
        if (sizeCombo.getValue() != null) {
            updatePrice();
        }
    }

    @FXML
    public void initialize() {
        MenuDataManager dataManager = new MenuDataManager();
        allIngredients = dataManager.loadToppingsAndCrusts();

        setupDropdowns();
        setupToppings();

        // Add listeners to update price when user changes options
        sizeCombo.setOnAction(e -> updatePrice());
        crustCombo.setOnAction(e -> updatePrice());
        sauceCombo.setOnAction(e -> updatePrice());
    }

    private void setupDropdowns() {
        sizeCombo.getItems().addAll("Small", "Medium", "Large", "Extra Large"); 
        sizeCombo.setValue("Medium"); 

        List<String> crusts = allIngredients.stream()
            .filter(ing -> "Crust".equalsIgnoreCase(ing.getUnitType()))
            .map(Ingredient::getName)
            .collect(Collectors.toList());
        crustCombo.setItems(FXCollections.observableArrayList(crusts));
        crustCombo.getSelectionModel().selectFirst();

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
                CheckBox cb = new CheckBox(ing.getName() + " (+$" + String.format("%.2f", ing.getExtraCost()) + ")");
                
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
    
    private void updatePrice() {
        double basePrice = 0.0;
        
        // 1. Start with the Standard Cheese Pizza prices
        if (sizeCombo.getValue() != null) {
            switch (sizeCombo.getValue()) {
                case "Small": basePrice = 7.99; break;
                case "Medium": basePrice = 9.99; break;
                case "Large": basePrice = 11.99; break;
                case "Extra Large": basePrice = 13.99; break;
            }
        }

        // 2. --- FIX: Add the Specialty Markup ---
        // If it's a Pepperoni Pizza, this adds the missing $2.00 back
        basePrice += basePriceOffset;

        // 3. Update the model
        currentPizza.setBasePrice(basePrice);
        currentPizza.setSize(sizeCombo.getValue());
        currentPizza.setCrust(crustCombo.getValue());
        currentPizza.setSauce(sauceCombo.getValue());

        // 4. Add toppings cost
        currentPizza.clearToppings(); 
        selectedToppingObjects.forEach(currentPizza::addTopping);

        // 5. Update Label
        priceLabel.setText("Price: $" + String.format("%.2f", currentPizza.calculatePrice()));
    }

    @FXML
    private void handleAddToCart() {
        // Check Pickup vs Delivery
        if (Cart.getInstance().getOrderType() == null) {
            ChoiceDialog<String> dialog = new ChoiceDialog<>("Pickup", "Pickup", "Delivery");
            dialog.setTitle("Start Order");
            dialog.setHeaderText("How would you like to receive your order?");
            dialog.setContentText("Choose option:");

            Optional<String> result = dialog.showAndWait();

            if (result.isPresent()) {
                Cart.getInstance().setOrderType(result.get());
            } else {
                return; 
            }
        }

        Cart.getInstance().addItem(currentPizza);
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION, 
                                "Custom Pizza: " + currentPizza.getSize() + " added to order!");
        alert.showAndWait();
        
        ((Stage) priceLabel.getScene().getWindow()).close();
    }

    @FXML
    private void handleCancel() {
        ((Stage) priceLabel.getScene().getWindow()).close();
    }
}