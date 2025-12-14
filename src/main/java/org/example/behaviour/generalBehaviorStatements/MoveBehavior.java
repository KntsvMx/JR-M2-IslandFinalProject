package org.example.behaviour.generalBehaviorStatements;

import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.Cell;
import org.example.entities.map.InteractableCell;
import org.example.managers.CellManager;
import org.example.utils.SpaceUtil;

import java.util.concurrent.locks.ReentrantLock;

public class MoveBehavior {
    private final CellManager cellManager;

    public MoveBehavior() {
        cellManager = CellManager.getInstance();
    }

    public void move(Animal animal) {
        try {
            InteractableCell currentCell = animal.getCell();
//            TODO: 2025-08-05 (added) Add randomness for the next cell, so Animal could stay in the same cell
            Cell randomCellFromClosest = cellManager.getRandomCellFromClosest(currentCell);
            int maxAmount = animal.getLimits().getMaxAmount();

            if (animal.getHealth() >= 60 && SpaceUtil.availableSpaceForSpecie(randomCellFromClosest, maxAmount)) {
                animal.move(randomCellFromClosest);
                animal.changeHealthAfterMove();
            }
        } finally {

        }
    }
}

