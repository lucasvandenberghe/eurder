package org.switchfully.eurder.domain;

import org.switchfully.eurder.domain.exceptions.ItemException;

import java.util.Objects;
import java.util.UUID;

public class Item {
    private final UUID id;
    private final String name;
    private final String description;
    private final double price;
    private final int amount;

    public Item(String name, String description, double price, int amount) {
        if (name.isBlank())
            throw new ItemException("Please provide a non-blank name.");
        if (description.isBlank())
            throw new ItemException("Please provide a non-blank description.");
        if (price < 0)
            throw new ItemException("Please provide a non-negative price.");
        if (amount < 0)
            throw new ItemException("Please provide a non-negative amount.");

        id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
