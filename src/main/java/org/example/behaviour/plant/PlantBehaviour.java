package org.example.behaviour.plant;

import org.example.behaviour.generalBehaviorStatements.ReproduceBehavior;
import org.example.entities.map.Cell;
import org.example.entities.plants.Plant;
import org.example.statistic.interfaces.Observer;
import org.example.statistic.interfaces.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlantBehaviour implements Subject {
    List<Observer> observers = new ArrayList<>();
    private final ReproduceBehavior reproduceBehavior;

    public PlantBehaviour() {
        this.reproduceBehavior = new ReproduceBehavior();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {

    }

    public void lifeCycle(Cell cell) {
        processPlants(cell, plant -> {
            synchronized (plant) {
                plant.decreaseHealthOverTime();
                if (plant.getHealth() <= 5) {
                    plant.isDeath();
                }
            }
        });
    }

    public void grow(Cell cell) {
        processPlants(cell, plant -> {
            synchronized (plant) {
                reproduceBehavior.reproduce(plant);
            }
        });
    }

    private void processPlants(Cell cell, Consumer<Plant> action) {
        cell.getResidents().forEach((key, value) -> {
            if (Plant.class.isAssignableFrom(key)) {
                Plant plant = (Plant) value.stream()
                        .filter(gameObject1 -> gameObject1 instanceof Plant).findFirst().get();
                action.accept(plant);
            }
        });
    }
}
