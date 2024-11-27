package org.example.behaviour.generalBehaviorStatements;

import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.InteractableCell;
import org.example.entities.plants.Plant;
import org.example.factory.OrganismFactory;
import org.example.managers.CellManager;
import org.jetbrains.annotations.Nullable;

public class ReproduceBehavior {
    private final CellManager cellManager;

    public ReproduceBehavior() {
        cellManager = CellManager.getInstance();
    }

    public void reproduce(GameObject gameObject) {
        InteractableCell currentCell = getInteractableCell(gameObject);
        OrganismFactory organismFactory = OrganismFactory.getInstance();

        if(gameObject instanceof Animal animal) {
            reproduceAnimal(animal, currentCell, organismFactory);
        } else if (gameObject instanceof Plant plantGameObject) {
            reproducePlant(plantGameObject, currentCell, organismFactory);
        }
    }

    private void reproduceAnimal(Animal animal, InteractableCell currentCell, OrganismFactory organismFactory) {
        Animal sameSpecie = getSameSpecie(animal);
        if (canReproduce(animal, currentCell, sameSpecie)) {
            GameObject newAnimal = organismFactory.create(animal.getClass());
            cellManager.addGameObject(currentCell, newAnimal);
        } else {
            throw new IllegalArgumentException("Species not available");
        }
    }


    private void reproducePlant(Plant plantGameObject, InteractableCell currentCell, OrganismFactory organismFactory) {
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
        }
        return currentCell;
    }

    private static boolean isEnoughHealth(Animal sameSpecie, Animal gameObject) {
        final int MINIMUM_HEALTH_FOR_REPRODUCE = 70;
        return gameObject.getHealth() > MINIMUM_HEALTH_FOR_REPRODUCE && sameSpecie.getHealth() > MINIMUM_HEALTH_FOR_REPRODUCE;
    }

    private boolean availableSpaceForSpecie(InteractableCell sameSpecie, Integer maxAmount) {
        int amountOfSpecie = sameSpecie.getResidents().get(sameSpecie.getClass()).size();
        return amountOfSpecie < maxAmount;
    }

    private static @Nullable Animal getSameSpecie(Animal animalGameObject) {
        return (Animal) animalGameObject.getCell().getResidents().entrySet().stream().findAny().orElse(null);
    }
}
