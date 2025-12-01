package com.stackunderflow.pizzasystem.model;

import java.util.ArrayList;
import java.util.List;

public class Pizza extends MenuItem { 

    private String size;
    private String crust;
    private String sauce;
    private List<Ingredient> toppingsList; 
    
    public Pizza(int itemId, String name, double basePrice, String size, String crust, String sauce) {
        super(itemId, name, basePrice, "Pizza"); 
        this.size = size;
        this.crust = crust;
        this.sauce = sauce;
        this.toppingsList = new ArrayList<>();
    }

    // --- Customization Methods ---
    public void addTopping(Ingredient topping) {
        this.toppingsList.add(topping);
    }
    
    public void removeTopping(Ingredient topping) {
        this.toppingsList.remove(topping);
    }
    
    public void clearToppings() { // (Used by Controller to reset toppings)
        this.toppingsList.clear();
    }

    // --- Setters for Customization ---
    public void setSize(String size) { this.size = size; }
    public void setCrust(String crust) { this.crust = crust; }
    public void setSauce(String sauce) { this.sauce = sauce; }

    // --- Core Business Logic ---
    public double calculatePrice() {
        // 1. Start with the base price (set by the size).
        double totalPrice = this.getBasePrice();
        
        // 2. Add cost of every topping.
        for (Ingredient topping : toppingsList) {
            totalPrice += topping.getExtraCost();
        }
        return totalPrice;
    }

    // --- Getters ---
    public List<Ingredient> getToppings() { return toppingsList; }
    public String getSize() { return size; }
    public String getCrust() { return crust; }
    public String getSauce() { return sauce; }
}