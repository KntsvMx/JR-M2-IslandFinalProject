package org.example.generators;

import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.Cell;
import org.example.entities.plants.Plant;
import org.example.factory.OrganismFactory;
import org.example.statistic.AbstractSubject;
import org.example.statistic.interfaces.StatsType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ResidentsGenerator extends AbstractSubject {
    private final OrganismFactory organismFactory;

    public ResidentsGenerator() {
        organismFactory = OrganismFactory.getInstance();
    }

    public void generateNewResidence(Cell[][] cells) {
        for (Cell[] cell : cells) {
            for (Cell value : cell) {
                fillCells(value);
            }
        }
    }

    private void fillCells(Cell cell) {
        HashMap<Class<? extends GameObject>, List<GameObject>> residents = new HashMap<>();
        Map<Class<? extends GameObject>, GameObject> prototypes = organismFactory.getPrototypes();

        int maxCapacity = 0;
        int amountToGenerate;

        for (Class<? extends GameObject> prototype : prototypes.keySet()) {
            List<GameObject> organisms = new CopyOnWriteArrayList<>();
            GameObject gameObjectPrototypeInstance = prototypes.get(prototype);

            maxCapacity = getMaxCount(gameObjectPrototypeInstance);
            amountToGenerate = ThreadLocalRandom.current().nextInt(maxCapacity + 1);

            for (int i = 0; i < amountToGenerate; i++) {
                organisms.add(gameObjectPrototypeInstance.reproduce());

                collectStatistic(gameObjectPrototypeInstance);
            }

            initializeCells(organisms, cell);
            residents.put(prototype, organisms);
        }
        cell.setResidents(residents);
    }

    //    TODO: Probably it would be better to create additional class for statistics methods.
    private void collectStatistic(GameObject gameObjectPrototypeInstance) {
        if (gameObjectPrototypeInstance instanceof Animal) {
            notifyObservers(StatsType.CURRENT_ANIMALS, 1);
        } else if (gameObjectPrototypeInstance instanceof Plant) {
            notifyObservers(StatsType.CURRENT_PLANTS, 1);
        }
    }

    private int getMaxCount(GameObject gameObjectPrototypeInstance) {
        int maxAmountPerCell = 0;
        if (gameObjectPrototypeInstance instanceof Animal animal && animal.getLimits() != null) {
            maxAmountPerCell = animal.getLimits().getMaxAmount();
        } else if (gameObjectPrototypeInstance instanceof Plant plant) {
            maxAmountPerCell = plant.getMaxAmount();
        }
        return maxAmountPerCell;
    }

    private void initializeCells(List<GameObject> organisms, Cell cell) {
        for (GameObject gameObject : organisms) {
            gameObject.setCell(cell);
        }
    }
}
