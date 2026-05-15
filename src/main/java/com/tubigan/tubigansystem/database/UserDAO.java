package com.tubigan.tubigansystem.database;

import com.tubigan.tubigansystem.models.User;
import java.sql.*;

public class UserDAO {

    // LOGIN LOGIC
    public User login(String email, String password) {
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        Connection conn = DatabaseManager.getInstance().getConnection(); // Moved outside

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Login failed
    }

    // REGISTRATION LOGIC
    public boolean register(String name, String email, String password) {
        String query = "INSERT INTO users (full_name, email, password, role) VALUES (?, ?, ?, 'CUSTOMER')";
        Connection conn = DatabaseManager.getInstance().getConnection(); // Moved outside

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // PROFILE UPDATE LOGIC
    public boolean updateProfile(int userId, String phone, String street, String barangay, String city) {
        String sql = "UPDATE users SET phone_number = ?, street_address = ?, barangay = ?, city = ? WHERE id = ?";
        Connection conn = DatabaseManager.getInstance().getConnection(); // Moved outside

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
            pstmt.setString(2, street);
            pstmt.setString(3, barangay);
            pstmt.setString(4, city);
            pstmt.setInt(5, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}