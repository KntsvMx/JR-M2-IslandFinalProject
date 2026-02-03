package org.example.behaviour.plant;

import org.example.behaviour.generalBehaviorStatements.ReproduceBehavior;
import org.example.entities.map.Cell;
import org.example.entities.plants.Plant;
import org.example.statistic.AbstractSubject;
import org.example.statistic.interfaces.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlantBehaviour extends AbstractSubject {
    List<Observer> observers = new ArrayList<>();
    private final ReproduceBehavior reproduceBehavior;

    public PlantBehaviour() {
        this.reproduceBehavior = new ReproduceBehavior();
    }


    public void grow(Cell cell) {
        processPlants(cell, plant -> {
            reproduceBehavior.reproduce(plant, cell);
        });
    }

    private void processPlants(Cell cell, Consumer<Plant> action) {
        cell.getResidents().forEach((key, value) -> {
            if (Plant.class.isAssignableFrom(key)) {
                value.stream()
                        .filter(obj -> obj instanceof Plant)
                        .map(obj -> (Plant) obj)
                        .findFirst()
                        .ifPresent(action);
            }
        });
    }
}
