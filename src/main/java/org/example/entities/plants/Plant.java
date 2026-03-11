package org.example.entities.plants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.entities.interfaces.Eatable;
import org.example.entities.interfaces.Organism;
import org.example.entities.map.InteractableCell;
import org.example.managers.CellManager;

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
        lock.lock();
        try {
            if(!this.isAlive) return;
            this.isAlive = false;
            this.health = 0;

            CellManager.getInstance().removeGameObject(this.getCell(), this);
        } finally {
            lock.unlock();
        }
    }

    //    TODO: implement this method into logic of simulation
    public void decreaseHealthOverTime() {
        this.setHealth(this.getHealth() - DECREASE_HEALTH_OVER_TIME);
    }

//    TODO: move this method to DeathService and make it universal for all organisms.
//    public void checkDeath() {
//        if (this.getHealth() <= MINIMAL_HEALTH) {
//            this.setAlive(false);
//        }
//    }
}
