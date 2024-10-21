package org.example.entities.plants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
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
public abstract class Plant implements Organism, Eatable {

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
    public void play() {

    }

    @Override
    public void beEaten() {
        this.setAlive(false);
        this.setHealth(0);
//        TODO: implement method which will delete GameObject from cell (performance improvement)
    }

}
