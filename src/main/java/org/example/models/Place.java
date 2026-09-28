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

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setPriceEur(BigDecimal priceEur) {
        this.priceEur = priceEur;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
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
