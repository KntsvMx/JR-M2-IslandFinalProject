package org.example.managers;

import org.example.abstraction.interfaces.GameObject;
import org.example.behaviour.animal.AnimalBehaviour;
import org.example.behaviour.plant.PlantBehaviour;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.Cell;
import org.example.entities.map.GameField;
import org.example.statistic.StatisticCollector;
import org.example.statistic.StatisticMonitor;
import org.example.statistic.interfaces.Observer;
import org.example.statistic.interfaces.Subject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

//Be aware of future changes for this class as now there are
// a lot of problem might appear during either due redesign or completely change of this file

//TODO: Too huge class, which have to be divided by any chance and separate his functionality

public class EntityBehaviorManager implements Subject {
    private static EntityBehaviorManager instance;
    private final AnimalBehaviour animalBehaviour;
    private final PlantBehaviour plantBehaviour;
    private List<Observer> observers = new ArrayList<>();

    private final StatisticMonitor statisticMonitor;
    private final StatisticCollector statisticCollector;
    private int cycleCount = 0;

    private long startTime = 0;
    private long endTime = 0;

    private EntityBehaviorManager() {
        animalBehaviour = new AnimalBehaviour();
        plantBehaviour = new PlantBehaviour();
        statisticMonitor = StatisticMonitor.getInstance();
        statisticCollector = StatisticCollector.getInstance();
        addObserver(statisticMonitor);
    }

    public static EntityBehaviorManager getInstance() {
        if (instance == null) {
            instance = new EntityBehaviorManager();
        }
        return instance;
    }

    public void init(GameField gameField) {
        startTime = System.currentTimeMillis();
        statisticCollector.setGameField(gameField);
        startSimulation(gameField);
    }

    private void startSimulation(GameField gameField) {
        ThreadPoolManager.getInstance().scheduleAtFixedRate(() -> {
            try {
                runCycle(gameField);

                if (cycleCount %2 == 0) {
                    growPlants(gameField);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private void growPlants(GameField gameField) {
        ThreadPoolManager.getInstance().submit( () -> {
            System.out.println("Запуск задачи роста растений");
            for (Cell[] cells : gameField.getCells()) {
                for (Cell cell : cells) {
                    plantBehaviour.grow(cell);
                }
            }
            plantBehaviour.notifyObservers();
        });

    }

    private void handlePlantLifeCycle(GameField gameField) {

    }

    private void runCycle(GameField gameField) {
        ThreadPoolManager threadPool = ThreadPoolManager.getInstance();

        for(Cell[] row: gameField.getCells()) {
            for(Cell cell: row) {
                threadPool.submit(() -> processCell(cell));
            }
        }

        cycleCount++;
        observers.forEach(Observer::updateCycle);

        statisticCollector.notifyObservers();
        collectStatistics();

        if (isAllAnimalsDead(gameField)) {
            stopSimulation();
            endTime = System.currentTimeMillis();
            System.out.println("Game over");
            observers.forEach(observer -> observer.updateTime(startTime, endTime));
        }

    }

    //    TODO: realize statistic observing and print
    private void collectStatistics() {
        System.out.println("Сбор статистики");
        statisticCollector.collectStatisticsFromMap();
        statisticMonitor.printStatistics();
    }

    private void processCell(Cell cell) {
        //TODO: consider whether this lock is needed here or not (perhaps it's might create some performance issues)
        cell.getLock().lock();
        try {
            for (Animal animal : getAllAnimals(cell)) {
                animalBehaviour.act(animal);
            }
        } finally {
            cell.getLock().unlock();
        }
    }

    private List<Animal> getAllAnimals(Cell cell) {
        List<Animal> allAnimals = new ArrayList<>();

        for (Map.Entry<Class<? extends GameObject>, List<GameObject>> entry : cell.getResidents().entrySet()) {
            if (Animal.class.isAssignableFrom(entry.getKey())) {
                for (GameObject obj : entry.getValue()) {
                    if(obj instanceof Animal) {
                        allAnimals.add((Animal) obj);
                    }
                }
            }
        }
        return allAnimals;
    }

    private boolean isAllAnimalsDead(GameField gameField) {
        return Arrays.stream(gameField.getCells())
                .flatMap(Arrays::stream)
                .flatMap(cell -> getAllAnimals(cell).stream())
                .noneMatch(Animal::isAlive);
    }

    public void stopSimulation() {
        statisticCollector.notifyObservers();
        collectStatistics();
        ThreadPoolManager.getInstance().shutDown();
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

    }
}