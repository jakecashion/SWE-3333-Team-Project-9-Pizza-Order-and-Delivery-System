package com.stackunderflow.pizzasystem.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the customer's entire order session (Shopping Cart).
 */
public class Cart {

    private static Cart instance = null;

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    private List<MenuItem> items; 
    // Assumed tax rate (6% based on your receipt mockup).
    private static final double TAX_RATE = 0.06; 

    public Cart() {
        this.items = new ArrayList<>();
    }

    // --- Cart Management (Task 6) ---
    
    public void addItem(MenuItem item) {
        this.items.add(item);
    }
    
    public void removeItem(MenuItem item) {
        this.items.remove(item);
    }

    // --- Final Calculation Logic (P1.9) ---
    
    public double calculateSubtotal() {
        double subtotal = 0.0;
        for (MenuItem item : items) {
            // If the item is a Pizza, use its dynamic calculation method.
            if (item instanceof Pizza) {
                subtotal += ((Pizza) item).calculatePrice();
            } else {
                // Otherwise, use the item's fixed base price (for drinks/sides).
                subtotal += item.getBasePrice(); 
            }
        }
        return subtotal;
    }
    
    public double calculateTax() {
        return calculateSubtotal() * TAX_RATE;
    }
    
    public double calculateGrandTotal() {
        return calculateSubtotal() + calculateTax();
    }
    
    public List<MenuItem> getItems() {
        return items;
    }
}
