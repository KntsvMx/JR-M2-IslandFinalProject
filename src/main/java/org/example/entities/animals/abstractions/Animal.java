package org.example.entities.animals.abstractions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.abstraction.interfaces.GameObject;
import org.example.entities.interfaces.Eatable;
import org.example.entities.interfaces.Movable;
import org.example.entities.interfaces.Organism;
import org.example.entities.limits.Limits;
import org.example.entities.map.Cell;
import org.example.entities.plants.Plant;
import org.example.entities.target.Target;

import java.util.List;
import java.util.Map;
import java.util.Random;

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
        findFood();
    }

    public void move() {
        Cell currentCell;
        if (isEnoughHealth()) {
            currentCell = cell.getRandomCell();
            currentCell.addNewResident(getObjectClass(), getGameObject());
            changeHealthAfterMove();
            cell.removeGameObjectFromCell(this);
        }
    }

    private void changeHealthAfterMove() {
        health = (health * 5) / 100;
    }

    private boolean isEnoughHealth() {
        return health >= 40;
    }

    private GameObject getGameObject() {
        List<GameObject> gameObjects = cell.getResidents().get(getObjectClass());
        return gameObjects.get(gameObjects.indexOf(this));
    }

    private Class<? extends GameObject> getObjectClass() {
        return this.getClass();
    }

    private void eat(List<GameObject> potentialFood, Integer targetValue) {
        Random random = new Random();
        int randomIndex = random.nextInt(100);

        GameObject gameObject = potentialFood.stream()
                .filter(obj -> obj instanceof Eatable)
                .findAny()
                .orElse(null);

        if (gameObject == null) {
            throw new IllegalArgumentException("Game object not found");
        }
        if (randomIndex >= targetValue) {
            Eatable eatenObject = (Eatable) gameObject;
            eatenObject.beEaten();
            this.weight += calculateNutritionalValue(gameObject);
        }
    }

    private int calculateNutritionalValue(GameObject gameObject) {
        if (gameObject instanceof Animal) {
            return ((Animal) gameObject).getWeight();
        }
        if (gameObject instanceof Plant) {
            return ((Plant) gameObject).getWeight();
        }
        return 0;
    }

    public void findFood() {
        Map<Class<? extends GameObject>, List<GameObject>> residents = getCell().getResidents();
        Map<Class<? extends GameObject>, Integer> targets = getTarget().getTargetMatrix();


        for (Map.Entry<Class<? extends GameObject>, Integer> target : targets.entrySet()) {
            List<GameObject> potentialFood = residents.get(target.getKey());
            Integer targetValue = target.getValue();
            if (potentialFood != null && !potentialFood.isEmpty()) {
                eat(potentialFood, targetValue);
                return;
            }
        }
    }

    @Override
    public void beEaten() {
        this.setAlive(false);
    }
}
