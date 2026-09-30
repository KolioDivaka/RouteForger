package org.example.services;

import org.example.models.Category;
import org.example.models.Place;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaceServiceTest {

    private PlaceRepository repository;
    private PlaceService service;

    @BeforeEach
    void setUp() {
        repository = mock(PlaceRepository.class);
        service = new PlaceService(repository);
    }

    private Place createPlace(String name) {
        return new Place(
                UUID.randomUUID(),
                name,
                Category.MUSEUM,
                90,
                new BigDecimal("12.00"),
                Set.of("history")
        );
    }

    @Test
    void addPlaceDelegatesToRepository() throws SQLException {
        Place place = createPlace("History Museum");

        service.addPlace(place);

        verify(repository).addPlace(place);
    }

    @Test
    void addPlaceRejectsNullWithoutCallingRepository() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addPlace(null));

        verifyNoInteractions(repository);
    }

    @Test
    void getAllPlacesReturnsRepositoryResult() throws SQLException {
        Place place = createPlace("History Museum");
        when(repository.getAllPlaces()).thenReturn(List.of(place));

        List<Place> result = service.getAllPlaces();

        assertEquals(List.of(place), result);
        verify(repository).getAllPlaces();
    }

    @Test
    void searchMatchesPartOfNameIgnoringCase() throws SQLException {
        Place museum = createPlace("History Museum");
        Place gallery = createPlace("Art Gallery");
        when(repository.getAllPlaces())
                .thenReturn(List.of(museum, gallery));

        List<Place> result = service.searchPlaces("  HISTORY  ");

        assertEquals(1, result.size());
        assertEquals(museum.getId(), result.getFirst().getId());
        verify(repository).getAllPlaces();
    }

    @Test
    void searchRejectsBlankQueryWithoutCallingRepository() {
        assertThrows(IllegalArgumentException.class,
                () -> service.searchPlaces("   "));

        verifyNoInteractions(repository);
    }

    @Test
    void removePlaceDelegatesToRepository() throws SQLException {
        UUID id = UUID.randomUUID();
        when(repository.remove(id)).thenReturn(true);

        service.removePlace(id);

        verify(repository).remove(id);
    }

    @Test
    void removePlaceRejectsUnknownId() throws SQLException {
        UUID id = UUID.randomUUID();
        when(repository.remove(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,
                () -> service.removePlace(id));

        verify(repository).remove(id);
    }

    @Test
    void removePlaceRejectsNullWithoutCallingRepository() {
        assertThrows(IllegalArgumentException.class,
                () -> service.removePlace(null));

        verifyNoInteractions(repository);
    }
}