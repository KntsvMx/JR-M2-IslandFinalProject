package org.example.managers;


import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.InteractableCell;
import org.example.entities.plants.Plant;
import org.example.statistic.AbstractSubject;
import org.example.statistic.StatisticMonitor;
import org.example.statistic.interfaces.StatsType;


public class DeathManager extends AbstractSubject {
    private final static DeathManager deathManager = new DeathManager();
    private final CellManager cellManager = CellManager.getInstance();
    private final StatisticMonitor statisticMonitor = StatisticMonitor.getInstance();

    private DeathManager() {

    }

    public static DeathManager getInstant() {
        return deathManager;
    }

    public void processDeath(InteractableCell cell, GameObject victim, StatsType deathReason) {
        cellManager.removeGameObject(cell, victim);

        statisticMonitor.update(deathReason, 1);

        if (victim instanceof Animal) {
            StatisticMonitor.getInstance().update(StatsType.CURRENT_ANIMALS, -1);
        } else if (victim instanceof Plant) {
            StatisticMonitor.getInstance().update(StatsType.CURRENT_PLANTS, -1);
        }
    }

    public void registerDeath(Animal animal, InteractableCell cell) {
        animal.setAlive(false);
        animal.setHealth(0);

        processDeath(cell, animal, StatsType.DIED_ANIMALS);
    }

    public void registerDeath(Plant plant, InteractableCell cell) {
        plant.setAlive(false);
        plant.setHealth(0);

        processDeath(cell, plant, StatsType.DIED_PLANT);
    }
}

