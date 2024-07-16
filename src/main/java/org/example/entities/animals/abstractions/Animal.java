package org.example.entities.animals.abstractions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.abstraction.interfaces.GameObject;
import org.example.entities.interfaces.Movable;
import org.example.entities.interfaces.Organism;
import org.example.entities.limits.Limits;
import org.example.entities.map.Cell;
import org.example.entities.target.Target;

import java.util.List;
import java.util.Random;

@NoArgsConstructor
@SuperBuilder
@Getter
@EqualsAndHashCode
@ToString

public abstract class Animal implements Organism, Movable {
    private static long serialUID = 1L;

    @Builder.Default
    private final long UID = serialUID++;

    @JsonIgnore
    private Cell cell;

    private Limits limits;
    private Target target;

    private String icon;
    private boolean isAlive = true;
    private int weight;
    private int health;
    private int age;

    public void play() {
        move();
    }

    public void move() {
        Cell currentCell;
        if (isEnoughHealth()) {
            currentCell = cell.getRandomCell();
            currentCell.addNewResident(getObjectClass(), getGameObject());
            removeGameObject();
        }
    }

    private boolean isEnoughHealth() {
        if (health >= 40) {
            return true;
        } else {
            return false;
        }
    }

    private GameObject getGameObject() {
        List<GameObject> gameObjects = cell.getResidents().get(getObjectClass());
        return gameObjects.get(gameObjects.indexOf(this));
    }

    private Class<? extends GameObject> getObjectClass() {
        return this.getClass();
    }

    private void removeGameObject() {
        cell.getResidents().get(getObjectClass()).remove(getGameObject());
    }
}
