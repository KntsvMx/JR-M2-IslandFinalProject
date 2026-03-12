package org.example.statistic;

import lombok.Getter;
import lombok.Setter;
import org.example.statistic.interfaces.Observer;
import org.example.statistic.interfaces.StatsType;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

@Setter

public class StatisticMonitor implements Observer {
    @Getter
    private static StatisticMonitor instance = new StatisticMonitor();

    private final Map<StatsType, LongAdder> stats = new ConcurrentHashMap<>();
    private Instant startTime;
    private Instant endTime;

    private StatisticMonitor() {
        for (StatsType type : StatsType.values()) {
            stats.put(type, new LongAdder());
        }
    }

    public void startTimeOfSimulation() {
        startTime = Instant.now();
    }

    public void endTimeOfSimulation() {
        endTime = Instant.now();
    }

    @Override
    public void update(StatsType type, int count) {
        stats.get(type).add(count);
    }

    public void printStatistics() {
        long currentCycle = stats.get(StatsType.CYCLE_NUMBER).sum();

        StringBuilder sb = new StringBuilder();
        sb.append("\n====== STATISTICS [Cycle ").append(currentCycle).append("] ======\n");
        sb.append(String.format("Time: %s%n", getTimeFormatted()));
        sb.append("-----------------------------------\n");

        long plants = stats.get(StatsType.CURRENT_PLANTS).sum();
        long animals = stats.get(StatsType.CURRENT_ANIMALS).sum();
        long totalOrganisms = plants + animals;

        sb.append(String.format("Total organisms: %d%n", totalOrganisms));
        sb.append("-----------------------------------\n");
        sb.append(String.format("Plants (Alive): %d%n", plants));
        sb.append(String.format("Animals (Alive): %d%n", animals));

        sb.append("---------- Event History ----------\n");
        sb.append(String.format("Animals born: %d%n", stats.get(StatsType.BORN_ANIMALS).sum()));
        sb.append(String.format("Died (Hunger): %d%n", stats.get(StatsType.DIED_ANIMALS).sum()));
        sb.append(String.format("Animals eaten: %d%n", stats.get(StatsType.KILLED_ANIMALS).sum()));
        sb.append(String.format("Plants eaten: %d%n", stats.get(StatsType.EATEN_PLANT).sum()));
        sb.append("-----------------------------------\n");
        System.out.print(sb);

        if (endTime != null) {
            reset();
        }
    }

    private String getTimeFormatted() {
        if (endTime != null) {
            return String.format("%d min %d sec", getMinutes(startTime, endTime), getSeconds(startTime, endTime));
        }

        return String.format("%d min %d sec", getMinutes(startTime, Instant.now()), getSeconds(startTime, Instant.now()));
    }

    private long getSeconds(Instant startTime, Instant endTime) {
        return Duration.between(startTime, endTime).toSecondsPart();
    }

    private long getMinutes(Instant startTime, Instant endTime) {
        return Duration.between(startTime, endTime).toMinutes();
    }

    public void reset() {
        startTime = null;
        endTime = null;
        stats.values().forEach(LongAdder::reset);
    }

}
