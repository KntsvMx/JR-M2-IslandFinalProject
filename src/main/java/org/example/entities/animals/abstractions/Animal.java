package org.example.entities.animals.abstractions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.abstraction.interfaces.GameObject;
import org.example.entities.interfaces.Eatable;
import org.example.entities.interfaces.Movable;
import org.example.entities.interfaces.Organism;
import org.example.entities.limits.Limits;
import org.example.entities.map.Cell;
import org.example.entities.plants.Plant;
import org.example.entities.target.Target;
import org.example.factory.OrganismFactory;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Random;

@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@EqualsAndHashCode
@ToString

public abstract class Animal implements Organism, Movable, Eatable {
    private static long serialUID = 1L;

    @Builder.Default
    private final long UID = serialUID++;

    @JsonIgnore
    private Cell cell;

    private Limits limits;
    private Target target;

    private String icon;
    private boolean isAlive = true;
    private int weight;
    private int health;
    private int age;


    public void play() {
//      TODO: To think about exchanging from weight to health in witch ratio it will be
        exchangeWeightToHealth();
        move();
        findFood();
//      TODO: realize when we have enough food and another animal witch the same species
        reproduce();
    }


    public void move() {
        Cell currentCell;
//      TODO: Change health to getHealth()
        if (health >= 60) {
            currentCell = cell.getRandomCell();
            currentCell.addNewResident(getObjectClass(), getGameObject());
            changeHealthAfterAction();
            cell.removeGameObjectFromCell(this);
        }
    }

    private void changeHealthAfterAction() {
        //      TODO: Change health to getHealth()
        health = (health * 5) / 100;
    }

    private GameObject getGameObject() {
        List<GameObject> gameObjects = cell.getResidents().get(getObjectClass());
        return gameObjects.get(gameObjects.indexOf(this));
    }

    private Class<? extends GameObject> getObjectClass() {
        return this.getClass();
    }

    public void findFood() {
        Map<Class<? extends GameObject>, List<GameObject>> residents = getCell().getResidents();
        Map<Class<? extends GameObject>, Integer> targets = getTarget().getTargetMatrix();

//    TODO: Probably available to change to lambda expression
        for (Map.Entry<Class<? extends GameObject>, Integer> target : targets.entrySet()) {
            List<GameObject> potentialFood = residents.get(target.getKey());
            Integer targetValue = target.getValue();
            if (potentialFood != null && !potentialFood.isEmpty()) {
                eat(potentialFood, targetValue);
                return;
            }
        }
    }

    private void eat(List<GameObject> potentialFood, Integer targetValue) {
        Random random = new Random();
        int randomIndex = random.nextInt(100);

        GameObject gameObject = findAnyFirstPotentialFood(potentialFood);

        if (gameObject == null) {
            throw new IllegalArgumentException("Game object not found");
        }
        if (randomIndex >= targetValue) {
            Eatable eatenObject = (Eatable) gameObject;
            eatenObject.beEaten();
            //      TODO: Change this.weight to setWeight()
            this.weight += calculateNutritionalValue(gameObject);
            cell.removeGameObjectFromCell(gameObject);
        } else {
            changeHealthAfterAction();
            //      TODO: Change this.weight to setWeight()
            this.weight -= calculateNutritionalValue(gameObject) / 2;
        }
    }

    private static @Nullable GameObject findAnyFirstPotentialFood(List<GameObject> potentialFood) {
        return potentialFood.stream()
                .filter(obj -> obj instanceof Eatable)
                .findAny()
                .orElse(null);
    }

    private int calculateNutritionalValue(GameObject gameObject) {
        if (gameObject instanceof Animal) {
            return ((Animal) gameObject).getWeight();
        }
        if (gameObject instanceof Plant) {
            return ((Plant) gameObject).getWeight();
        }
        return 0;
    }

    @Override
    public void beEaten() {
        this.setAlive(false);
    }

    private void exchangeWeightToHealth() {
//      TODO: To think about replace MINIMUM_HEALTH to global variable from local
        final int MINIMUM_HEALTH = 20;
        if ((this.getWeight() / 2) > MINIMUM_HEALTH) {
//          TODO: To think about name of exchangedWeight, probably will found better name
            int exchangedWeight = this.getWeight() / 2;
            setHealth(exchangedWeight);
            setWeight(exchangedWeight);
        }
    }

    @Override
    public GameObject reproduce() {
        final int MINIMUM_HEALTH_FOR_REPRODUCE = 70;
        OrganismFactory organismFactory = OrganismFactory.getInstance();
        GameObject sameSpecie = getCell().getResidents().get(getObjectClass()).stream().findAny().orElse(null);
//      TODO: check if cell has available space, not max amount of animals in the current cell, have two parent for child and enough health
        if (getHealth() > MINIMUM_HEALTH_FOR_REPRODUCE && sameSpecie != null && availableSpaceForSpecie()) {
            // TODO: create new animal and place it into cell
            GameObject newAnimal = organismFactory.create(getObjectClass());
            cell.addNewResident(getObjectClass(), newAnimal);
            return newAnimal;
        } else {
           throw new IllegalArgumentException("Species not available");
        }
    }

    private boolean availableSpaceForSpecie() {
        int amountOfSpecie = getCell().getResidents().get(getObjectClass()).size();
        return amountOfSpecie < getLimits().getMaxAmount();
    }
}
