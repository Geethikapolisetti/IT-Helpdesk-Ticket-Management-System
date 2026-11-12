package src;

import java.sql.Connection;

public class DBTest {

    public static void main(String[] args) {

        try (Connection connection = DBConnection.getConnection()) {

            if (connection != null) {
                System.out.println("MySQL Connected Successfully!");
                System.out.println("Database: helpdesk_db");
            }

        } catch (Exception e) {
            System.out.println("Unable to connect to MySQL.");
            e.printStackTrace();
        }
    }
}