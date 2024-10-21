package org.example.statistic;

import lombok.Getter;
import lombok.Setter;
import org.example.statistic.interfaces.Observer;

@Setter
@Getter

//    TODO: realize observer pattern here
public class StatisticMonitor implements Observer {
    private int organisms = 0;
    private int aliveOrganisms = 0;
    private int killedOrganisms = 0;
    private int bornOrganisms = 0;
    private int deathOrganisms = 0;
    private int plantAlive = 0;


//    TODO: add realization to printing statistic in console
    public void printStatistics() {

    }

//    TODO: rename soon
    public void countTimeOfLivingIsland() {

    }

    @Override
    public void updateKilled() {

    }

    @Override
    public void updateAlive() {

    }

    @Override
    public void updateBorn() {

    }

    @Override
    public void updateDeath() {

    }

    @Override
    public void updatePlant() {

    }
}
