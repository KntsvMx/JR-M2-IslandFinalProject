package org.example.behaviour.generalBehaviorStatements;

import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.interfaces.Eatable;
import org.example.entities.map.InteractableCell;
import org.example.entities.plants.Plant;
import org.example.managers.CellManager;
import org.example.statistic.interfaces.Observer;
import org.example.statistic.interfaces.Subject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class EatBehavior implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private final CellManager cellManager;
    private final ReentrantLock lock = new ReentrantLock();

    public EatBehavior() {
        cellManager = CellManager.getInstance();
    }

    public void findFood(Animal animal) {

        if (isHungry(animal)) {
            return;
        }

        lock.lock();
        try {
            Map<Class<? extends GameObject>, List<GameObject>> residents = animal.getCell().getResidents();
            Map<Class<? extends GameObject>, Integer> targets = animal.getTarget().getTargetMatrix();

//    TODO: Probably available to change to lambda expression below
//            targets.entrySet().stream()
//                    .filter(target -> {
//                        List<GameObject> potentialFood = residents.get(target.getKey());
//                        return potentialFood != null && !potentialFood.isEmpty();
//                    })
//                    .findFirst()
//                    .ifPresent(target -> {
//                        List<GameObject> potentialFood = residents.get(target.getKey());
//                        Integer targetValue = target.getValue();
//                        eat(animal, potentialFood, targetValue);
//                    });

            for (Map.Entry<Class<? extends GameObject>, Integer> target : targets.entrySet()) {
                List<GameObject> potentialFood = residents.get(target.getKey());
                Integer targetValue = target.getValue();
                if (potentialFood != null && !potentialFood.isEmpty()) {
                    eat(animal, potentialFood, targetValue);
                    return;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    private boolean isHungry(Animal animal) {
        return animal.getWeight() < animal.getLimits().getMaxWeight();
    }

    private void eat(Animal animal, List<GameObject> potentialFood, Integer targetValue) {
        Random random = new Random();
        int randomIndex = random.nextInt(100);
        int currentWeight = animal.getWeight();
        int SUBTRACTING_WEIGHT = -5;
        InteractableCell interactableCell = animal.getCell();
        interactableCell.getLock().lock();
        try {
            GameObject gameObject = findAnyFirstPotentialFood(potentialFood);

            if (gameObject == null) {
                throw new IllegalArgumentException("Game object not found");
            }

            if (randomIndex >= targetValue || targetValue == 100) {
                Eatable eatenObject = (Eatable) gameObject;
                eatenObject.beEaten();

                animal.setWeight(calculateNutritionalValue(gameObject));
                observers.forEach(Observer::updateKilled);
                cellManager.removeGameObject(interactableCell, (GameObject) eatenObject);
            } else {
//                animal.changeHealthAfterAction();
                animal.setWeight(currentWeight - SUBTRACTING_WEIGHT);
            }
        } finally {
            interactableCell.getLock().unlock();
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


//Observer methods

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
}
