package org.example.behaviour.generalBehaviorStatements;

import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.InteractableCell;
import org.example.entities.plants.Plant;
import org.example.factory.OrganismFactory;
import org.example.managers.CellManager;
import org.example.statistic.StatisticMonitor;
import org.example.statistic.interfaces.Observer;
import org.example.statistic.interfaces.Subject;
import org.example.utils.SpaceUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ReproduceBehavior implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private final CellManager cellManager;
    private final ReentrantLock lock = new ReentrantLock();
    private final OrganismFactory organismFactory;
    private final StatisticMonitor statisticMonitor;

    public ReproduceBehavior() {
        cellManager = CellManager.getInstance();
        organismFactory = OrganismFactory.getInstance();
        statisticMonitor = StatisticMonitor.getInstance();
        addObserver(statisticMonitor);
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
            animal.changeHealthAfterReproduce();
            observers.forEach(Observer::updateBorn);
        }
        // TODO 2024-12-08(added) probably need to add some logger here to log that animals can't reproduce
    }

    private void reproducePlant(Plant plantGameObject, InteractableCell currentCell) {
        if (SpaceUtil.availableSpaceForSpecie(currentCell, plantGameObject.getMaxAmount())) {
            GameObject newPlant = organismFactory.create(plantGameObject.getClass());
            cellManager.addGameObject(currentCell, newPlant);
        } else {
            throw new IllegalArgumentException("Plants not available");
        }
    }

    private boolean canReproduce(Animal animal, InteractableCell currentCell, Animal sameSpecie) {
        return isEnoughHealth(sameSpecie, animal)
                && sameSpecie != null
                && SpaceUtil.availableSpaceForSpecie(currentCell, animal.getLimits().getMaxAmount())
                && isMatureEnough(animal, sameSpecie);
    }

    private boolean isMatureEnough(Animal animal, Animal sameSpecie) {
        int minReproductionAge = 2;
        return animal.getAge() >= minReproductionAge && sameSpecie.getAge() >= minReproductionAge;
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
        final int MINIMUM_HEALTH_FOR_REPRODUCE = 50;
        return gameObject.getHealth() > MINIMUM_HEALTH_FOR_REPRODUCE && sameSpecie.getHealth() > MINIMUM_HEALTH_FOR_REPRODUCE;
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
        // Implementation will be added when Subject interface is updated
    }
}
