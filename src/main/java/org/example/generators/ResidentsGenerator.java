package org.example.generators;

import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.limits.Limits;
import org.example.entities.map.Cell;
import org.example.entities.plants.Plant;
import org.example.factory.OrganismFactory;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ResidentsGenerator {
    private final OrganismFactory organismFactory;

    public ResidentsGenerator() {
        organismFactory = OrganismFactory.getInstance();
    }

    public Map<Class<? extends GameObject>, List<GameObject>> generateNewResidence(Cell[][] cells) {
        Map<Class<? extends GameObject>, List<GameObject>> residents = new ConcurrentHashMap<>();
        Map<Class<? extends GameObject>, GameObject> prototypes = organismFactory.getPrototypes();

        for (Cell[] cell : cells) {
            for (Cell value : cell) {
                fillCells(prototypes, residents, value);
            }
        }

        return residents;
    }

    private void fillCells(Map<Class<? extends GameObject>, GameObject> prototypes, Map<Class<? extends GameObject>, List<GameObject>> residents, Cell cell) {
        Random randomAmount = new Random();

        int maxCount = 0;
        int count;

        for (Class<? extends GameObject> prototype : prototypes.keySet()) {
            List<GameObject> organisms = new CopyOnWriteArrayList<>();
            GameObject gameObjectPrototypeInstance = prototypes.get(prototype);

            maxCount = getMaxCount(gameObjectPrototypeInstance, maxCount);
            count = randomAmount.nextInt(maxCount + 1);

            for (int i = 0; i < count; i++) {
                organisms.add(prototypes.get(prototype).copy());
            }

            initializeCells(organisms, cell);
            residents.put(prototype, organisms);
            cell.setResidents((ConcurrentHashMap<Class<? extends GameObject>, List<GameObject>>) residents);
        }
    }

    private int getMaxCount(GameObject gameObjectPrototypeInstance, int maxCount) {
        Limits limits = null;

        if (gameObjectPrototypeInstance instanceof Animal animal) {
            limits = animal.getLimits();
        } else if (gameObjectPrototypeInstance instanceof Plant plant) {
            maxCount = plant.getMaxAmount();
        }

        if (limits != null) {
            maxCount = limits.getMaxAmount();
        }
        return maxCount;
    }

    private void initializeCells(List<GameObject> organisms, Cell cell) {
        for (GameObject gameObject : organisms) {
            gameObject.setCell(cell);
        }
    }
}
