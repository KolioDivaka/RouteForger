package org.example;

import org.example.models.Category;
import org.example.models.Itinerary;
import org.example.models.Place;
import org.example.models.PlanRequest;
import org.example.services.GreedyPlanner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        Place museum = new Place( UUID.randomUUID(),"History Museum"
                , Category.MUSEUM,90
                ,new BigDecimal("12.00")
                , Set.of("history","indoor")
        );
        Place gallery = new Place(
                UUID.randomUUID(),
                "Art Gallery",
                Category.LANDMARK,
                75,
                new BigDecimal("10.00"),
                Set.of("art", "indoor")
        );
        PlanRequest request = new PlanRequest(
                180,
                new BigDecimal("30.00"),
                Set.of("history", "art")
        );

        List<Place> places = new ArrayList<>();
        places.add(museum);
        places.add(gallery);

        Itinerary itinerary =new GreedyPlanner().plan(request, places);

        IO.println("Stops: " + itinerary.getPlaces().size());
        IO.println("Total minutes: "+ itinerary.getTotalMinutes());
        IO.println("Total price: "+ itinerary.getTotalPrice());

    }
}
