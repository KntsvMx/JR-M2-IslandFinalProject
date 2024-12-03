package org.example.statistic.interfaces;

public interface Observer {
    void updateTime(long startTime, long endTime);
    void updateKilled();
    void updateAlive();
    void updateBorn();
    void updateDeath();
    void updatePlant();
    void updateCycle();
    void updateTotalOrganisms(int totalOrganisms);
}
