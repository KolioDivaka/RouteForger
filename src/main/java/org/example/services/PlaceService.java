package org.example.services;

import org.example.models.Place;

import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class PlaceService {

    private  final PlaceRepository placeRepository ;
    public PlaceService(PlaceRepository placeRepository){
        this.placeRepository =  Objects.requireNonNull(placeRepository, "placeRepository");
    }



    public void addPlace(Place place) throws SQLException {
        if (place == null) {
            throw new IllegalArgumentException("Place cannot be null!");
        }

        placeRepository.addPlace(place);
    }

    public List<Place> getAllPlaces() throws SQLException {
        return placeRepository.getAllPlaces();
    }

    public List<Place> searchPlaces(String query) throws SQLException {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Search cannot be empty!");
        }

        String needle = query.trim().toLowerCase(Locale.ROOT);

        return placeRepository.getAllPlaces().stream()
                .filter(place -> place.getName()
                        .toLowerCase(Locale.ROOT)
                        .contains(needle))
                .toList();
    }

    public void removePlace (UUID id) throws SQLException{

        boolean remove = placeRepository.remove(id);

        if (!remove){
            throw new IllegalArgumentException("No place found with id : "+ id);
        }
    }
}
