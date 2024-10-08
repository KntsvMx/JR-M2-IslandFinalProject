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

@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@EqualsAndHashCode
@ToString

public abstract class Animal implements Organism, Movable, Eatable {
    private static long serialUID = 1L;

    @Builder.Default
    private final long UID = serialUID++;

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


    public void play() {

    }

    public void move() {
    // TODO: realize move method via MoveBehavior
    }



    @Override
    public void beEaten() {
        this.setAlive(false);
    }

    public void changeHealthAfterAction() {
        setHealth((getHealth() * 5) / 100);
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
}
