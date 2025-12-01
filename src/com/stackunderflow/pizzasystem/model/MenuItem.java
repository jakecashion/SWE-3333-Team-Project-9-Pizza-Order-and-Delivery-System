package com.stackunderflow.pizzasystem.model;

public class MenuItem {
    private int itemId;
    private String name;
    private double basePrice;
    private String category; // e.g., "Pizza", "Drink", "Dessert"

    public MenuItem(int itemId, String name, double basePrice, String category) {
        this.itemId = itemId;
        this.name = name;
        this.basePrice = basePrice;
        this.category = category;
    }
    
    // --- Getters ---
    public int getItemId() { return itemId; } // (New!)
    public String getName() { return name; }
    public double getBasePrice() { return basePrice; }
    public String getCategory() { return category; }

    // --- Setters ---
    public void setBasePrice(double basePrice) { // (New!)
        this.basePrice = basePrice; 
    }
}