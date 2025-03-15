package com.nerfex.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private static final String SQL_FILE = "src/main/resources/trackify.sql"; // Store SQL file here

    public static void initialize() {
        try (Statement stmt = DatabaseConnector.getConnection().createStatement()) {

            // Connect to the new database
            try (Statement dbStmt = DatabaseConnector.getConnection().createStatement()) {
                // Execute SQL script
                executeSQLScript(dbStmt, SQL_FILE);
                System.out.println("Database setup complete.");
            }
        } catch (SQLException e) {
            System.err.println("Database Initialization Error:" + e.getMessage());
        }
    }

    private static void executeSQLScript(Statement stmt, String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sql = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("--") && !line.trim().isEmpty()) { // Ignore comments and empty lines
                    sql.append(" " + line);
                    if (line.endsWith(";")) { // Execute when a full query is read
                        stmt.executeUpdate(sql.toString());
                        sql.setLength(0); // Reset for the next query
                    }
                }
            }
        } catch (IOException | SQLException e) {
            System.err.println("Error executing SQL script: " + e.getMessage());
        }
    }
}
