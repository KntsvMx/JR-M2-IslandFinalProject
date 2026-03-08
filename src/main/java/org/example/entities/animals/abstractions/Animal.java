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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;


@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@EqualsAndHashCode
@ToString

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


    private final int HEALTH_AFTER_MOVE = 8;
    private final int HEALTH_AFTER_HUNT = 5;
    private final int HEALTH_AFTER_REPRODUCE = 25;
    private final int HEALTH_AFTER_EXCHANGE = 15;
    private final int HEALTH_IF_NOT_ENOUGH_WEIGHT = 15;

    private final int WEIGHT_TO_HEALTH_EXCHANGE = 10;

    private AtomicInteger consecutiveActions = new AtomicInteger(0);
    public static final int MAX_CONSECUTIVE_ACTIONS = 3;



    @Override
    public void beEaten() {
        lock.lock();
        try {
            this.isAlive = false;
            this.health = 0;
            this.cell.removeGameObjectFromResidents(this);
        } finally {
            lock.unlock();
        }
    }


    public void exchangeWeightToHealth() {
        double weightAfterExchange = this.getWeight() - WEIGHT_TO_HEALTH_EXCHANGE;
        int healthAfterExchange = this.getHealth() + HEALTH_AFTER_EXCHANGE;
        if (this.weight < limits.getMaxWeight()) {
            setHealth(healthAfterExchange);
            setWeight(weightAfterExchange);
        } else if (this.weight <= limits.getMinWeight()) {
            decreaseHealthIfNotEnoughWeight();
        }
    }


    public boolean isStarving() {
        return this.getWeight() <= this.limits.getMinWeight();
    }

    public boolean isFatallyInjured() {
        return this.getHealth() <= 0 || this.getHealth() <= this.getLimits().getMinHealth();
    }

    public void changeHealthAfterMove() {
        this.setHealth(this.getHealth() - HEALTH_AFTER_MOVE);
    }

    public void changeHealthAfterHunt() {
        this.setHealth(this.getHealth() - HEALTH_AFTER_HUNT);
    }

    public void decreaseHealthAfterReproduction() {
        this.setHealth(this.getHealth() - HEALTH_AFTER_REPRODUCE);
    }

    private void decreaseHealthIfNotEnoughWeight() {
        this.setHealth(this.getHealth() - HEALTH_IF_NOT_ENOUGH_WEIGHT);
    }

    public void recoverHealth() {
        if (this.getHealth() < 100 && this.getWeight() > this.getLimits().getMinWeight()) {
            int recoveryAmount = 3;
            int weightCost = 1;
            this.setHealth(Math.min(100, this.getHealth() + recoveryAmount));
            this.setWeight(this.getWeight() - weightCost);
        }
    }

    public void reduceWeightPerTick() {
        double loss = this.getLimits().getMaxWeight() * 0.05;
        this.setWeight(this.getWeight() - loss);
    }

}
