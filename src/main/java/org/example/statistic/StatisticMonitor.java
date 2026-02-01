package org.example.statistic;

import lombok.Setter;
import org.example.statistic.interfaces.Observer;
import org.example.statistic.interfaces.StatsType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

@Setter

public class StatisticMonitor implements Observer {
    private final Map<StatsType, LongAdder> stats = new ConcurrentHashMap<>();

    private long startTime;

    private static StatisticMonitor instance;

    private StatisticMonitor() {
        for(StatsType type: StatsType.values()) {
            stats.put(type, new LongAdder());
        }
        // TODO: Initialization time instantiation from Runner (when simulation starts)
        this.startTime = System.currentTimeMillis();
    }

    public static StatisticMonitor getInstance() {
        if (instance == null) {
            instance = new StatisticMonitor();
        }
        return instance;
    }

    @Override
    public void update(StatsType type, int count) {
        stats.get(type).add(count);
    }

    public void increment(StatsType type) {
        update(type, 1);
    }

    public void printStatistics() {
        long currentCycle = stats.get(StatsType.CYCLE_NUMBER).sum();
        long plants = stats.get(StatsType.CURRENT_PLANTS).sum();
        long animals = stats.get(StatsType.CURRENT_ANIMALS).sum();

        long totalOrganisms = plants + animals;


        StringBuilder sb = new StringBuilder();
        sb.append("____________________Current Statistics___________________\n");
        sb.append(String.format("Current cycle: %d%n", getTimeFormatted()));
        sb.append("_________________________________________________________\n");


        sb.append(String.format("Alive plants: %d%n", plants));
        sb.append(String.format("Alive animals: %d%n", animals));
        sb.append(String.format("Total organisms: %d%n", totalOrganisms));
        sb.append(String.format("/n--- Event History ---/n"));
        sb.append(String.format("Animals born: %d%n", stats.get(StatsType.BORN_ANIMALS).sum()));
        sb.append(String.format("Animals eaten: %d%n", stats.get(StatsType.KILLED_ANIMALS).sum()));
        sb.append(String.format("Plants eaten: %d%n", stats.get(StatsType.EATEN_PLANT).sum()));

        sb.append("_________________________________________________________\n");
        System.out.print(sb);
    }

    private String getTimeFormatted() {
        long duration = System.currentTimeMillis() - startTime;
        long minutes = duration / 60000;
        long seconds = (duration / 1000) % 60;
        return String.format("%d min %d sec", minutes, seconds);
    }

    public void reset() {
        stats.values().forEach(LongAdder::reset);
        this.startTime = System.currentTimeMillis();
    }


}
