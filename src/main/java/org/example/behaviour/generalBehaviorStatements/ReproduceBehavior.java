package org.example.behaviour.generalBehaviorStatements;

import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.InteractableCell;
import org.example.entities.plants.Plant;
import org.example.factory.OrganismFactory;

public class ReproduceBehavior {

//    TODO: improve method, combine two logic with reproduce animals and plants (by using polymorphism)
    public void reproduceAnimal(Animal animalGameObject) {
        InteractableCell currentCell = animalGameObject.getCell();
        OrganismFactory organismFactory = OrganismFactory.getInstance();

        Animal sameSpecie = (Animal) animalGameObject.getCell().getResidents().entrySet().stream().findAny().orElse(null);
        if (isEnoughHealth(sameSpecie, animalGameObject) && sameSpecie != null && availableSpaceForSpecie(animalGameObject.getCell(), animalGameObject.getLimits().getMaxAmount())) {
            GameObject newAnimal = organismFactory.create(animalGameObject.getClass());
            currentCell.addGameObjectToResidents(newAnimal.getClass(), newAnimal);
        } else {
            throw new IllegalArgumentException("Species not available");
        }
    }

    public void reproducePlant(Plant plantGameObject) {
        InteractableCell currentCell = plantGameObject.getCell();
        OrganismFactory organismFactory = OrganismFactory.getInstance();

        if (availableSpaceForSpecie(plantGameObject.getCell(), plantGameObject.getMaxAmount())) {
          GameObject newPlant = organismFactory.create(plantGameObject.getClass());
          currentCell.addGameObjectToResidents(newPlant.getClass(), newPlant);
        } else {
            throw new IllegalArgumentException("Plants not available");
        }
    }

    private static boolean isEnoughHealth(Animal sameSpecie, Animal gameObject) {
        final int MINIMUM_HEALTH_FOR_REPRODUCE = 70;
        return gameObject.getHealth() > MINIMUM_HEALTH_FOR_REPRODUCE && sameSpecie.getHealth() > MINIMUM_HEALTH_FOR_REPRODUCE;
    }

    private boolean availableSpaceForSpecie(InteractableCell sameSpecie, Integer maxAmount) {
        int amountOfSpecie = sameSpecie.getResidents().get(sameSpecie.getClass()).size();
        return amountOfSpecie < maxAmount;
    }
}
