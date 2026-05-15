package com.tubigan.tubigansystem;

import com.tubigan.tubigansystem.database.UserDAO;
import com.tubigan.tubigansystem.database.SystemDAO;
import com.tubigan.tubigansystem.database.DatabaseManager;
import com.tubigan.tubigansystem.models.User;
import com.tubigan.tubigansystem.models.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.util.List;

public class HelloController {

    private final UserDAO userDAO = new UserDAO();
    private final SystemDAO systemDAO = new SystemDAO();

    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        // Trigger the database to wake up
        DatabaseManager.getInstance();

        // 1. Dry Run Login
        User admin = userDAO.login("admin", "admin123");

        if (admin != null) {
            // 2. Register a customer properly using UserDAO instead of the deleted addCustomer method
            boolean isRegistered = userDAO.register("Test Juan", "juan@test.com", "password123");

            if (isRegistered) {
                // 3. Fetch customers and update UI
                List<Customer> customers = systemDAO.getAllCustomers();
                welcomeText.setText("Success! Total Customers: " + customers.size());
            } else {
                welcomeText.setText("Customer 'juan@test.com' might already exist!");
            }
        } else {
            welcomeText.setText("Database Connection Failed.");
        }
    }
}