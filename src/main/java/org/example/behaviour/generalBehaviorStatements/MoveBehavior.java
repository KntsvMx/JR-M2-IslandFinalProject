package org.example.behaviour.generalBehaviorStatements;

import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.Cell;
import org.example.entities.map.InteractableCell;
import org.example.managers.CellManager;
import org.example.utils.SpaceUtil;

import java.util.concurrent.locks.ReentrantLock;

public class MoveBehavior {
    private final CellManager cellManager;
    private final ReentrantLock lock = new ReentrantLock();

    public MoveBehavior() {
        cellManager = CellManager.getInstance();
    }

    public void move(Animal animal) {
        lock.lock();
        try {
            InteractableCell currentCell = animal.getCell();
            Cell randomCellFromClosest = cellManager.getRandomCellFromClosest(currentCell);
            int maxAmount = animal.getLimits().getMaxAmount();

            if (animal.getHealth() >= 60 && SpaceUtil.availableSpaceForSpecie(randomCellFromClosest, maxAmount)) {
                animal.move(randomCellFromClosest);
                animal.changeHealthAfterMove();
            }
        } finally {
            lock.unlock();
        }
    }
}

