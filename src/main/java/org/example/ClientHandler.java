package org.example;

import org.example.models.Category;
import org.example.models.Itinerary;
import org.example.models.Place;
import org.example.models.PlanRequest;
import org.example.services.GreedyPlanner;
import org.example.services.PlaceService;

import java.math.BigDecimal;
import java.util.*;

public class ClientHandler {
    private final PlaceService placeService;
    private final GreedyPlanner planner;

    public ClientHandler(PlaceService placeService, GreedyPlanner planner)
    {
        this.placeService = placeService;
        this.planner=planner;
    }

    public void run() {
        while (true) {
            IO.println("\n=== ROUTE FORGER ===");
            IO.println("1. List places");
            IO.println("2. Add place");
            IO.println("3. Remove place");
            IO.println("4. Generate itinerary");
            IO.println("0. Exit");
            IO.println("Choose an option:");

            String choice = IO.readln().trim();

            switch (choice) {
                case "1" -> listPlaces();
                case "2" -> addPlace();
                case "3" -> removePlace();
                case "4" -> generateItinerary();
                case "5" -> searchPlaceByName();
                case "0" -> {
                    IO.println("Goodbye!");
                    return;
                }
                default -> IO.println("Unknown option. Choose 0–4.");
            }
        }
    }
    public void listPlaces(){
        List<Place> places = placeService.getAllPlaces();

        if(places==null || places.isEmpty()) {
           IO.println("No places found!");
           return;
        }

        int num = 1;
        for(Place p : places){
            IO.println(num+". "+ p);
        }
    }

    public void searchPlaceByName(){
        String name = IO.readln();
        if(name.isEmpty()){
            IO.println("Search Cancelled!!!");
            return;
        }


        List<Place> foundPlaces = placeService.searchByName(name);

        if(foundPlaces.isEmpty()){
            IO.println("No matches found!");
        }

    }

    public void addPlace(){
        try{
           IO.println("Name: ");
           String name = IO.readln();

           IO.println("Duration in minutes: ");
           int duration = Integer.parseInt(IO.readln().trim());


           IO.println("Enter price(EUR): ");
            BigDecimal price = new BigDecimal(IO.readln().trim());

            IO.println("Enter interest: ");
            String s = IO.readln();
            Set<String> tags = parseTags(s);

            Category category = chooseCategory();

            Place place = new Place(
                    UUID.randomUUID(),
                    name,
                    category,
                    duration,
                    price,
                    tags
            );

            placeService.addPlace(place);
            IO.println("Place added: " + place.getName());
        }catch (NumberFormatException e){
          IO.println("Please enter valid numbers for duration and price.");
        }
        catch (IllegalArgumentException e){
            IO.println("Could not add place: " + e.getMessage());
        }
    }

    public void removePlace() {
        List<Place> places = placeService.getAllPlaces();

        if (places.isEmpty()) {
            IO.println("There are no places to delete.");
            return;
        }

        for (int i = 0; i < places.size(); i++) {
            IO.println((i + 1) + ". " + places.get(i));
        }

        IO.println("Choose a place to delete (1-" + places.size() + "):");

        try {
            int choice = Integer.parseInt(IO.readln().trim());

            if (choice < 1 || choice > places.size()) {
                IO.println("Choose a number from 1 to " + places.size() + ".");
                return;
            }

            Place selected = places.get(choice - 1);
            placeService.removePlace(selected.getId());
            IO.println("Deleted: " + selected.getName());

        } catch (NumberFormatException e) {
            IO.println("Please enter a whole number.");
        } catch (IllegalArgumentException e) {
            IO.println("Could not delete place: " + e.getMessage());
        }
    }

    public void generateItinerary(){
        try {
            PlanRequest planRequest = makePlanRequest();
            List<Place> places = placeService.getAllPlaces();
            Itinerary itinerary = planner.plan(planRequest, places);

            IO.println(itinerary);

        } catch (NumberFormatException e) {
            IO.println("Enter a valid whole number for minutes and a decimal number for EUR.");
        } catch (IllegalArgumentException e) {
            IO.println("Invalid plan request: " + e.getMessage());
        }
    }

    private Set<String> parseTags(String input) {
        Set<String> tags = new HashSet<>();

        for (String part : input.split(",")) {
            String tag = part.trim().toLowerCase(Locale.ROOT);

            if (!tag.isEmpty()) {
                tags.add(tag);
            }
        }

        return tags;
    }


    private Category chooseCategory() {
        Category[] categories = Category.values();

        while (true) {
            IO.println("Categories:");
            for (int i = 0; i < categories.length; i++) {
                IO.println((i + 1) + ". " + categories[i].name());
            }

            IO.print("Choose a category: ");

            try {
                int choice = Integer.parseInt(IO.readln().trim());

                if (choice < 1 || choice > categories.length) {
                    IO.println("Choose a number from 1 to " + categories.length + ".");
                    continue;
                }

                return categories[choice - 1];

            } catch (NumberFormatException e) {
                IO.println("Please enter a whole number.");
            }
        }
    }

    private PlanRequest makePlanRequest() {
        IO.println("How much time do you have available (minutes)?");
        int availableTime = Integer.parseInt(IO.readln().trim());

        IO.println("What is your budget (EUR)?");
        BigDecimal budget = new BigDecimal(IO.readln().trim());

        IO.println("Enter your interests, separated by commas:");
        Set<String> interests = parseTags(IO.readln());

        return new PlanRequest(availableTime, budget, interests);
    }



}

