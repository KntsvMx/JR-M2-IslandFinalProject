package org.example.entities.config.plantEntitySettings;

import lombok.Getter;
import org.example.entities.config.EntitySettings;
import org.example.entities.map.InteractableCell;

@Getter
public class PlantEntitySettings extends EntitySettings {

    private final int maxAmountPerCell;


    protected PlantEntitySettings(boolean isAlive, double weight, int health, int age, InteractableCell cell, int maxAmountPerCell) {
        super(isAlive, weight, health, age, cell);
        this.maxAmountPerCell = maxAmountPerCell;
    }
}
