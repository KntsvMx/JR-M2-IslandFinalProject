package org.example.behaviour.general;

import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.InteractableCell;

public class MoveBehavior {


    public void move(Animal animal) {
        InteractableCell currentCell = animal.getCell();
        InteractableCell nextCell;
//      TODO: Change health to getHealth()

        if (animal.getHealth() >= 60) {
            nextCell = animal.getCell().getRandomCellFromClosest();
            nextCell.addGameObjectToResidents(animal.getClass(), animal);
            animal.changeHealthAfterAction();
            currentCell.removeGameObjectFromResidents(animal);
        }
    }
}
