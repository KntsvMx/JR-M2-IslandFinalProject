package org.example.entities.plants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.abstraction.interfaces.GameObject;
import org.example.entities.interfaces.Eatable;
import org.example.entities.interfaces.Organism;
import org.example.entities.map.Cell;
import org.example.entities.map.InteractableCell;

@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
@EqualsAndHashCode
@ToString
public abstract class Plant implements Organism, Eatable, Cloneable{

    @Setter
    @JsonIgnore
    private InteractableCell cell;

    private int maxAmount;

    private String icon;
    private boolean isAlive = true;
    private int weight;
    private int health;
    private int age;

    private final int MINIMAL_HEALTH = 5;

    @Override
    public void play() {

    }

    @Override
    public void beEaten() {
        this.setAlive(false);
        this.setHealth(0);
//        TODO: implement method which will delete GameObject from cell (performance improvement)
    }

    @Override
    public GameObject copy() {
        try {
            return (Plant) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Cloning not supported", e);
        }
    }

    public void decreaseHealthOverTime() {
        this.setHealth(this.getHealth() - 20);
    }

    @Override
    public void checkDeath() {
        if (this.getHealth() <= MINIMAL_HEALTH) {
            this.setAlive(false);
        }
    }
}
