package org.example.services;

import org.example.database.Database;
import org.example.models.Category;
import org.example.models.Place;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class PlaceRepository {

         private final Database database;

         public PlaceRepository(Database database){
             this.database = database;
         }

         public List<Place> getAllPlaces() throws  SQLException{
             String sql = """
                     SELECT id,name,category,duration_minutes, price_cents
                     FROM places
                     """;
             List<Place> places = new ArrayList<>();

             try (Connection connection= database.getConnection();
                  Statement stm = connection.createStatement();
                  ResultSet rs = stm.executeQuery(sql)){
                  while (rs.next()){
                      UUID id = UUID.fromString(rs.getString("id"));

                      Category category = Category.valueOf(rs.getString("category"));

                      long priceCents = rs.getLong("price_cents");
                      BigDecimal price = BigDecimal.valueOf(priceCents, 2);
                      Place place = new Place(
                              id,
                              rs.getString("name"),
                              category,
                              rs.getInt("duration_minutes"),
                              price,
                              new HashSet<>()
                      );

                      loadTags(connection, place);

                      places.add(place);
                  }
                 return places;

             }
         }

         public void addPlace(Place place) throws SQLException{
             String insertPlace = """
                     INSERT INTO places(id,name, category, duration_minutes, price_cents)
                     VALUES (?,?,?,?,?)
                     """;

             String insertTag= """
                     INSERT INTO place_tags (place_id, tag)
                                 VALUES (?, ?)
                     """;


             try (Connection connection = database.getConnection()){
                 connection.setAutoCommit(false);
                 try(PreparedStatement placeStm = connection.prepareStatement(insertPlace);
                 PreparedStatement tagStm = connection.prepareStatement(insertTag)){
                     String id =place.getId().toString();

                     placeStm.setString(1,id);
                     placeStm.setString(2, place.getName());
                     placeStm.setString(3,place.getCategory().name());
                     placeStm.setInt(4,place.getDurationMinutes());
                     placeStm.setLong(5, place.getPriceEur()
                             .movePointRight(2)
                             .longValueExact());

                     placeStm.executeUpdate();

                     for(String tag : place.getTags()){
                         tagStm.setString(1,id);
                         tagStm.setString(2,tag);
                         tagStm.executeUpdate();
                     }

                     connection.commit();

                 } catch (SQLException | RuntimeException e) {
                     connection.rollback();
                     throw e;
                 }
             }
         }

         public boolean remove(UUID id) throws SQLException{
             if(id == null){
                 throw new IllegalArgumentException("Place ID cannot be NULL");
             }
             String deleteTags = "DELETE FROM place_tags WHERE place_id = ?";
             String deletePlace = "DELETE FROM places WHERE id = ?";

             try(Connection connection = database.getConnection()){
                 connection.setAutoCommit(false);

                 try (PreparedStatement tagStm = connection.prepareStatement(deleteTags);
                 PreparedStatement placeStm = connection.prepareStatement(deletePlace)){

                     String idText  = id.toString();

                     tagStm.setString(1,idText);
                     tagStm.executeUpdate();

                     placeStm.setString(1,idText);
                     int deletedPlace = placeStm.executeUpdate();

                     connection.commit();
                     return deletedPlace>0;

                 }
                 catch (SQLException e){
                     connection.rollback();
                     throw e;
                 }
             }
         }

        private void loadTags(Connection connection, Place place)
                throws SQLException {

            String sql = """
                SELECT tag
                FROM place_tags
                WHERE place_id = ?
                """;

            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, place.getId().toString());

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        place.getTags().add(rs.getString("tag"));
                    }
                }
            }
    }
}
