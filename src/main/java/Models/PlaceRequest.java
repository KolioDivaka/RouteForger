package Models;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

public record PlaceRequest(
        int availableMinutes,
        BigDecimal budgetEur,
        Set<String> interest
) {
    public  PlaceRequest{
        if (availableMinutes <=0){
            throw new IllegalArgumentException("Available minutes should be positive!");
        }
        Objects.requireNonNull(budgetEur, "Budget cannot be null!");

        if(budgetEur.signum()<=0){
            throw new IllegalArgumentException("Budget cannot be negative!");
        }
        interest= Set.copyOf(Objects.requireNonNull(interest, "Interest cannot be null"));
    }

}
