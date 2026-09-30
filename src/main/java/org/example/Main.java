package org.example;

import org.example.database.Database;
import org.example.database.DatabaseInitializer;
import org.example.services.GreedyPlanner;
import org.example.services.PlaceRepository;
import org.example.services.PlaceService;

import java.sql.SQLException;

public class Main {

     static void main() {
        Database database = new Database();
        PlaceRepository repository = new PlaceRepository(database);
        PlaceService placeService = new PlaceService(repository);

         try {
             new DatabaseInitializer(database).initialize();
         } catch (SQLException e) {
             System.err.println("Could not initialize database: " + e.getMessage());
             return;
         }
        GreedyPlanner planner = new GreedyPlanner();
        new ClientHandler(placeService, planner).run();
    }
}