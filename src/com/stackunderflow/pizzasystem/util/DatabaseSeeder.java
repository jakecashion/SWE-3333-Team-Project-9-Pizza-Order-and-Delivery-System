package com.stackunderflow.pizzasystem.util;

import com.stackunderflow.pizzasystem.data.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseSeeder {

    public static void main(String[] args) {
        seedMenuItems();
        seedInventory();
    }

    private static void seedMenuItems() {
        String sql = "INSERT INTO MenuItems (Item_Name, Description, Base_Price, Category) VALUES (?, ?, ?, ?)";
        
        // Data from Sprint 2 Requirements
        Object[][] items = {
            {"Cheese Pizza", "Classic cheese", 9.99, "Pizza"},
            {"Pepperoni Pizza", "Pepperoni and cheese", 11.99, "Pizza"},
            {"Coke", "20oz Bottle", 2.50, "Drink"},
            {"Lava Cake", "Chocolate molten cake", 5.99, "Dessert"}
        };

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            for (Object[] item : items) {
                pstmt.setString(1, (String) item[0]);
                pstmt.setString(2, (String) item[1]);
                pstmt.setDouble(3, (double) item[2]);
                pstmt.setString(4, (String) item[3]);
                pstmt.executeUpdate();
            }
            System.out.println("MenuItems populated!");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void seedInventory() {
        String sql = "INSERT INTO Inventory (Ingredient_Name, Unit_Type, Extra_Cost, Current_Stock) VALUES (?, ?, ?, ?)";
        
        Object[][] ingredients = {
            {"Thin Crust", "Crust", 0.00, 100},
            {"Pan Crust", "Crust", 1.00, 100},
            {"Pepperoni", "Topping", 1.50, 200},
            {"Cheese", "Topping", 0.00, 200}
        };

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            for (Object[] ing : ingredients) {
                pstmt.setString(1, (String) ing[0]);
                pstmt.setString(2, (String) ing[1]);
                pstmt.setDouble(3, (double) ing[2]);
                pstmt.setInt(4, (int) ing[3]);
                pstmt.executeUpdate();
            }
            System.out.println("Inventory populated!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}