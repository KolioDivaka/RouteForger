package org.example.services;

import org.example.models.Category;
import org.example.models.Place;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PlaceServiceTest {

    private Place createPlace() {
        return new Place(
                UUID.randomUUID(),
                "History Museum",
                Category.MUSEUM,
                90,
                new BigDecimal("12.00"),
                Set.of("history")
        );
    }

    @Test
    void addsAndReturnsPlace() {
        PlaceService service = new PlaceService();
        Place museum = createPlace();

        service.addPlace(museum);

        assertEquals(1, service.getAllPlaces().size());
        assertEquals(museum, service.getAllPlaces().getFirst());
    }

    @Test
    void findsPlaceById() {
        PlaceService service = new PlaceService();
        Place museum = createPlace();

        service.addPlace(museum);

        Place result = service.getById(museum.getId());

        assertEquals(museum, result);
    }

    @Test
    void searchesPlaceByPartOfNameIgnoringCase() {
        PlaceService service = new PlaceService();
        Place museum = createPlace();

        service.addPlace(museum);

        assertEquals(
                1,
                service.searchByName("history").size()
        );
    }

    @Test
    void removesPlaceById() {
        PlaceService service = new PlaceService();
        Place museum = createPlace();

        service.addPlace(museum);
        service.removePlace(museum.getId());

        assertTrue(service.getAllPlaces().isEmpty());
    }

    @Test
    void rejectsDuplicateId() {
        PlaceService service = new PlaceService();
        UUID id = UUID.randomUUID();

        Place first = new Place(
                id,
                "Museum",
                Category.MUSEUM,
                90,
                new BigDecimal("12.00"),
                Set.of("history")
        );

        Place second = new Place(
                id,
                "Another Museum",
                Category.MUSEUM,
                60,
                new BigDecimal("8.00"),
                Set.of("art")
        );

        service.addPlace(first);

        assertThrows(
                IllegalArgumentException.class,
                () -> service.addPlace(second)
        );
    }
}