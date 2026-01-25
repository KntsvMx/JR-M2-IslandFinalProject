package org.example.entities.animals.abstractions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.abstraction.interfaces.GameObject;
import org.example.entities.interfaces.Eatable;
import org.example.entities.interfaces.Movable;
import org.example.entities.interfaces.Organism;
import org.example.entities.limits.Limits;
import org.example.entities.map.InteractableCell;
import org.example.entities.target.Target;
import org.example.statistic.StatisticMonitor;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;


@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@EqualsAndHashCode
@ToString

public abstract class Animal implements Organism, Movable, Eatable, Cloneable {

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
    private int weight;
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
    public void play() {

    }


    @Override
    public void beEaten() {
        lock.lock();
        try {
            this.setAlive(false);
            this.setHealth(0);
            this.getCell().removeGameObjectFromResidents(this);
            StatisticMonitor.getInstance().updateKilled();
        } finally {
            lock.unlock();
        }
    }


    public void exchangeWeightToHealth() {
        int weightAfterExchange = this.getWeight() - WEIGHT_TO_HEALTH_EXCHANGE;
        int healthAfterExchange = this.getHealth() + HEALTH_AFTER_EXCHANGE;
        if (this.weight < limits.getMaxWeight()) {
            setHealth(healthAfterExchange);
            setWeight(weightAfterExchange);
        } else if (this.weight <= limits.getMinWeight()) {
            decreaseHealthIfNotEnoughWeight();
        }
    }

    @Override
    public GameObject copy() {
        try {
            return (Animal) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }

    @Override
    public void checkDeath() {
        if (shouldDie() && isAlive) {
            die();
            // TODO: 2025-06-19(added) need to implement observer notification about this event
        }
    }

    private void die() {
        this.setAlive(false);
        this.getCell().removeGameObjectFromResidents(this);
//            TODO: 2024-12-08(added) need to resolve the issue with observer pattern notifying about death
        StatisticMonitor.getInstance().updateDeath();
    }

    private boolean shouldDie() {
        return isEnoughHealthAndWeight() || isEnoughWeightToSurvive();
    }
    //TODO: 2025-08-07 (added) refactor this method to be more readable
    private boolean isEnoughWeightToSurvive() {
        return this.getWeight() >= this.limits.getMinWeight() && this.getCell() != null;
    }
    //TODO: 2025-08-07 (added) refactor this method to be more readable
    private boolean isEnoughHealthAndWeight() {
        return this.getHealth() == 0 && this.getWeight() <= this.limits.getMinWeight();
    }

    public void changeHealthAfterMove() {
        this.setHealth(this.getHealth() - HEALTH_AFTER_MOVE);
    }

    public void changeHealthAfterHunt() {
        this.setHealth(this.getHealth() - HEALTH_AFTER_HUNT);
        checkDeath();
    }

    public void decreaseHealthAfterReproduction() {
        this.setHealth(this.getHealth() - HEALTH_AFTER_REPRODUCE);
        checkDeath();
    }

    private void decreaseHealthIfNotEnoughWeight() {
        this.setHealth(this.getHealth() - HEALTH_IF_NOT_ENOUGH_WEIGHT);
        checkDeath();
    }

    public void recoverHealth() {
        if (this.getHealth() < 100 && this.getWeight() > this.getLimits().getMinWeight()) {
            int recoveryAmount = 3;
            int weightCost = 1;
            this.setHealth(Math.min(100, this.getHealth() + recoveryAmount));
            this.setWeight(this.getWeight() - weightCost);
        }
    }

    public void incrementConsecutiveActions() {
        this.consecutiveActions.incrementAndGet();
    }

    public void resetConsecutiveActions() {
        this.consecutiveActions.set(0);
    }

}
