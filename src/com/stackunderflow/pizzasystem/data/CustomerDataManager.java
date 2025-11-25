package com.stackunderflow.pizzasystem.data;

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

    public String validateLogin(String username, String rawPassword) {
        String sql = "SELECT Password_Hash FROM Customers WHERE Username = ?";
        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("Password_Hash");
                    if (PasswordHasher.check(rawPassword, storedHash)) return "Login Success";
                    else return "Error: Invalid password";
                } else { return "Error: User not found"; }
            }
        } catch (SQLException e) { return "Error: Database problem"; }
    }
}
