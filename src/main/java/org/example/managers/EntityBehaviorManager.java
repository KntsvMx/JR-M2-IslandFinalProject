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

public class EntityBehaviorManager implements Subject {
    private static EntityBehaviorManager instance;
    private final AnimalBehaviour animalBehaviour;
    private final PlantBehaviour plantBehaviour;

    private List<Observer> observers = new ArrayList<>();

    private final ScheduledExecutorService scheduledExecutor;
    private final ExecutorService cellExecutorService;

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
    }

    public static EntityBehaviorManager getInstance() {
        if (instance == null) {
            instance = new EntityBehaviorManager();
        }
        return instance;
    }

    public void init(GameField gameField) {
        startSimulation(gameField);
        startTime = System.currentTimeMillis();
        statisticCollector.setGameField(gameField);
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

    private void runCycle(GameField gameField) {
        List<CompletableFuture<Void>> cellFutures = new ArrayList<>();

        for (Cell[] cells : gameField.getCells()) {
            for (Cell cell : cells) {
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> processCell(cell), cellExecutorService);
                cellFutures.add(future);
            }
        }

        CompletableFuture.allOf(cellFutures.toArray(new CompletableFuture[0])).join();
        cycleCount++;
        observers.forEach(Observer::updateCycle);


        if (isAllAnimalsDead(gameField)) {
            stopSimulation();
            endTime = System.currentTimeMillis();
            System.out.println("Game over");
            observers.forEach(observer -> observer.updateTime(startTime, endTime));
        }

        scheduledExecutor.schedule(statisticCollector::notifyObservers, 0, TimeUnit.SECONDS);
        scheduledExecutor.schedule(this::collectStatistics, 0, TimeUnit.SECONDS);
    }

    //    TODO: realize statistic observing and print
    private void collectStatistics() {
        System.out.println("Сбор статистики");
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