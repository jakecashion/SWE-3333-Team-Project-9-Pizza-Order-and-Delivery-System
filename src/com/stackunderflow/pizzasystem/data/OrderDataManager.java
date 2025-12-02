package com.stackunderflow.pizzasystem.data;

import com.stackunderflow.pizzasystem.model.Ingredient;
import com.stackunderflow.pizzasystem.model.MenuItem;
import com.stackunderflow.pizzasystem.model.Pizza;
import java.sql.*;

public class OrderDataManager {

    /**
     * Inserts the main order record and returns the generated Order_ID.
     */
    public int createOrder(int customerId, double totalPrice) {
        // MS Access uses Now() for current timestamp. 
        // 'Pickup' is hardcoded as default, or you can pass it as a parameter.
        String sql = "INSERT INTO Orders (Customer_ID, Order_Date, Order_Type, Total_Price, Status) VALUES (?, Now(), 'Pickup', ?, 'Pending')";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, customerId);
            pstmt.setDouble(2, totalPrice);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) return -1;

            // Retrieve the auto-generated Order_ID
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 on failure
    }

    /**
     * Inserts a single item from the cart into OrderItems and returns the generated OrderItem_ID.
     */
    public int createOrderItem(int orderId, MenuItem item) {
        String sql = "INSERT INTO OrderItems (Order_ID, Item_ID, Quantity, Subtotal) VALUES (?, ?, 1, ?)";
        
        double price = item.getBasePrice();
        if (item instanceof Pizza) {
            price = ((Pizza) item).calculatePrice();
        }

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, orderId);
            pstmt.setInt(2, item.getItemId());
            pstmt.setDouble(3, price);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) return -1;

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Links specific ingredients (toppings/crust/sauce) to an OrderItem.
     */
    public void createOrderCustomization(int orderItemId, Ingredient ingredient) {
        String sql = "INSERT INTO OrderItemCustomizations (OrderItem_ID, Ingredient_ID, Type) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, orderItemId);
            // Note: Ensure getIngredientId() exists in your Ingredient model
            pstmt.setInt(2, ingredient.getIngredientId()); 
            pstmt.setString(3, ingredient.getUnitType()); 
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}