package org.example.entities.config;

import lombok.Getter;
import org.example.entities.map.InteractableCell;

public abstract class EntitySettings {

    @Getter
    private String icon;
    private final boolean isAlive;
    private final double weight;
    private final int health;
    private final int age;
    private final InteractableCell cell;

    protected EntitySettings(boolean isAlive, double weight, int health, int age, InteractableCell cell) {
        this.cell = cell;
        this.isAlive = isAlive;
        this.weight = weight;
        this.health = health;
        this.age = age;
    }

    public boolean isAlive() {
        return isAlive;
    }
}
