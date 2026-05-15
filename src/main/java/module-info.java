module com.tubigan.tubigansystem {
    requires javafx.controls;
    requires javafx.fxml;

    // This fixes the "does not read it" errors
    requires java.sql;

    // This allows the app to use the SQLite driver you just downloaded
    requires org.xerial.sqlitejdbc;

    // This allows JavaFX to see your controllers and your database logic
    opens com.tubigan.tubigansystem to javafx.fxml;
    opens com.tubigan.tubigansystem.database to javafx.fxml;

    exports com.tubigan.tubigansystem;
    exports com.tubigan.tubigansystem.database;
}