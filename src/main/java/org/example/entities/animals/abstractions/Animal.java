package org.example.entities.animals.abstractions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.entities.interfaces.Eatable;
import org.example.entities.interfaces.Movable;
import org.example.entities.interfaces.Organism;
import org.example.entities.limits.Limits;
import org.example.entities.map.InteractableCell;
import org.example.entities.target.Target;
import org.example.managers.CellManager;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import static org.example.entities.animals.constants.AnimalConstants.*;


@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@EqualsAndHashCode
@ToString

// TODO: create DeathService to handle death of animals and plants instead of call other methods.

public abstract class Animal implements Organism, Movable, Eatable {

    private static AtomicLong serialUID = new AtomicLong(1);
    private final ReentrantLock lock = new ReentrantLock();

    @Builder.Default
    private final long UID = serialUID.getAndIncrement();

    @Setter
    @JsonIgnore
    private InteractableCell cell;

    private Limits limits;
    private Target target;

    private String icon;
    private boolean isAlive = true;
    private double weight;
    private int health;
    private int age;

    private AtomicInteger consecutiveActions = new AtomicInteger(0);
    public static final int MAX_CONSECUTIVE_ACTIONS = 3;


    @Override
    public void beEaten() {
        lock.lock();
        try {
            if (!this.isAlive) return;
            this.isAlive = false;
            this.health = 0;
            
            CellManager.getInstance().removeGameObject(this.getCell(), this);
        } finally {
            lock.unlock();
        }
    }

    public boolean isStarving() {
        return this.getWeight() <= this.limits.getMinWeight();
    }

    public boolean isFatallyInjured() {
        return this.getHealth() <= 0 || this.getHealth() <= this.getLimits().getMinHealth();
    }

    public boolean decreaseHealth(int healthLoss) {
        this.setHealth(Math.max(NO_HEALTH, this.getHealth() - healthLoss));
        return this.isFatallyInjured();
    }

    public void recoverHealth() {
        if (this.getHealth() < MAX_WEIGHT && this.getWeight() > this.getLimits().getMinWeight()) {
            this.setHealth(Math.min(100, this.getHealth() + RECOVERY_AMOUNT));
            this.setWeight(this.getWeight() - WEIGHT_COST);
        }
    }

    public void reduceWeightPerTick() {
        double loss = this.getLimits().getMaxWeight() * 0.05;
        this.setWeight(this.getWeight() - loss);
    }

}
