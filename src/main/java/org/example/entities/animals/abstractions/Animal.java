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
import org.example.entities.target.Target;

import java.util.*;

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
            removeGameObjectFromCell();
        }
    }

    @Override
    public void findFood() {
        Map<Class<? extends GameObject>, List<GameObject>> residents = getCell().getResidents();
        Map<Class<? extends GameObject>, Integer> targets = getTarget().getTargetMatrix();
        Optional<Map.Entry<Class<? extends GameObject>, Integer>> matchingTarget = Optional.empty();
        List<GameObject> gameObjects = new ArrayList<>();

        for (Map.Entry<Class<? extends GameObject>, List<GameObject>> resident : residents.entrySet()) {
            matchingTarget = targets.entrySet()
                    .stream()
                    .filter(entry -> entry.getKey().equals(resident.getKey()))
                    .findFirst();
            gameObjects = resident.getValue();

        }

        eat(matchingTarget, gameObjects);
    }

    @Override
    public void eat(Optional<Map.Entry<Class<? extends GameObject>, Integer>> matchingTarget, List<GameObject> gameObjects) {
        isNullMatchingTarget(matchingTarget);

        Map.Entry<Class<? extends GameObject>, Integer> target = matchingTarget.get();
        Integer targetValue = target.getValue();
        Animal gameObject = (Animal) gameObjects.stream().findAny().get();
        int weight = getWeight();

        if (doesCatchTarget(targetValue)) {
            updateTargetStatus(weight, gameObject);
        }
    }

    private static void isNullMatchingTarget(Optional<Map.Entry<Class<? extends GameObject>, Integer>> matchingTarget) {
        if (matchingTarget.isEmpty()) {
            throw new IllegalArgumentException("Matching target is null");
        }
    }

    private static boolean doesCatchTarget(Integer targetValue) {
        Random random = new Random();
        return random.nextInt(100) >= targetValue;
    }

    private void updateTargetStatus(int weight, Animal gameObject) {
        weight = weight + gameObject.getWeight();
        gameObject.setAlive(false);
        this.setWeight(weight);
        gameObject.removeGameObjectFromCell();
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

    public void removeGameObjectFromCell() {
        cell.getResidents().get(getObjectClass()).remove(getGameObject());
    }

}
