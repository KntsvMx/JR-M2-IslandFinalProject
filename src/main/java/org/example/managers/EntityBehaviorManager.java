package org.example.managers;

import lombok.Getter;
import org.example.behaviour.animal.AnimalBehaviour;
import org.example.behaviour.generalBehaviorStatements.EatBehavior;
import org.example.behaviour.generalBehaviorStatements.MoveBehavior;
import org.example.behaviour.generalBehaviorStatements.ReproduceBehavior;
import org.example.behaviour.plant.PlantBehaviour;
import org.example.entities.map.Cell;
import org.example.entities.map.GameField;
import org.example.statistic.AbstractSubject;
import org.example.statistic.StatisticMonitor;
import org.example.statistic.interfaces.StatsType;
import org.example.tasks.CellTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class EntityBehaviorManager extends AbstractSubject {
    @Getter
    private static final EntityBehaviorManager instance = new EntityBehaviorManager();
    private final AnimalBehaviour animalBehaviour;
    private final PlantBehaviour plantBehaviour;

    private final StatisticMonitor statisticMonitor;
    private int cycleCount = 0;

    private EntityBehaviorManager() {
        MoveBehavior moveBehavior = new MoveBehavior();
        EatBehavior eatBehavior = new EatBehavior();
        ReproduceBehavior reproduceBehavior = new ReproduceBehavior();
        animalBehaviour = new AnimalBehaviour(moveBehavior, eatBehavior, reproduceBehavior);
        plantBehaviour = new PlantBehaviour(reproduceBehavior);
        statisticMonitor = StatisticMonitor.getInstance();
    }

    public void init(GameField gameField) {
        statisticMonitor.startTimeOfSimulation();
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

    private void runCycle(GameField gameField) {
        List<Future<?>> futures = submitCellTasks(gameField);
        waitForTasksCompletion(futures);

        incrementCycleCount();

        checkGameOver(gameField);

    }

    private List<Future<?>> submitCellTasks(GameField gameField) {
        ThreadPoolManager threadPool = ThreadPoolManager.getInstance();
        List<Future<?>> futures = new ArrayList<>();
        for (Cell[] row : gameField.getCells()) {
            for (Cell cell : row) {
                CellTask cellTask = new CellTask(cell, animalBehaviour, plantBehaviour, cycleCount);
                futures.add(threadPool.submit(cellTask));
            }
        }
        return futures;
    }

    private static void waitForTasksCompletion(List<Future<?>> futures) {
        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void incrementCycleCount() {
        cycleCount++;
        statisticMonitor.update(StatsType.CYCLE_NUMBER, 1);
        printStatistic();
    }

    //    TODO: realize statistic observing and print
    private void printStatistic() {
        statisticMonitor.printStatistics();
    }

    private void checkGameOver(GameField gameField) {
//        TODO: move isEcosystemDead to DeathService
        if (gameField.isEcosystemDead()) {
            stopSimulation();
            System.out.println("Game over");
        }
    }

    public void stopSimulation() {
        statisticMonitor.endTimeOfSimulation();
        printStatistic();
        ThreadPoolManager.getInstance().shutDown();
    }
}