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


    private final int HEALTH_AFTER_MOVE = 20;
    private final int HEALTH_AFTER_HUNT = 15;
    private final int HEALTH_AFTER_REPRODUCE = 40;
    private final int HEALTH_AFTER_EXCHANGE = 10;
    private final int HEALTH_IF_NOT_ENOUGH_WEIGHT = 20;

    private final int WEIGHT_TO_HEALTH_EXCHANGE = 15;

    @Override
    public void play() {

    }

    @Override
    public void move(InteractableCell targetCell) {
        ReentrantLock currentLock = cell.getLock();
        ReentrantLock targetLock = targetCell.getLock();

        // To avoid deadlock
        if (System.identityHashCode(currentLock) < System.identityHashCode(targetLock)) {
            currentLock.lock();
            targetLock.lock();
        } else {
            targetLock.lock();
            currentLock.lock();
        }

        try {
            targetCell.addGameObjectToResidents(this.getClass(), this);
            cell.removeGameObjectFromResidents(this);
            cell = targetCell;
        } finally {
            currentLock.unlock();
            targetLock.unlock();
        }
    }

    @Override
    public void beEaten() {
        lock.lock();
        try {
            this.setAlive(false);
            this.setHealth(0);
            this.getCell().removeGameObjectFromResidents(this);
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
            changeHealthIfNotEnoughWeight();
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
//    TODO: 2024-12-08(added) need to rename this method to right version
    public void isDeath() {
        if (this.getHealth() <= limits.getMinHealth()) {
            this.setAlive(false);
            this.getCell().removeGameObjectFromResidents(this);
//            TODO: 2024-12-08(added) need to resolve the issue with observer pattern notifying about death
            StatisticMonitor.getInstance().updateDeath();
        }
    }

    //    TODO: 2024-12-08(added) need to change method isDeath to another with similar functionality
    public void changeHealthAfterMove() {
        isDeath();
        this.health -= HEALTH_AFTER_MOVE;
    }

    //    TODO: 2024-12-08(added) need to change method isDeath to another with similar functionality
    public void changeHealthAfterHunt() {
        isDeath();
        this.health -= HEALTH_AFTER_HUNT;
    }

    //    TODO: 2024-12-08(added) need to change method isDeath to another with similar functionality
    public void changeHealthAfterReproduce() {
        this.health -= HEALTH_AFTER_REPRODUCE;
    }

    //    TODO: 2024-12-08(added) need to change method isDeath to another with similar functionality
    private void changeHealthIfNotEnoughWeight() {
        isDeath();
        this.health -= HEALTH_IF_NOT_ENOUGH_WEIGHT;
    }

}
