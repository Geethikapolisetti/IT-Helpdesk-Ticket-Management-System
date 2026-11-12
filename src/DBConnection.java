package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/helpdesk_db";

    private static final String USER = "root";

    public static Connection getConnection() {

        String password = System.getenv("HELPDESK_DB_PASSWORD");

        if (password == null || password.isEmpty()) {
            throw new RuntimeException(
                "Database password not configured. Set HELPDESK_DB_PASSWORD."
            );
        }

        try {
            return DriverManager.getConnection(URL, USER, password);
        } catch (SQLException e) {
            System.out.println("Database connection failed!");
            throw new RuntimeException(e);
        }
    }
}