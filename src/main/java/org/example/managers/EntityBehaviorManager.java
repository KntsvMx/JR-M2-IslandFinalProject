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

    //Perhaps idea to make it final is good
    //Think about divide observation and other classes from BehaviorManager as it's not related to behavior
    private List<Observer> observers = new ArrayList<>();

    //Future threads executors
    //Possibly capable to divide and create other class for threads in this case to prevent misrepresenting class with its main functionality
    private final ScheduledExecutorService scheduledExecutor;
    private final ExecutorService cellExecutorService;

    //Monitoring simulation process
    //The same status as fields above
    private final StatisticMonitor statisticMonitor;
    private final StatisticCollector statisticCollector;
    private int cycleCount = 0;

    private long startTime = 0;
    private long endTime = 0;

    private EntityBehaviorManager() {
        scheduledExecutor = Executors.newScheduledThreadPool(2);
        cellExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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
        scheduledExecutor.scheduleAtFixedRate(() -> {
            if (cycleCount % 2 == 0) {
                this.growPlants(gameField);
            }
        }, 0, 10, TimeUnit.SECONDS);
        while (!Thread.currentThread().isInterrupted()) {
            runCycle(gameField);
        }
    }

    private void growPlants(GameField gameField) {
        System.out.println("Запуск задачи роста растений");
        for (Cell[] cells : gameField.getCells()) {
            for (Cell cell : cells) {
                CompletableFuture.runAsync(() -> plantBehaviour.grow(cell), cellExecutorService)
                        .thenRun(plantBehaviour::notifyObservers);
            }
        }
    }

    private void handlePlantLifeCycle(GameField gameField) {
        for (Cell[] cells : gameField.getCells()) {
            for (Cell cell : cells) {
                CompletableFuture.runAsync(() -> plantBehaviour.lifeCycle(cell), cellExecutorService)
                        .thenRun(plantBehaviour::notifyObservers);
            }
        }
    }

    private void runCycle(GameField gameField) {
        if (scheduledExecutor.isShutdown() || cellExecutorService.isShutdown()) {
            return;
        }

        List<CompletableFuture<Void>> cellFutures = new ArrayList<>();

        for (Cell[] cells : gameField.getCells()) {
            for (Cell cell : cells) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> processCell(cell), cellExecutorService);
                cellFutures.add(future);
            }
        }
        scheduledExecutor.schedule(() -> handlePlantLifeCycle(gameField), 0, TimeUnit.SECONDS);

        CompletableFuture.allOf(cellFutures.toArray(new CompletableFuture[0])).join();
        cycleCount++;
        observers.forEach(Observer::updateCycle);

        scheduledExecutor.schedule(statisticCollector::notifyObservers, 0, TimeUnit.SECONDS);
        scheduledExecutor.schedule(this::collectStatistics, 0, TimeUnit.SECONDS);

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

    private synchronized void processCell(Cell cell) {
        for (Animal animal : getAllAnimals(cell)) {
            animalBehaviour.act(animal);
        }
    }

    private List<Animal> getAllAnimals(Cell cell) {
        for (Map.Entry<Class<? extends GameObject>, List<GameObject>> gameObject : cell.getResidents().entrySet()) {
            if (Animal.class.isAssignableFrom(gameObject.getKey())) {
                return gameObject.getValue().stream()
                        .filter(gameObject1 -> gameObject1 instanceof Animal)
                        .map(gameObject1 -> (Animal) gameObject1)
                        .toList();
            }
        }
        return List.of();
    }

    private boolean isAllAnimalsDead(GameField gameField) {
        return Arrays.stream(gameField.getCells())
                .flatMap(Arrays::stream)
                .flatMap(cell -> getAllAnimals(cell).stream())
                .noneMatch(Animal::isAlive);
    }

    public void stopSimulation() {
        scheduledExecutor.schedule(statisticCollector::notifyObservers, 0, TimeUnit.SECONDS);
        scheduledExecutor.schedule(this::collectStatistics, 0, TimeUnit.SECONDS);

        scheduledExecutor.shutdown();
        cellExecutorService.shutdown();
        try {
            if (!scheduledExecutor.awaitTermination(1, TimeUnit.MINUTES)) {
                scheduledExecutor.shutdownNow();
            }
            if (!cellExecutorService.awaitTermination(1, TimeUnit.MINUTES)) {
                cellExecutorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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