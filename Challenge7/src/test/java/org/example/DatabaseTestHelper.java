package org.example;

import java.sql.Connection;

public class DatabaseTestHelper {
    private static Connection testConnection;

    public static void setConnection(Connection connection) {
        testConnection = connection;
    }

    public static Connection getConnection() {
        return testConnection;
    }
}
