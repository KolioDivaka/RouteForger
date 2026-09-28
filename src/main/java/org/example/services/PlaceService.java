package org.example.services;

import org.example.models.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlaceService {
    private final List<Place> places = new ArrayList<>();

    public void addPlace(Place place){
        if(place == null){
            throw new IllegalArgumentException("Place cannot be null!");
        }
        if(places.stream().noneMatch(p->p.getId().equals(place.getId()))){
            places.add(place);
        }
        else {
            throw new IllegalArgumentException("Place with this id already exist!");
        }
    }

    public List<Place> getAllPlaces(){
        return List.copyOf(places);
    }

    public Place getById(UUID id){
        return places.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Place> searchByName(String name){
        String normalize = name.toLowerCase().trim();

        return places.stream().filter(p->p.getName().toLowerCase().contains(normalize)).toList();
    }

    public void removePlace (UUID id){
        boolean remove = places.removeIf(p -> p.getId().equals(id));

        if (!remove){
            throw new IllegalArgumentException("No place found with id : "+ id);
        }
    }
}
