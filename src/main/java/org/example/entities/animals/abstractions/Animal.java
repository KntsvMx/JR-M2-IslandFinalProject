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
import org.example.global.GlobalVariables;

import java.util.concurrent.atomic.AtomicLong;


@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@EqualsAndHashCode
@ToString

public abstract class Animal implements Organism, Movable, Eatable, Cloneable {

    private static AtomicLong serialUID = new AtomicLong(1);

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

    @Override
    public void play() {

    }

    @Override
    public void move(InteractableCell targetCell) {
        GlobalVariables.lock.lock();
        try {
            targetCell.addGameObjectToResidents(this.getClass(), this);
            cell.removeGameObjectFromResidents(this);
            cell = targetCell;
        } finally {
            GlobalVariables.lock.unlock();
        }
    }

    @Override
    public void beEaten() {
        GlobalVariables.lock.lock();
        try {
            this.setAlive(false);
            this.setHealth(0);
            cell.removeGameObjectFromResidents(this);
        } finally {
            GlobalVariables.lock.unlock();
        }
    }

    public void changeHealthAfterAction() {
        setHealth(getHealth() - 20);
    }

    public void exchangeWeightToHealth() {
//      TODO: To think about replace MINIMUM_HEALTH to global variable from local
        final int MINIMUM_HEALTH = 20;
        if ((this.getWeight() / 2) > MINIMUM_HEALTH) {
//          TODO: To think about name of exchangedWeight, probably will found better name
            int exchangedWeight = this.getWeight() / 2;
            setHealth(exchangedWeight);
            setWeight(exchangedWeight);
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
}
