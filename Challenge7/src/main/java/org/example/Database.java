package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.example.DatabaseTestHelper;

public class Database {
    private static final String URL = "jdbc:mysql://localhost:3306/challenge6";
    private static final String USER = "root";
    private static final String PASSWORD = "Luzoscura0531";

    public static Connection getConnection() throws SQLException {
        if (DatabaseTestHelper.getConnection() != null) {
            return DatabaseTestHelper.getConnection();
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

