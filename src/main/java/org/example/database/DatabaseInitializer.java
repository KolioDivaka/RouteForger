package org.example.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    private final Database database;

    public DatabaseInitializer ( Database database){
        this.database = database;
    }

    public void initialize() throws SQLException{
        String createPlaces = """
                CREATE TABLE IF NOT EXISTS places (
                    id TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    category TEXT NOT NULL,
                    duration_minutes INTEGER NOT NULL
                        CHECK (duration_minutes > 0),
                    price_cents INTEGER NOT NULL
                        CHECK (price_cents >= 0)
                )
                """;

        String createPlaceTags = """
                CREATE TABLE IF NOT EXISTS place_tags (
                    place_id TEXT NOT NULL,
                    tag TEXT NOT NULL,
                    PRIMARY KEY (place_id, tag),
                    FOREIGN KEY (place_id)
                        REFERENCES places(id)
                        ON DELETE CASCADE
                )
                """;

        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement()) {

            // Execute the places creation first
            stmt.execute(createPlaces);
            // Then execute dependent tables
            stmt.execute(createPlaceTags);
        }
    }

}
