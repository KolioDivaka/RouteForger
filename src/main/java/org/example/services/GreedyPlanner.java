package org.example.services;

import org.example.models.Itinerary;
import org.example.models.Place;
import org.example.models.PlanRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GreedyPlanner {
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    public Itinerary plan(PlanRequest request, List<Place> places){
         List<Place> remainingPlaces = new ArrayList<>(places);
         List<Place> selectedPlaces =new ArrayList<>();

         int remainingMinutes = request.availableMinutes();
        BigDecimal remainingMoney = request.budgetEur();

        while(true){
          Place bestPlace = null;
          int bestScore = -1;

          for(Place place: remainingPlaces){

              boolean fitsTime = place.getDurationMinutes()<=remainingMinutes;
              boolean fitsBudget = place.getPriceEur().compareTo(remainingMoney)<=0;

              if(!fitsTime || !fitsBudget){
                  continue;
              }

              int score = countMatchingTags(place.getTags(),request.interest());
               if(bestPlace == null || isBetter( place,score,bestPlace,bestScore)){
                   bestPlace=place;
                   bestScore=score;
               }


          }
          if(bestPlace==null){
              break;
          }
          selectedPlaces.add(bestPlace);
          remainingPlaces.remove(bestPlace);

          remainingMinutes-=bestPlace.getDurationMinutes();
          remainingMoney = remainingMoney.subtract(bestPlace.getPriceEur());



        }
        return  new Itinerary(selectedPlaces);

    }
    private  int countMatchingTags(Set<String> tags, Set<String> interests){
        int matches = 0;
        for(String tag : tags){
            if(interests.contains(tag)){
                matches++;
            }
        }
        return matches;
    }
    private boolean isBetter(
            Place candidate,
            int candidateScore,
            Place currentBest,
            int currentBestScore
    ) {
        if (candidateScore != currentBestScore) {
            return candidateScore > currentBestScore;
        }

        int priceComparison =
                candidate.getPriceEur().compareTo(currentBest.getPriceEur());

        if (priceComparison != 0) {
            return priceComparison < 0;
        }

        if (candidate.getDurationMinutes()
                != currentBest.getDurationMinutes()) {
            return candidate.getDurationMinutes()
                    < currentBest.getDurationMinutes();
        }

        return candidate.getName()
                .compareToIgnoreCase(currentBest.getName()) < 0;
    }
}
