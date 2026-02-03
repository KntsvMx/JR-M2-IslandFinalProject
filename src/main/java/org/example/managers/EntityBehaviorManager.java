package org.example.managers;

import org.example.behaviour.animal.AnimalBehaviour;
import org.example.behaviour.plant.PlantBehaviour;
import org.example.entities.map.Cell;
import org.example.entities.map.GameField;
import org.example.statistic.AbstractSubject;
import org.example.statistic.StatisticMonitor;
import org.example.statistic.interfaces.Observer;
import org.example.tasks.CellTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class EntityBehaviorManager extends AbstractSubject {
    private static EntityBehaviorManager instance;
    private final AnimalBehaviour animalBehaviour;
    private final PlantBehaviour plantBehaviour;
    private List<Observer> observers = new ArrayList<>();

    private final StatisticMonitor statisticMonitor;
    private int cycleCount = 0;

    private long startTime = 0;
    private long endTime = 0;

    private EntityBehaviorManager() {
        animalBehaviour = new AnimalBehaviour();
        plantBehaviour = new PlantBehaviour();
        statisticMonitor = StatisticMonitor.getInstance();
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
        startSimulation(gameField);
    }

    private void startSimulation(GameField gameField) {
        ThreadPoolManager.getInstance().scheduleAtFixedRate(() -> {
            try {
                runCycle(gameField);
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
        });

    }

    private void runCycle(GameField gameField) {
        ThreadPoolManager threadPool = ThreadPoolManager.getInstance();
        List<Future<?>> futures = new ArrayList<>();

        for (Cell[] row : gameField.getCells()) {
            for (Cell cell : row) {
                CellTask cellTask = new CellTask(cell, animalBehaviour, plantBehaviour, cycleCount);
                futures.add(threadPool.submit(cellTask));
            }
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        cycleCount++;

        collectStatistics();

        if (gameField.isEcosystemDead()) {
            stopSimulation();
            endTime = System.currentTimeMillis();
            System.out.println("Game over");

        }

    }

    //    TODO: realize statistic observing and print
    private void collectStatistics() {
        statisticMonitor.printStatistics();
    }


    public void stopSimulation() {
        collectStatistics();
        ThreadPoolManager.getInstance().shutDown();
    }
}