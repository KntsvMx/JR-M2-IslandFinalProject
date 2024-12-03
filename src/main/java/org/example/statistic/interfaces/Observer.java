package org.example.statistic.interfaces;

public interface Observer {
    void updateTime(long startTime, long endTime);
    void updateKilled();
    void updateAlive(int aliveOrganisms);
    void updateBorn();
    void updateDeath();
    void updatePlant(int plantAlive);
    void updateAnimal(int animalAlive);
    void updateCycle();
}
