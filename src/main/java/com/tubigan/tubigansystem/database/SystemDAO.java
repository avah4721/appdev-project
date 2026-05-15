package com.tubigan.tubigansystem.database;

import com.tubigan.tubigansystem.models.Customer;
import com.tubigan.tubigansystem.models.Order;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SystemDAO {

    public boolean placeOrder(int userId, int gallons, String orderType, double amount, String paymentMethod) {
        String sql = "INSERT INTO orders (user_id, gallons, order_type, amount, payment_method, status, order_date) " +
                "VALUES (?, ?, ?, ?, ?, 'Pending', datetime('now', 'localtime'))";
        Connection conn = DatabaseManager.getInstance().getConnection(); // Moved outside

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, gallons);
            pstmt.setString(3, orderType);
            pstmt.setDouble(4, amount);
            pstmt.setString(5, paymentMethod);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT id, full_name, street_address, phone_number FROM users WHERE role = 'CUSTOMER'";
        Connection conn = DatabaseManager.getInstance().getConnection(); // Moved outside

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Customer(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("street_address") != null ? rs.getString("street_address") : "No Address",
                        rs.getString("phone_number") != null ? rs.getString("phone_number") : "No Phone"
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        Connection conn = DatabaseManager.getInstance().getConnection(); // Moved outside

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, orderId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> getOrdersByUser(int userId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";
        Connection conn = DatabaseManager.getInstance().getConnection(); // Moved outside

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Order(
                            rs.getInt("id"), rs.getInt("user_id"), rs.getInt("gallons"),
                            rs.getString("order_type"), rs.getDouble("amount"),
                            rs.getString("payment_method"), rs.getString("status"), rs.getString("order_date")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM orders ORDER BY order_date DESC";
        Connection conn = DatabaseManager.getInstance().getConnection(); // Moved outside

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Order(
                        rs.getInt("id"), rs.getInt("user_id"), rs.getInt("gallons"),
                        rs.getString("order_type"), rs.getDouble("amount"),
                        rs.getString("payment_method"), rs.getString("status"), rs.getString("order_date")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public double getTotalRevenue() {
        String sql = "SELECT SUM(amount) FROM orders WHERE status = 'Paid'";
        Connection conn = DatabaseManager.getInstance().getConnection(); // Moved outside

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}