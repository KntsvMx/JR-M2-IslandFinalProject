package org.example.entities.plants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.entities.interfaces.Eatable;
import org.example.entities.interfaces.Organism;
import org.example.entities.map.InteractableCell;
import org.example.managers.DeathManager;
import org.example.statistic.interfaces.StatsType;

import java.util.concurrent.locks.ReentrantLock;

import static org.example.entities.plants.constants.PlantConstants.DECREASE_HEALTH_OVER_TIME;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public abstract class Plant implements Organism, Eatable {
    private final ReentrantLock lock = new ReentrantLock();


    @Setter
    @JsonIgnore
    private InteractableCell cell;

    private int maxAmount;

    private String icon;
    private boolean isAlive = true;
    private int weight;
    private int health;
    private int age;


    @Override
    public void beEaten() {
        if (!this.isAlive) return;
        this.isAlive = false;
        this.health = 0;

        DeathManager.getInstant().processDeath(this.getCell(), this, StatsType.EATEN_PLANT);
    }

    public boolean decreaseHealthOverTime() {
        this.setHealth(this.getHealth() - DECREASE_HEALTH_OVER_TIME);
        return this.getHealth() == 0;
    }
}
