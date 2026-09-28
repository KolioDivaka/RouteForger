package org.example.services;

import org.example.models.Category;
import org.example.models.Itinerary;
import org.example.models.Place;
import org.example.models.PlanRequest;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class GreedyPlannerTest {

    private final GreedyPlanner planner = new GreedyPlanner();

    private Place place(String name, int minutes, String price, String... tags) {
        return new Place(
                UUID.randomUUID(),
                name,
                Category.MUSEUM,
                minutes,
                new BigDecimal(price),
                Set.of(tags)
        );
    }

    @Test
    void returnsEmptyItineraryWhenThereAreNoPlaces() {
        PlanRequest request = new PlanRequest(
                120, new BigDecimal("20.00"), Set.of("history")
        );

        Itinerary result = planner.plan(request, List.of());

        assertTrue(result.getPlaces().isEmpty());
        assertEquals(0, result.getTotalMinutes());
        assertEquals(0, result.getTotalPrice().compareTo(BigDecimal.ZERO));
    }

    @Test
    void includesPlaceThatExactlyFitsTimeAndBudget() {
        Place museum = place("Museum", 90, "12.00", "history");
        PlanRequest request = new PlanRequest(
                90, new BigDecimal("12.00"), Set.of("history")
        );

        Itinerary result = planner.plan(request, List.of(museum));

        assertEquals(List.of(museum), result.getPlaces());
        assertEquals(90, result.getTotalMinutes());
        assertEquals(0,
                result.getTotalPrice().compareTo(new BigDecimal("12.00")));
    }

    @Test
    void choosesHigherInterestScoreFirst() {
        Place gallery = place("Gallery", 60, "10.00", "art");
        Place museum = place("Museum", 60, "10.00", "art", "history");

        PlanRequest request = new PlanRequest(
                120, new BigDecimal("20.00"), Set.of("art", "history")
        );

        Itinerary result = planner.plan(request, List.of(gallery, museum));

        assertEquals(List.of(museum, gallery), result.getPlaces());
    }

    @Test
    void neverExceedsTimeOrBudget() {
        Place museum = place("Museum", 90, "15.00", "history");
        Place gallery = place("Gallery", 60, "12.00", "art");
        Place park = place("Park", 150, "30.00", "nature");

        PlanRequest request = new PlanRequest(
                120, new BigDecimal("20.00"), Set.of("history", "art")
        );

        Itinerary result = planner.plan(
                request, List.of(museum, gallery, park)
        );

        assertTrue(result.getTotalMinutes() <= request.availableMinutes());
        assertTrue(result.getTotalPrice()
                .compareTo(request.budgetEur()) <= 0);
        assertEquals(List.of(gallery), result.getPlaces());
    }

    @Test
    void choosesCheaperPlaceWhenScoresTie() {
        Place expensive = place("Expensive", 60, "15.00", "art");
        Place cheap = place("Cheap", 60, "10.00", "art");

        PlanRequest request = new PlanRequest(
                60, new BigDecimal("20.00"), Set.of("art")
        );

        Itinerary result = planner.plan(
                request, List.of(expensive, cheap)
        );

        assertEquals(List.of(cheap), result.getPlaces());
    }
    @Test
    void doesNotSelectTheSamePlaceTwice() {
        Place museum = place("Museum", 30, "5.00", "history");

        PlanRequest request = new PlanRequest(
                120,
                new BigDecimal("20.00"),
                Set.of("history")
        );

        Itinerary result = planner.plan(request, List.of(museum));

        assertEquals(List.of(museum), result.getPlaces());
    }
}