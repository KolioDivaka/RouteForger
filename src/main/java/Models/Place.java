package Models;

import java.math.BigDecimal;
import java.util.Set;

public class Place {

    int id;
    String name;
    Category category;
    int durationMinutes;
    BigDecimal priceEur;
    Set<String> tags;

    public Place(int id, String name, Category category, int durationMinutes, BigDecimal priceEur, Set<String> tags) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.durationMinutes = durationMinutes;
        this.priceEur = priceEur;
        this.tags = tags;
    }

    public BigDecimal getPriceEur() {
        return priceEur;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", durationMinutes=" + durationMinutes +
                ", priceEur=" + priceEur +
                ", tags=" + tags +
                '}';
    }
}
