package org.example.statistic.interfaces;

public interface Observer {
    void update(StatsType type, int count);
}
