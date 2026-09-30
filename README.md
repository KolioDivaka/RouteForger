# RouteForger

RouteForger is a Java console application for building a simple itinerary from a collection of places. You can manage places in a SQLite database, then generate an itinerary based on your available time, budget, and interests.

## Features

- List, add, remove, and search places by name.
- Store places and their tags in SQLite so changes persist between runs.
- Give each place a category, visit duration, price in EUR, and set of tags.
- Generate an itinerary using a greedy planning approach.

## Tech stack

- Java
- SQLite and JDBC
- JUnit 5 for testing
- Mockito for service unit tests

## Getting started

1. Clone the repository and open it in IntelliJ IDEA.
2. Let IntelliJ import the project and resolve its dependencies.
3. Run `org.example.Main`.
4. Use the numbered console menu to manage places or generate an itinerary.

The application initializes its database tables at startup. It uses a SQLite file named `routeforger.db` in the application's working directory. If the repository includes a prepared demo database, it will contain sample places; otherwise, you can add places through the menu.

## How planning works

Enter the time you have available, your budget in EUR, and interests separated by commas. RouteForger passes the request and the stored places to `GreedyPlanner`, which produces an itinerary with selected places, total time, and total price.

## Project structure

- `models` — places, categories, plan requests, and itineraries.
- `database` — SQLite connections and table initialization.
- `services/PlaceRepository` — reads and writes places and tags.
- `services/PlaceService` — place validation and name search.
- `services/GreedyPlanner` — itinerary generation.
- `ClientHandler` — console menu and input handling.

## Tests

Run the tests from IntelliJ's test runner. Repository tests use a separate temporary SQLite database; service unit tests mock the repository.

## Notes

RouteForger stores prices as integer cents in SQLite and uses `BigDecimal` for EUR amounts in Java. Place IDs use UUIDs, and categories use a Java enum.
