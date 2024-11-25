package org.example.behaviour.generalBehaviorStatements;

import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.InteractableCell;
import org.example.managers.CellManager;

public class MoveBehavior {
    private final CellManager cellManager;

    public MoveBehavior() {
        cellManager = CellManager.getInstance();
    }

    public void move(Animal animal) {
        synchronized (animal) {
            InteractableCell nextCell;
            InteractableCell currentCell = animal.getCell();

            if (animal.getHealth() >= 60) {
                nextCell = animal.getCell().getRandomCellFromClosest();
                cellManager.addGameObject(nextCell, animal);
                cellManager.removeGameObject(currentCell, animal);
                animal.changeHealthAfterAction();
            }
        }
    }
}
