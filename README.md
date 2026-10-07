# RouteForger

RouteForger is a Java console application for managing places and generating simple itineraries. It stores places in a SQLite database, allowing data to persist between application runs.

## Features

- List saved places
- Add a place with a name, category, visit duration, price, and tags
- Remove a place from the catalog
- Search places by a partial name without case sensitivity
- Generate an itinerary from available time, budget, and interests
- Persist places and tags with SQLite and JDBC

## Architecture

RouteForger is a layered Java console application:

- **ClientHandler** — displays the console menu, reads user input, and prints results
- **PlaceService** — validates place operations and provides name searching
- **PlaceRepository** — handles SQLite/JDBC queries for adding, reading, and removing places
- **GreedyPlanner** — creates an itinerary from a `PlanRequest` and the available places
- **Database / DatabaseInitializer** — opens the SQLite connection and creates the database schema

This project is not a microservices application. Its service classes are parts of one Java application, with clear separation of responsibilities.

## Data model

Each place contains:

- A UUID identifier
- A name
- A `Category` enum value
- Visit duration in minutes
- A price represented as `BigDecimal` in Java
- A set of interest tags

SQLite stores prices as integer cents to avoid floating-point precision issues. Tags are stored in a separate `place_tags` table linked to `places`.

## Technologies

- Java
- SQLite
- JDBC
- JUnit 5
- Mockito
- Maven

## Getting started

1. Clone the repository:

   ```bash
   git clone https://github.com/<your-username>/RouteForger.git
   ```

2. Open the project in IntelliJ IDEA.

3. Allow Maven to download and resolve project dependencies.

4. Run `org.example.Main`.

5. Use the menu displayed in the console.

At startup, RouteForger initializes the SQLite schema if it does not already exist. The application uses a local SQLite database file named `routeforger.db` in the working directory.

## Menu options

```text
1. List places
2. Add place
3. Remove place
4. Generate itinerary
5. Search places by name
0. Exit
```

## Testing

The project includes two categories of tests:

- **Repository integration tests** use a temporary SQLite database to test inserts, reads, tag mapping, and deletion.
- **Service unit tests** use Mockito to mock `PlaceRepository` and test service-level validation, searching, and delegation independently from SQLite.

Run the tests through IntelliJ IDEA or with Maven:

```bash
mvn test
```

## Notes

- Database-related files such as SQLite WAL and shared-memory files should normally be excluded from version control.
- A prepared demo database can be committed for demonstration purposes, but do not include private or sensitive data.
