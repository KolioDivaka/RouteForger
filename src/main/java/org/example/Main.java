package org.example;

import org.example.database.Database;
import org.example.database.DatabaseInitializer;
import org.example.models.Category;
import org.example.models.Place;
import org.example.services.GreedyPlanner;
import org.example.services.PlaceService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {

        Database database = new Database();

        try {
            DatabaseInitializer initializer =
                    new DatabaseInitializer(database);

            initializer.initialize();

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.out.println("Could not initialize database.");
            e.printStackTrace();
            return;
        }



        PlaceService placeService = new PlaceService();
        GreedyPlanner planner = new GreedyPlanner();

        seedPlaces(placeService);

        ClientHandler clientHandler = new ClientHandler(placeService, planner);
        clientHandler.run();


    }

    private static void seedPlaces(PlaceService placeService) {
        if (!placeService.getAllPlaces().isEmpty()) {
            return;
        }

        placeService.addPlace(new Place(
                UUID.randomUUID(),
                "History Museum",
                Category.MUSEUM,
                90,
                new BigDecimal("12.00"),
                Set.of("history", "indoor")
        ));

        placeService.addPlace(new Place(
                UUID.randomUUID(),
                "City Park",
                Category.PARK,
                60,
                new BigDecimal("0.00"),
                Set.of("nature", "outdoor")
        ));
    }
}
