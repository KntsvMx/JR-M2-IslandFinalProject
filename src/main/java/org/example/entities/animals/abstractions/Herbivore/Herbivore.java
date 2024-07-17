package org.example.entities.animals.abstractions.Herbivore;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.abstraction.interfaces.GameObject;
import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.interfaces.Eatable;

import java.util.*;

@SuperBuilder
@NoArgsConstructor
public abstract class Herbivore extends Animal implements Eatable {
    @Override
    public void play() {
        super.play();
        findFood();
    }

    @Override
    public void eat(Optional<Map.Entry<Class<? extends GameObject>, Integer>> matchingTarget, List<GameObject> gameObjects) {
        if (matchingTarget.isEmpty()) {
            throw new IllegalArgumentException("Matching target is null");
        }

        Random random = new Random();
        Map.Entry<Class<? extends GameObject>, Integer> target = matchingTarget.get();
//        Class<? extends GameObject> targetClass = target.getKey();
        Integer targetValue = target.getValue();
        Animal gameObject = (Animal) gameObjects.stream().findAny().get();
        int weight = getWeight();

        if (random.nextInt(100) >= targetValue) {
            weight = weight + gameObject.getWeight();
            gameObject.setAlive(false);
            this.setWeight(weight);
            gameObject.removeGameObjectFromCell();
        }
    }

//    TODO: Отрефакторить будущее дублирование кода findFood() в Herbivore and Predator

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
}
