package Models;

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
}
