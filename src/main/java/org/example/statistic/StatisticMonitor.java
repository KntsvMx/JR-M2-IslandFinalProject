package org.example.statistic;

import lombok.Setter;
import org.example.statistic.interfaces.Observer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Setter

public class StatisticMonitor implements Observer {
    private AtomicLong timeOfLivingIsland = new AtomicLong(0);
    private AtomicInteger cycleCount = new AtomicInteger(0);
    private AtomicInteger aliveOrganisms = new AtomicInteger(0);
    private AtomicInteger killedOrganisms = new AtomicInteger(0);
    private AtomicInteger bornOrganisms = new AtomicInteger(0);
    private AtomicInteger deathOrganisms = new AtomicInteger(0);
    private AtomicInteger plantAlive = new AtomicInteger(0);
    private AtomicInteger animalAlive = new AtomicInteger(0);

    private static StatisticMonitor instance;

    private StatisticMonitor() {

    }

    public static StatisticMonitor getInstance() {
        if (instance == null) {
            instance = new StatisticMonitor();
        }
        return instance;
    }

    public void printStatistics() {
        StringBuilder sb = new StringBuilder();
        if (timeOfLivingIsland.get() != 0) {
            countTimeOfLivingIsland();
        }
        sb.append("____________________Current Statistics___________________\n");
        sb.append(String.format("Current cycle: %d%n", cycleCount.get()));
        sb.append("_________________________________________________________\n");
        sb.append("Statistics: \n");
        sb.append(String.format("Alive organisms: %d%n", aliveOrganisms.get()));
        sb.append(String.format("Killed organisms: %d%n", killedOrganisms.get()));
        sb.append(String.format("Born organisms: %d%n", bornOrganisms.get()));
        sb.append(String.format("Death organisms: %d%n", deathOrganisms.get()));
        sb.append(String.format("Alive plants: %d%n", plantAlive.get()));
        sb.append(String.format("Alive animals: %d%n", animalAlive.get()));
        sb.append("_________________________________________________________\n");
        System.out.print(sb);
    }

    private void countTimeOfLivingIsland() {
        long minutes = timeOfLivingIsland.get() / 60000;
        long seconds = (timeOfLivingIsland.get() / 1000) % 60;
        System.out.printf("Time of living island: %d minutes %d seconds%n", minutes, seconds);
    }

    @Override
    public void updateTime(long startTime, long endTime) {
        this.timeOfLivingIsland.set(endTime - startTime);
    }

    @Override
    public void updateKilled() {
        killedOrganisms.incrementAndGet();
        aliveOrganisms.decrementAndGet();
    }

    @Override
    public void updateAlive(int aliveOrganisms) {
        this.aliveOrganisms.set(aliveOrganisms);
    }

    @Override
    public void updateBorn() {
        bornOrganisms.incrementAndGet();
    }

    @Override
    public void updateDeath() {
        deathOrganisms.incrementAndGet();
        aliveOrganisms.decrementAndGet();
    }

    @Override
    public void updatePlant(int plantAlive) {
        this.plantAlive.set(plantAlive);
    }

    @Override
    public void updateAnimal(int animalAlive) {
        this.animalAlive.set(animalAlive);
    }

    @Override
    public void updateCycle() {
        cycleCount.incrementAndGet();
    }
}
