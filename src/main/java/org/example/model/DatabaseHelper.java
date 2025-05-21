package org.example.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// this handles the connection to a SQLite database through JDBC - creates the connection and closes the connection

public class DatabaseHelper {
    private static final String DB_URL = "jdbc:sqlite:tasks.db";

    private static Connection conn;

    // Singleton pattern for connection management
    public static Connection getConnection() {
        if (conn == null) {
            try {
                // Load SQLite JDBC driver
                Class.forName("org.sqlite.JDBC");
                conn = DriverManager.getConnection(DB_URL);
                System.out.println("Connection established!");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return conn;
    }

    // Close the connection if it's open
    public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
                conn = null; // Set to null after closing to avoid reuse
                System.out.println("Connection closed!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
