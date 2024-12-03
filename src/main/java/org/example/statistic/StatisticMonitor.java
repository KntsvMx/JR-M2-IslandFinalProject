package org.example.statistic;

import lombok.Setter;
import org.example.statistic.interfaces.Observer;

@Setter

public class StatisticMonitor implements Observer {
    private long timeOfLivingIsland = 0;
    private int cycleCount = 0;
    private int organisms = 0;
    private int aliveOrganisms = 0;
    private int killedOrganisms = 0;
    private int bornOrganisms = 0;
    private int deathOrganisms = 0;
    private int plantAlive = 0;
    private int animalAlive = 0;

    private static StatisticMonitor instance;
    private final StatisticCollector statisticCollector;

    private StatisticMonitor() {
        statisticCollector = StatisticCollector.getInstance();
    }

    public static StatisticMonitor getInstance() {
        if (instance == null) {
            instance = new StatisticMonitor();
        }
        return instance;
    }

    public void printStatistics() {
        StringBuilder sb = new StringBuilder();
        countTimeOfLivingIsland();
        sb.append(String.format("Current cycle: %d%n", cycleCount));
        sb.append("Statistics:%n");
        sb.append(String.format("Alive organisms: %d%n", aliveOrganisms));
        sb.append(String.format("Killed organisms: %d%n", killedOrganisms));
        sb.append(String.format("Born organisms: %d%n", bornOrganisms));
        sb.append(String.format("Death organisms: %d%n", deathOrganisms));
        sb.append(String.format("Alive plants: %d%n", plantAlive));
        sb.append(String.format("Alive animals: %d%n", animalAlive));
        System.out.print(sb.toString());
    }

    private void countTimeOfLivingIsland() {
        long minutes = timeOfLivingIsland / 60000;
        long seconds = (timeOfLivingIsland / 1000) % 60;
        System.out.printf("Time of living island: %d minutes %d seconds%n", minutes, seconds);
    }

    @Override
    public void updateTime(long startTime, long endTime) {
        timeOfLivingIsland = endTime - startTime;
    }

    @Override
    public void updateKilled() {
        killedOrganisms++;
    }

    @Override
    public void updateAlive(int aliveOrganisms) {
        setAliveOrganisms(aliveOrganisms);
    }

    @Override
    public void updateBorn() {
        bornOrganisms++;
    }

    @Override
    public void updateDeath() {
        deathOrganisms++;
    }

    @Override
    public void updatePlant(int plantAlive) {
        setPlantAlive(plantAlive);
    }

    @Override
    public void updateAnimal(int animalAlive) {
        setAnimalAlive(animalAlive);
    }

    @Override
    public void updateCycle() {
        cycleCount++;
    }
}
