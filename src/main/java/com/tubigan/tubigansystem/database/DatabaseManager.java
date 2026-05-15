package com.tubigan.tubigansystem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseManager - Singleton class for managing SQLite database connections
 * and schema initialization for the TubiganSystem JavaFX application.
 */
public class DatabaseManager {

    private static final String DATABASE_URL = "jdbc:sqlite:tubigan.db";
    private static final String DATABASE_DRIVER = "org.sqlite.JDBC";

    private static volatile DatabaseManager instance;
    private Connection connection;

    /**
     * Private constructor to enforce Singleton pattern.
     * Loads the SQLite JDBC driver, initializes the database connection,
     * and enforces Foreign Key constraints.
     */
    private DatabaseManager() {
        try {
            Class.forName(DATABASE_DRIVER);
            this.connection = DriverManager.getConnection(DATABASE_URL);

            // EXPERT TWEAK: Force SQLite to respect Foreign Keys
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }

            initializeDatabase();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found. Ensure sqlite-jdbc is on the classpath.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish database connection.", e);
        }
    }

    /**
     * Returns the singleton instance of DatabaseManager.
     * Uses double-checked locking for thread safety.
     *
     * @return The single DatabaseManager instance.
     */
    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    /**
     * Returns the active database connection.
     *
     * @return The java.sql.Connection object.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Initializes the database schema by creating required tables if they
     * do not already exist, and seeds a default admin account.
     */
    public void initializeDatabase() {
        createUsersTable();
        createOrdersTable();
        seedDefaultAdmin();
    }

    /**
     * Creates the 'users' table if it does not exist.
     * Stores customer and admin account information, including profile details.
     */
    private void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "full_name TEXT NOT NULL, "
                + "email TEXT UNIQUE NOT NULL, "
                + "password TEXT NOT NULL, "
                + "phone_number TEXT, "
                + "street_address TEXT, "
                + "barangay TEXT, "
                + "city TEXT, "
                + "province TEXT, "
                + "zip_code TEXT, "
                + "role TEXT DEFAULT 'CUSTOMER'"
                + ");";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create 'users' table.", e);
        }
    }

    /**
     * Creates the 'orders' table if it does not exist.
     * Stores water order information with a strict foreign key reference to users.
     */
    private void createOrdersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS orders ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER, "
                + "gallons INTEGER NOT NULL, "
                + "order_type TEXT, "
                + "status TEXT, "
                + "amount REAL NOT NULL, "
                + "payment_method TEXT, "
                + "order_date TEXT, "
                + "FOREIGN KEY (user_id) REFERENCES users(id)"
                + ");";

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create 'orders' table.", e);
        }
    }

    /**
     * Seeds the database with a default admin account if one does not already exist.
     * Admin credentials:
     * Email: admin
     * Password: admin123
     * Full Name: System Admin
     * Role: ADMIN
     */
    private void seedDefaultAdmin() {
        String checkSql = "SELECT COUNT(*) FROM users WHERE email = ? AND role = 'ADMIN'";
        String insertSql = "INSERT INTO users (full_name, email, password, role) VALUES (?, ?, ?, 'ADMIN')";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, "admin");
            try (ResultSet resultSet = checkStmt.executeQuery()) {
                if (resultSet.next() && resultSet.getInt(1) == 0) {
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                        insertStmt.setString(1, "System Admin");
                        insertStmt.setString(2, "admin");
                        insertStmt.setString(3, "admin123");
                        insertStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to seed default admin account.", e);
        }
    }

    /**
     * Closes the database connection.
     * Should be called when the application is shutting down.
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException("Failed to close database connection.", e);
            }
        }
    }
}