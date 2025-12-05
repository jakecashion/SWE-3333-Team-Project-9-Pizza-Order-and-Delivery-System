package com.stackunderflow.pizzasystem.util;

import com.stackunderflow.pizzasystem.data.DatabaseConnector;
import com.stackunderflow.pizzasystem.util.PasswordHasher;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseSeeder {

    public static void main(String[] args) {
        clearTables(); // <--- NEW: Deletes all old data
        seedMenuItems();
        seedInventory();
        seedUsers();
    }

    // New method to clear data from tables before inserting.
    private static void clearTables() {
        // SQL commands to delete all rows from the tables we are seeding
        String[] deleteSQLs = {
            "DELETE FROM MenuItems",
            "DELETE FROM Inventory"
        };

        try (Connection conn = DatabaseConnector.getConnection()) {
            for (String sql : deleteSQLs) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.executeUpdate();
                }
            }
            System.out.println("Tables cleared successfully!");
        } catch (SQLException e) {
            System.err.println("Error clearing tables: " + e.getMessage());
            // If this fails, the menu will be duplicated, but the app won't crash.
        }
    }

    private static void seedMenuItems() {
        String sql = "INSERT INTO MenuItems (Item_Name, Description, Base_Price, Category) VALUES (?, ?, ?, ?)";
        
        Object[][] items = {
            {"Cheese Pizza", "Classic cheese", 9.99, "Pizza"},
            {"Custom Pizza", "Build your own pizza with any toppings", 9.99, "Pizza"},
            {"Pepperoni Pizza", "Pepperoni and cheese", 11.99, "Pizza"},
            {"Meat Lovers", "Bacon, pepperoni, and sausage", 14.99, "Pizza"},
            {"Veggie Pizza", "Onions, peppers, and olives", 12.99, "Pizza"},
            {"Coke", "20oz Bottle", 2.50, "Drink"},
            {"Sprite", "20oz Bottle", 2.50, "Drink"},
            {"Fanta", "20oz Bottle", 2.50, "Drink"},
            {"Lava Cake", "Chocolate molten cake", 5.99, "Dessert"},
            {"Cookie", "Chocolate chunk cookie", 4.99, "Dessert"},
            {"Breadsticks", "6 pieces with marinara", 4.99, "Side"}
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
            {"Thin", "Crust", 0.00, 100},
            {"Pan", "Crust", 1.00, 100},
            {"Regular", "Crust", 0.00, 100},
            {"Marinara", "Sauce", 0.00, 50},
            {"Alfredo", "Sauce", 1.00, 50},
            {"Cheese", "Topping", 0.00, 200},
            {"Pepperoni", "Topping", 1.50, 200},
            {"Sausage", "Topping", 1.50, 200},
            {"Bacon", "Topping", 1.50, 200},
            {"Beef", "Topping", 1.50, 200},
            {"Chicken", "Topping", 1.50, 200},
            {"Onion", "Topping", 0.50, 200},
            {"Olives", "Topping", 0.50, 200},
            {"Mushroom", "Topping", 0.50, 200},
            {"Pineapple", "Topping", 0.50, 200},
            {"Jalapeno", "Topping", 0.50, 200}
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

    private static void seedUsers() {
        // SQL to insert a Customer and an Employee (Manager)
        String sqlCustomer = "INSERT INTO Customers (Username, Password_Hash, First_Name, Last_Name, Phone_Number, Address) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlEmployee = "INSERT INTO Employees (Username, Password_Hash, First_Name, Last_Name, Role) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection()) {
            
            // 1. Create Test Customer (user: customer | pass: password123)
            try (PreparedStatement pstmt = conn.prepareStatement(sqlCustomer)) {
                pstmt.setString(1, "customer");
                pstmt.setString(2, PasswordHasher.hash("password123")); // Hash the password
                pstmt.setString(3, "John");
                pstmt.setString(4, "Doe");
                pstmt.setString(5, "5551234567");
                pstmt.setString(6, "123 Pizza Lane");
                pstmt.executeUpdate();
            }

            // 2. Create Test Manager (user: manager | pass: admin123)
            try (PreparedStatement pstmt = conn.prepareStatement(sqlEmployee)) {
                pstmt.setString(1, "manager");
                pstmt.setString(2, PasswordHasher.hash("admin123")); // Hash the password
                pstmt.setString(3, "Jane");
                pstmt.setString(4, "Boss");
                pstmt.setString(5, "Manager"); // Role
                pstmt.executeUpdate();
            }

            System.out.println("Test Users (Customer & Manager) populated!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}