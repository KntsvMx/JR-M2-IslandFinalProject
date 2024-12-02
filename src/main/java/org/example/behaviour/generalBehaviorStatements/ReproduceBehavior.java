package org.example.behaviour.generalBehaviorStatements;

import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.InteractableCell;
import org.example.entities.plants.Plant;
import org.example.factory.OrganismFactory;
import org.example.managers.CellManager;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ReproduceBehavior {
    private final CellManager cellManager;
    private final ReentrantLock lock = new ReentrantLock();
    private final OrganismFactory organismFactory = OrganismFactory.getInstance();

    public ReproduceBehavior() {
        cellManager = CellManager.getInstance();
    }

    public void reproduce(GameObject gameObject) {
        lock.lock();
        InteractableCell currentCell = getInteractableCell(gameObject);

        if (currentCell != null) {
            currentCell.getLock().lock();
        }

        try {
            if (gameObject instanceof Animal animal) {
                reproduceAnimal(animal, currentCell);
            } else if (gameObject instanceof Plant plantGameObject) {
                reproducePlant(plantGameObject, currentCell);
            }
        } finally {
            currentCell.getLock().unlock();
            lock.unlock();
        }
    }

    private void reproduceAnimal(Animal animal, InteractableCell currentCell) {
        Animal sameSpecie = getSameSpecie(animal);
        if (canReproduce(animal, currentCell, sameSpecie)) {
            GameObject newAnimal = organismFactory.create(animal.getClass());
            cellManager.addGameObject(currentCell, newAnimal);
        } else {
            throw new IllegalArgumentException("Species not available");
        }
    }


    private void reproducePlant(Plant plantGameObject, InteractableCell currentCell) {
        if (availableSpaceForSpecie(currentCell, plantGameObject.getMaxAmount())) {
            GameObject newPlant = organismFactory.create(plantGameObject.getClass());
            cellManager.addGameObject(currentCell, newPlant);
        } else {
            throw new IllegalArgumentException("Plants not available");
        }
    }

    private boolean canReproduce(Animal animal, InteractableCell currentCell, Animal sameSpecie) {
        return isEnoughHealth(sameSpecie, animal) && sameSpecie != null && availableSpaceForSpecie(currentCell, animal.getLimits().getMaxAmount());
    }

    private static @Nullable InteractableCell getInteractableCell(GameObject gameObject) {
        InteractableCell currentCell = null;
        if (gameObject instanceof Animal) {
            currentCell = ((Animal) gameObject).getCell();
        } else if (gameObject instanceof Plant) {
            currentCell = ((Plant) gameObject).getCell();
        } else {
            throw new IllegalArgumentException("Object is not an animal or plant or unavailable to return cell");
        }
        return currentCell;
    }

    private static boolean isEnoughHealth(Animal sameSpecie, Animal gameObject) {
        final int MINIMUM_HEALTH_FOR_REPRODUCE = 70;
        return gameObject.getHealth() > MINIMUM_HEALTH_FOR_REPRODUCE && sameSpecie.getHealth() > MINIMUM_HEALTH_FOR_REPRODUCE;
    }

    private boolean availableSpaceForSpecie(InteractableCell sameSpecie, Integer maxAmount) {
        int amountOfSpecie = sameSpecie.getResidents().getOrDefault(sameSpecie.getClass(), List.of()).size();
        return amountOfSpecie < maxAmount;
    }

    private static @Nullable Animal getSameSpecie(Animal animalGameObject) {
        return animalGameObject.getCell().getResidents().get(animalGameObject.getClass())
                .stream()
                .filter(resident -> resident instanceof Animal)
                .map(resident -> (Animal) resident)
                .filter(animal -> animal.getClass().equals(animalGameObject.getClass()))
                .findFirst()
                .orElse(null);

    }
}
