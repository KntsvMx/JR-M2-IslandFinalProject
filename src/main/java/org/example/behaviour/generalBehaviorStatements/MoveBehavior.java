package org.example.behaviour.generalBehaviorStatements;

import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.InteractableCell;

public class MoveBehavior {

    public void move(Animal animal) {
        InteractableCell nextCell;

        if (animal.getHealth() >= 60) {
            nextCell = animal.getCell().getRandomCellFromClosest();
            animal.move(nextCell);
            animal.changeHealthAfterAction();
        }
    }
}
