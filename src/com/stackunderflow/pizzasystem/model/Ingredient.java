package com.stackunderflow.pizzasystem.model;

/**
 * Represents a component from the Inventory table, used for customization and dynamic pricing.
 */
public class Ingredient {
    private int ingredientId;
    private String name;
    private double extraCost; // Cost added when used as a topping
    private String unitType; // e.g., "lbs", "units", "Pan", "Thin"

    public Ingredient(int ingredientId, String name, double extraCost, String unitType) {
        this.ingredientId = ingredientId;
        this.name = name;
        this.extraCost = extraCost;
        this.unitType = unitType;
    }

    // Getters for customization and price calculation (P1.7)
    public String getName() { return name; }
    public double getExtraCost() { return extraCost; }
    public String getUnitType() { return unitType; }
    public int getIngredientId() { return ingredientId; }
    // ... add other necessary getters/setters ...
}
