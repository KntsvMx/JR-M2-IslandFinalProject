package org.example.behaviour.generalBehaviorStatements;

import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.InteractableCell;
import org.example.entities.plants.Plant;
import org.example.managers.CellManager;
import org.example.statistic.interfaces.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class EatBehavior {
    private List<Observer> observers = new ArrayList<>();
    private final CellManager cellManager;

    public EatBehavior() {
        cellManager = CellManager.getInstance();
    }

    public void eat(Animal animal, InteractableCell cell) {
        if (!isHungry(animal)) {
            return;
        }

        Map<Class<? extends GameObject>, Integer> diet = animal.getTarget().getTargetMatrix();

        if (diet == null || diet.isEmpty()) {
            return;
        }

        Optional<GameObject> foodOpt = findFood(animal, cell, diet);

        if (foodOpt.isPresent()) {
            GameObject victim = foodOpt.get();

            int chanceToEat = diet.getOrDefault(victim.getClass(), 0);
//            int diceRoll = ThreadLocalRandom.current().nextInt(100) + 1;
            int diceRoll = 0;
            if (diceRoll <= chanceToEat) {
                performEat(animal, victim, cell);
            }
        }
    }

    private Optional<GameObject> findFood(Animal animal, InteractableCell cell, Map<Class<? extends GameObject>, Integer> diet) {
        for (Map.Entry<Class<? extends GameObject>, List<GameObject>> entry : cell.getResidents().entrySet()) {
            Class<? extends GameObject> residentType = entry.getKey();

            if (diet.containsKey(residentType)) {
                List<GameObject> potentialFood = entry.getValue();

                for (GameObject foodItem : potentialFood) {
                    if (foodItem != animal && isAlive(foodItem)) {
                        return Optional.of(foodItem);
                    }
                }
            }
        }
        return Optional.empty();
    }

    private void performEat(Animal predator, GameObject victim, InteractableCell cell) {
        double foodWeight = victim instanceof Animal ? ((Animal) victim).getWeight() : ((Plant) victim).getWeight();
        int currentHealth = predator.getHealth();

        int newHealth = Math.min(100, currentHealth + (int) (foodWeight * 10));
        predator.setHealth(newHealth);

        if (victim instanceof Animal) {
            ((Animal) victim).beEaten();
        }

        cell.removeGameObjectFromResidents(victim);
    }


    private boolean isAlive(GameObject obj) {
        if (obj instanceof Animal) {
            return ((Animal) obj).isAlive();
        }
        return true;
    }

    private boolean isHungry(Animal animal) {
        return animal.getWeight() < animal.getLimits().getMaxWeight();
    }
}
