package org.example.behaviour.general;

import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.interfaces.Eatable;
import org.example.entities.plants.Plant;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class EatBehavior {


    public void findFood(Animal animal) {
        Map<Class<? extends GameObject>, List<GameObject>> residents = animal.getCell().getResidents();
        Map<Class<? extends GameObject>, Integer> targets = animal.getTarget().getTargetMatrix();

//    TODO: Probably available to change to lambda expression

        for (Map.Entry<Class<? extends GameObject>, Integer> target : targets.entrySet()) {
            List<GameObject> potentialFood = residents.get(target.getKey());
            Integer targetValue = target.getValue();
            if (potentialFood != null && !potentialFood.isEmpty()) {
                eat(animal, potentialFood, targetValue);
                return;
            }
        }
    }

    public void eat(Animal animal, List<GameObject> potentialFood, Integer targetValue) {
        Random random = new Random();
        int randomIndex = random.nextInt(100);

        GameObject gameObject = findAnyFirstPotentialFood(potentialFood);

        if (gameObject == null) {
            throw new IllegalArgumentException("Game object not found");
        }
        if (randomIndex >= targetValue) {
            Eatable eatenObject = (Eatable) gameObject;
            eatenObject.beEaten();

            animal.setWeight(calculateNutritionalValue(gameObject));
//            TODO: Realize remove eatenObject animal or grass from the cell
//            removeGameObject(gameObject);
        } else {
            animal.changeHealthAfterAction();
            animal.setWeight(calculateNutritionalValue(gameObject) / 2);
        }
    }

    private static @Nullable GameObject findAnyFirstPotentialFood(List<GameObject> potentialFood) {
        return potentialFood.stream()
                .filter(obj -> obj instanceof Eatable)
                .findAny()
                .orElse(null);
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




}
