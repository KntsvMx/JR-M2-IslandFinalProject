package org.example.statistic.interfaces;

public interface Observer {
    void updateKilled();
    void updateAlive();
    void updateBorn();
    void updateDeath();
    void updatePlant();
}
