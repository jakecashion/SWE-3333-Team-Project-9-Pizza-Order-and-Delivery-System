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
    
    // Getters for display (P1.6, P1.7)
    public String getName() { return name; }
    public double getBasePrice() { return basePrice; }
    public String getCategory() { return category; }
    public int getItemId() { return itemId; }
    // ... add other necessary getters/setters ...
}