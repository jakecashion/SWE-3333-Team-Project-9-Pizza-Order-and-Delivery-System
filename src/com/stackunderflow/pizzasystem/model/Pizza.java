package com.stackunderflow.pizzasystem.model;

import java.util.ArrayList;
import java.util.List;

public class Pizza extends MenuItem { 

    private String size;
    private String crust;
    private String sauce;
    // Toppings are stored as Ingredient objects (from the Inventory table).
    private List<Ingredient> toppingsList; 
    
    public Pizza(int itemId, String name, double basePrice, String size, String crust, String sauce) {
        super(itemId, name, basePrice, "Pizza"); 
        this.size = size;
        this.crust = crust;
        this.sauce = sauce;
        this.toppingsList = new ArrayList<>();
    }

    // --- Customization Methods (Used by P1.7 UI) ---
    
    public void addTopping(Ingredient topping) {
        this.toppingsList.add(topping);
    }
    
    public void removeTopping(Ingredient topping) {
        this.toppingsList.remove(topping);
    }
    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }
    public void setSize(String size) { this.size = size; }
    public void setCrust(String crust) { this.crust = crust; }
    public void setSauce(String sauce) { this.sauce = sauce; }
    public void clearToppings() { this.toppingsList.clear(); }
    // --- Core Business Logic (Task 5: Dynamic Pricing) ---
    
    /**
     * Calculates the total price: Base Price (Size) + Sum of all Topping Costs.
     */
    public double calculatePrice() {
        // 1. Start with the base price (set by the size).
        double totalPrice = this.getBasePrice();
        
        // 2. Add cost of every topping/component.
        for (Ingredient topping : toppingsList) {
            totalPrice += topping.getExtraCost();
        }
        return totalPrice;
    }

    // --- Getters for Cart/Summary Display (P1.9) ---
    public List<Ingredient> getToppings() {
        return toppingsList;
    }
    
    public String getSize() {
        return size;
    }
    // ... add other necessary getters for crust and sauce ...
}
