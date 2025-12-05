package com.stackunderflow.pizzasystem.data;

import com.stackunderflow.pizzasystem.ui.customer.Customer;
import com.stackunderflow.pizzasystem.util.PasswordHasher;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDataManager {

    private boolean userExists(String username) {
        String sql = "SELECT Customer_ID FROM Customers WHERE Username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            return true; 
        }
    }

    public String registerUser(String username, String rawPassword, String firstName, String lastName, String phone) {
        if (userExists(username)) return "Error: Username already taken";
        String passwordHash = PasswordHasher.hash(rawPassword); 
        String sql = "INSERT INTO Customers (Username, Password_Hash, First_Name, Last_Name, Phone_Number) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, passwordHash);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, phone);
            pstmt.executeUpdate();
            return "Success: Account Created!";
        } catch (SQLException e) { return "Error: Database insertion failed."; }
    }

    public String validateLogin(String identifier, String rawPassword) {
        // UPDATED SQL: Checks if the input matches EITHER Username OR Phone_Number
        String sql = "SELECT Password_Hash FROM Customers WHERE Username = ? OR Phone_Number = ?";
        
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, identifier);
            pstmt.setString(2, identifier); // Set the same input for the second '?'
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("Password_Hash");
                    // Check if the provided password matches the stored hash
                    if (PasswordHasher.check(rawPassword, storedHash)) {
                        return "Login Success";
                    } else {
                        return "Error: Invalid password";
                    }
                } else {
                    return "Error: User not found"; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: Database problem"; 
        }
    }

    public String validateLoginByPhone(String phone, String rawPassword) {
        String sql = "SELECT Password_Hash FROM Customers WHERE Phone_Number = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("Password_Hash");
                    if (PasswordHasher.check(rawPassword, storedHash)) return "Login Success";
                    else return "Error: Invalid password";
                } else { return "Error: User not found"; }
            }
        } catch (SQLException e) { return "Error: Database problem"; }
    }

    public boolean phoneExists(String phone) {
        String sql = "SELECT Customer_ID FROM Customers WHERE Phone_Number = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
            try (ResultSet rs = pstmt.executeQuery()) { return rs.next(); }
        } catch (SQLException e) {
            return false; 
        }
    }
    public Customer getCustomerByUsername(String username) {
        String sql = "SELECT * FROM Customers WHERE Username = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    Customer customer = new Customer(
                            rs.getInt("Customer_ID"),
                            rs.getString("Username"),
                            rs.getString("First_Name"),
                            rs.getString("Last_Name"),
                            rs.getString("Phone_Number"),
                            rs.getString("Address")
                    );
                    return customer;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public Customer getCustomerByPhone(String phone) {
        String sql = "SELECT * FROM Customers WHERE Phone_Number = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phone);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("Customer_ID"),
                            rs.getString("Username"),
                            rs.getString("First_Name"),
                            rs.getString("Last_Name"),
                            rs.getString("Phone_Number"),
                            rs.getString("Address")
                    );
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }

        return null;
    }
    public String updateCustomer(int customerId, String firstName, String lastName,
                                 String phone, String address) {

        String sql = "UPDATE Customers SET First_Name = ?, Last_Name = ?, Phone_Number = ?, Address = ? WHERE Customer_ID = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, phone);
            pstmt.setString(4, address);
            pstmt.setInt(5, customerId);

            int rows = pstmt.executeUpdate();

            return (rows > 0) ? "Success: Account updated!" : "Error: No changes made";

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error: Database update failed";
        }
    }

}
