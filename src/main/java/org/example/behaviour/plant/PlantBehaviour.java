package org.example.behaviour.plant;

import org.example.behaviour.generalBehaviorStatements.ReproduceBehavior;
import org.example.entities.map.InteractableCell;
import org.example.entities.plants.Grass;
import org.example.entities.plants.Plant;
import org.example.managers.DeathManager;
import org.example.statistic.AbstractSubject;

import java.util.function.Consumer;

public class PlantBehaviour extends AbstractSubject {
    private final ReproduceBehavior reproduceBehavior;

    public PlantBehaviour(ReproduceBehavior reproduceBehavior) {
        this.reproduceBehavior = reproduceBehavior;
    }


    public void grow(InteractableCell cell) {
        processPlants(cell, plant -> {
            reproduceBehavior.reproduce(plant, cell);
            if (plant.decreaseHealthOverTime()) {
                DeathManager.getInstant().registerDeath(plant, cell);
            }
        });
    }

    private void processPlants(InteractableCell cell, Consumer<Plant> action) {
//      TODO: Refactor this to be more efficient, and to avoid potential ConcurrentModificationException + implement exception handling
        var grasses = cell.getResidents().get(Grass.class);

        if (grasses != null && !grasses.isEmpty()) {

            Plant grass = (Plant) grasses.get(0);

            action.accept(grass);
        }

    }
}
