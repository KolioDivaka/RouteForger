package org.example.services;

import org.example.database.Database;
import org.example.database.DatabaseInitializer;
import org.example.models.Category;
import org.example.models.Place;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlaceRepositoryTest {

    @TempDir
    Path tempDir;

    @Test
    void addGetAndRemovePlace() throws SQLException {
        Path dbFile = tempDir.resolve("test.db");

        // Adapt these two lines to your actual constructors/method names.
        Database database = new Database(dbFile.toString());
        new DatabaseInitializer(database).initialize();

        PlaceRepository repository = new PlaceRepository(database);

        UUID id = UUID.randomUUID();
        Place expected = new Place(
                id,
                "Test Museum",
                Category.MUSEUM, // replace if your enum uses a different name
                60,
                new BigDecimal("12.99"),
                Set.of("history", "indoor")
        );

        repository.addPlace(expected);

        List<Place> places = repository.getAllPlaces();
        Place actual = places.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow();

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getCategory(), actual.getCategory());
        assertEquals(expected.getDurationMinutes(), actual.getDurationMinutes());
        assertEquals(0, expected.getPriceEur().compareTo(actual.getPriceEur()));
        assertEquals(expected.getTags(), actual.getTags());

        assertTrue(repository.remove(id));
        assertTrue(repository.getAllPlaces().stream()
                .noneMatch(p -> p.getId().equals(id)));
        assertFalse(repository.remove(id));
    }
}