package org.example.behaviour.generalBehaviorStatements;

import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.animals.constants.AnimalConstants;
import org.example.entities.map.InteractableCell;
import org.example.entities.plants.Plant;
import org.example.managers.CellManager;
import org.example.managers.DeathManager;
import org.example.statistic.AbstractSubject;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class EatBehavior extends AbstractSubject {
    private final CellManager cellManager = CellManager.getInstance();

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
            int diceRoll = ThreadLocalRandom.current().nextInt(100) + 1;
            if (diceRoll <= chanceToEat) {
                performEat(animal, victim, cell);
            }
            if (animal.sufferInjury(AnimalConstants.HEALTH_AFTER_HUNT)) {
                DeathManager.getInstant().registerDeath(animal, cell);
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
        double foodWeight = getFoodWeight(victim);
        int currentHealth = predator.getHealth();
        int newHealth = Math.min(100, currentHealth + (int) (foodWeight * 10));
        double newWeight = Math.min(predator.getLimits().getMaxWeight(), predator.getWeight() + foodWeight * 0.5);

        predator.setHealth(newHealth);
        predator.setWeight(newWeight);

        processVictimDeath(victim);
        cellManager.removeGameObject(cell, victim);
    }

    private void processVictimDeath(GameObject victim) {
        if (victim instanceof Animal) {
            ((Animal) victim).beEaten();
        } else {
            ((Plant) victim).beEaten();
        }
    }

    private static double getFoodWeight(GameObject victim) {
        return victim instanceof Animal ? ((Animal) victim).getWeight() : ((Plant) victim).getWeight();
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
