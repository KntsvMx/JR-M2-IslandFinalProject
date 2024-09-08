package org.example.entities.plants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.example.abstraction.interfaces.GameObject;
import org.example.entities.interfaces.Eatable;
import org.example.entities.interfaces.Organism;
import org.example.entities.map.Cell;
import org.example.factory.OrganismFactory;

@NoArgsConstructor
@SuperBuilder
@Getter
@EqualsAndHashCode
@ToString
public abstract class Plant implements Organism, Eatable {

    @JsonIgnore
    private Cell cell;

    private int maxAmount;

    private String icon;
    private boolean isAlive = true;
    private int weight;
    private int health;
    private int age;

    @Override
    public void play() {

        reproduce();
    }

    @Override
    public void beEaten() {
        this.isAlive = false;
    }

//   TODO: think about simplify the code inside reproduce's method and remove duplicate code in Animal reproduce method
    @Override
    public GameObject reproduce() {
        final int MINIMUM_HEALTH_FOR_REPRODUCE = 15;
        OrganismFactory organismFactory = OrganismFactory.getInstance();
        if (getHealth() > MINIMUM_HEALTH_FOR_REPRODUCE && availableSpaceForPlant()) {
            GameObject plant = organismFactory.create(this.getClass());
            cell.addNewResident(this.getClass(), plant);
            return plant;
        } else {
            throw new IllegalArgumentException("Plant can't be reproduced into the cell");
        }
    }

    private boolean availableSpaceForPlant() {
        int amountOfPlants = getCell().getResidents().get(this.getClass()).size();
        return amountOfPlants < getMaxAmount();
    }
}
