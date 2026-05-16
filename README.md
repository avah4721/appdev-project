# Tubigan System — UI/Frontend Developer Guidelines & Blueprint

Welcome to the frontend and user interface development layer of the **Tubigan System**. This document outlines the application's visual architecture, layout standards, FXML-to-Java controller bindings, and scene navigation patterns using **JavaFX 17** and **Maven**.

---

## 🛠️ UI Architecture & Design Pattern

The Tubigan System strictly enforces the **Model-View-Controller (MVC)** design pattern. To preserve a clean separation of concerns, frontend developers should limit modifications to the **Views** (FXML markup) and **Controllers** (Java event handlers).

### Frontend Component Layout Flow

### Core UI Development Rules:
1. **Zero Raw Queries:** Never write database connections, result processing, or direct SQL code inside a Controller. Always invoke methods from the backend DAO tier.
2. **Resource Isolation:** Keep all `.fxml` and visual assets strictly inside the `src/main/resources/com/tubigan/tubigansystem/` package.
3. **Controller Declarations:** Every root element in an FXML file must declare its explicit controlling class via the `fx:controller` attribute.

---

## 🏷️ Naming & Component Binding Standards (`@FXML`)

To maintain a predictable and clean code integration pipeline between multiple programmers, use these naming conventions for UI control IDs:

| UI Component Type | FXML Variable Suffix/Prefix | Code Example (`fx:id`) |
| :--- | :--- | :--- |
| **Input Fields** | Suffix: `Field` | `emailField`, `passwordField` |
| **Action Buttons** | Suffix: `Button` | `loginButton`, `refreshButton` |
| **Interactive Hyperlinks** | Suffix: `Link` | `registerLink` |
| **Data Grid Tables** | Suffix: `Table` | `ordersTable` |
| **Table Grid Columns** | Prefix: `col` | `colOrderId`, `colStatus`, `colAmount` |
| **Information Labels** | Suffix: `Label` | `errorLabel`, `revenueLabel` |

### Action Method Conventions:
Any Java method mapped via the `onAction` property inside an FXML file must use the `handle` prefix:
* FXML Element: `<Button onAction="#handleMarkPaid" text="Paid" />`
* Java Controller: `@FXML protected void handleMarkPaid() { ... }`

---

## 🖥️ Screen Blueprints & Component Matrix

### 1. Login Screen (`login-view.fxml` ➔ `LoginController.java`)
* **Purpose:** Initial security boundary. Collects operator credentials and triggers appropriate stage routing based on client access levels.
* **Exposed Variables:**
    * `TextField emailField`
    * `PasswordField passwordField`
    * `Label errorLabel`
* **Bound Action Hooks:**
    * `handleLoginButton()`: Feeds input values into `userDAO.login()`. Handles stage view-swapping upon positive authentication or passes error signals to the UI text display.
    * `handleRegisterLink()`: Re-routes the window window to the customer registration form layout.

### 2. Admin Dashboard (`admin-dashboard.fxml` ➔ `AdminDashboardController.java`)
* **Purpose:** The primary operational control deck for refilling station management. Renders systemic order indexes, calculates operational balances, and changes logistical fulfillment state conditions.
* **Exposed Variables:**
    * `TableView<Order> ordersTable`
    * `TableColumn<Order, Integer> colOrderId`, `colCustomerId`, `colGallons`
    * `TableColumn<Order, String> colType`, `colStatus`, `colDate`
    * `TableColumn<Order, Double> colAmount`
    * `Label revenueLabel`
* **Bound Action Hooks:**
    * `handleRefresh()`: Fetches the latest database records via `systemDAO.getAllOrders()` and flushes rows to the `ordersTable`.
    * `handleMarkDelivered()` / `handleMarkPaid()`: Updates the target item row state to `'Delivered'` or `'Paid'` and updates the financial calculation display counter string via `systemDAO.getTotalRevenue()`.

---

## 🔄 Desktop Window Navigation & Scene Swapping

To swap screens within a single application window without spawning messy separate desktop processes, execute an FXML asset tree swap on the primary Stage container.

### Standardization Template for Page Navigation:
```java
try {
    // 1. Locate and compile the target view layout resource
    FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-dashboard.fxml"));
    
    // 2. Capture the active window stage container from any visible node
    Stage stage = (Stage) emailField.getScene().getWindow(); 
    
    // 3. Swap the scene graph array tree using optimized design canvas dimensions
    stage.setScene(new Scene(loader.load(), 900, 600)); 
    stage.setTitle("Tubigan System - Admin Dashboard");
} catch (IOException e) {
    e.printStackTrace();
}

# Clear old cache buffers and sync visual layouts to target folders
mvn clean process-resources

# Run the frontend application directly via the JavaFX plugin wrapper
mvn javafx:run

🛡️ Critical Modular Modifiers (module-info.java)
Java 17 applies strong encapsulation restrictions across running modules. Because the JavaFX reflection engine needs direct read access to your code to map variables, your structural access definitions must be explicitly declared.

Ensure your module-info.java file contains these specific reflection clearance metrics:

Java
module com.tubigan.tubigansystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;

    // Open structural controllers path for layout rendering
    opens com.tubigan.tubigansystem to javafx.fxml;
    
    // CRITICAL: Open models path to allow TableView column binding reflection
    opens com.tubigan.tubigansystem.models to javafx.base;

    exports com.tubigan.tubigansystem;
}

🚀 Frontend Deployment & Build Targets
When optimizing static styles, tweaking visual nodes in Scene Builder, or pushing interface additions, use Maven to flush the layout cache into your application binary targets:

Bash
# Clear old cache buffers and sync visual layouts to target folders
mvn clean process-resources

# Run the frontend application directly via the JavaFX plugin wrapper
mvn javafx:run