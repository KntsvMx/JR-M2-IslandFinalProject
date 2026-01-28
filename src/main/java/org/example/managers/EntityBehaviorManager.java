package org.example.managers;

import org.example.behaviour.animal.AnimalBehaviour;
import org.example.behaviour.plant.PlantBehaviour;
import org.example.entities.map.Cell;
import org.example.entities.map.GameField;
import org.example.statistic.StatisticCollector;
import org.example.statistic.StatisticMonitor;
import org.example.statistic.interfaces.Observer;
import org.example.statistic.interfaces.Subject;
import org.example.tasks.CellTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


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

                if (cycleCount % 2 == 0) {
                    growPlants(gameField);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private void growPlants(GameField gameField) {
        ThreadPoolManager.getInstance().submit(() -> {
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
        List<Future<?>> futures = new ArrayList<>();

        for (Cell[] row : gameField.getCells()) {
            for (Cell cell : row) {
                CellTask cellTask = new CellTask(cell, animalBehaviour, plantBehaviour);
                futures.add(threadPool.submit(cellTask));
            }
        }

        for(Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        cycleCount++;
        observers.forEach(Observer::updateCycle);

        statisticCollector.notifyObservers();
        collectStatistics();

        if (gameField.isEcosystemDead()) {
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