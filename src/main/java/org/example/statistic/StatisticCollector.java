package org.example.statistic;

import lombok.Setter;
import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.Cell;
import org.example.entities.map.GameField;
import org.example.entities.plants.Plant;
import org.example.statistic.interfaces.Observer;
import org.example.statistic.interfaces.Subject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

@Setter
public class StatisticCollector implements Subject {
    private static StatisticCollector instance;
    private List<Observer> observers = new ArrayList<>();

    private GameField gameField;
    private volatile AtomicInteger amountOfAnimals = new AtomicInteger(0);
    private volatile AtomicInteger amountOfPlants = new AtomicInteger(0);
    private volatile AtomicInteger aliveOrganisms = new AtomicInteger(0);

    private final StatisticMonitor statisticMonitor = StatisticMonitor.getInstance();

    private StatisticCollector() {
    }

    public static StatisticCollector getInstance() {
        if (instance == null) {
            instance = new StatisticCollector();
            instance.initializeObservers();
        }
        return instance;
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
        for (Observer observer : observers) {
            observer.updateAnimal(amountOfAnimals.get());
            observer.updatePlant(amountOfPlants.get());
            observer.updateAlive(aliveOrganisms.get());
        }
    }

    public void collectStatisticsFromMap() {
        amountOfAnimals.set(countAmountOfAnimals());
        amountOfPlants.set(countAmountOfPlants());
        aliveOrganisms.set(countAliveOrganisms());
        notifyObservers();
    }

    private void initializeObservers() {
        observers.add(statisticMonitor);
    }

    private int countAmountOfPlants() {
        return (int) Arrays.stream(this.gameField.getCells())
                .flatMap(Arrays::stream)
                .flatMap(cell -> getPlants(cell).stream())
                .filter(Plant::isAlive)
                .count();
    }

    private List<Plant> getPlants(Cell cell) {
        for (Map.Entry<Class<? extends GameObject>, ConcurrentLinkedDeque<GameObject>> gameObject : cell.getResidents().entrySet()) {
            if (Plant.class.isAssignableFrom(gameObject.getKey())) {
                return gameObject.getValue().stream()
                        .filter(gameObject1 -> gameObject1 instanceof Plant)
                        .map(gameObject1 -> (Plant) gameObject1)
                        .toList();
            }
        }
        return List.of();
    }

    private int countAmountOfAnimals() {
        return (int) Arrays.stream(gameField.getCells())
                .flatMap(Arrays::stream)
                .flatMap(cell -> getAnimals(cell).stream())
                .filter(Animal::isAlive)
                .count();
    }

//  TODO: 2024-12-01(added) need to refactor code to avoid code duplication in StatisticCollector and EntityBehaviorManager classes
    private List<Animal> getAnimals(Cell cell) {
        for (Map.Entry<Class<? extends GameObject>, ConcurrentLinkedDeque<GameObject>> gameObject : cell.getResidents().entrySet()) {
            if (Animal.class.isAssignableFrom(gameObject.getKey())) {
                return gameObject.getValue().stream()
                        .filter(gameObject1 -> gameObject1 instanceof Animal)
                        .map(gameObject1 -> (Animal) gameObject1)
                        .toList();
            }
        }
        return List.of();
    }


    private int countAliveOrganisms() {
        return countAmountOfAnimals() + countAmountOfPlants();
    }
}
