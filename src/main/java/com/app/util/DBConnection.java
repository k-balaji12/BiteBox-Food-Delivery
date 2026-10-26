
package com.app.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static Connection getConnection() throws SQLException {

        String host = System.getenv("DB_HOST");

        String url;
        String username;
        String password;

        if (host != null && !host.isBlank()) {
            // Render: connect to Aiven
            String port = System.getenv("DB_PORT");
            String database = System.getenv("DB_NAME");

            username = System.getenv("DB_USER");
            password = System.getenv("DB_PASSWORD");

            if (port == null || database == null ||
                username == null || password == null) {
                throw new SQLException(
                    "Database environment variables are incomplete."
                );
            }

            url = "jdbc:mysql://" + host + ":" + port + "/"
                    + database + "?sslMode=REQUIRED";

        } else {
            // Eclipse: connect to local MySQL
            url = "jdbc:mysql://localhost:3306/bitebox";
            username = "root";
            password = System.getenv("LOCAL_DB_PASSWORD");

            if (password == null) {
                throw new SQLException(
                    "LOCAL_DB_PASSWORD is not configured."
                );
            }
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found.", e);
        }

        return DriverManager.getConnection(url, username, password);
    }
}
