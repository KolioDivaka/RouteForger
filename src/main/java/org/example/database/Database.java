package org.example.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private final String URL;

    public Database() {
        this("routeforger.db");
    }

    public Database(String databasePath) {
        this.URL = "jdbc:sqlite:" + databasePath;
    }
    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(URL);

        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }

        return connection;
    }


}
