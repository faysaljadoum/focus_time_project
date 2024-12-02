package com.example.focus_time.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/focustime_db";
    private static final String USER = "root"; // Your MySQL username
    private static final String PASSWORD = ""; // Your MySQL password

    public static Connection connect() {
        Connection conn = null;
        try {
            // Ensure the MySQL JDBC driver is loaded
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Make the connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to MySQL database: focustime_db!");
        } catch (SQLException e) {
            System.out.println("Error connecting to MySQL: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for more details
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC driver not found: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for more details
        }
        return conn;
    }

}
