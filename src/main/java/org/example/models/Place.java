package org.example.models;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public class Place {

    UUID id;
    String name;
    Category category;
    int durationMinutes;
    BigDecimal priceEur;
    Set<String> tags;

    public Place(UUID id, String name, Category category, int durationMinutes, BigDecimal priceEur, Set<String> tags) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.durationMinutes = durationMinutes;
        this.priceEur = priceEur;
        this.tags = tags;
    }

    public UUID getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }


    public Set<String> getTags() {
        return tags;
    }


    public BigDecimal getPriceEur() {
        return priceEur;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    @Override
    public String toString() {
        return "%s [%s] — %d min | %s EUR | %s"
                .formatted(
                        name,
                        category,
                        durationMinutes,
                        priceEur,
                        String.join(", ", tags)
                );
    }
}
