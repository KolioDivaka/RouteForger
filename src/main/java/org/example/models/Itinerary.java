package org.example.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class Itinerary {
    private final List<Place> places;
    private final int totalMinutes;
    private final BigDecimal totalPrice;

    public Itinerary(List<Place> places){
        this.places = List.copyOf(
                Objects.requireNonNull(places, "Places cannot be null")
        );
        this.totalMinutes=this.places.stream()
                .mapToInt(Place::getDurationMinutes).sum();

        this.totalPrice= this.places.stream()
                .map(Place::getPriceEur)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    public List<Place> getPlaces() {
        return places;
    }

    public int getTotalMinutes() {
        return totalMinutes;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }
    @Override
    public String toString() {
        if (places.isEmpty()) {
            return "No places fit your time and budget.";
        }

        StringBuilder result = new StringBuilder("Your itinerary:\n");

        for (int i = 0; i < places.size(); i++) {
            result.append(i + 1)
                    .append(". ")
                    .append(places.get(i))
                    .append('\n');
        }

        result.append("------------------------------\n")
                .append("Total time: ")
                .append(totalMinutes)
                .append(" min\n")
                .append("Total cost: ")
                .append(totalPrice)
                .append(" EUR");

        return result.toString();
    }
}
