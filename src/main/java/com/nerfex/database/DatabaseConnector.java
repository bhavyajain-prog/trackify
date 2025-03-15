package com.nerfex.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    public static final String DB_NAME = "trackify";
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                connection.createStatement().executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
                connection.close();
                connection = DriverManager.getConnection(URL + DB_NAME, USERNAME, PASSWORD);
            }
        } catch (SQLException e) {
            System.err
                    .println("Error connecting to the database. Please check if MySQL is running and the database '"
                            + DB_NAME + "' exists.");
            // Debugging
            e.printStackTrace();
        }

        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing the database connection.");
                // Debugging
                e.printStackTrace();
            }
        }
    }
}