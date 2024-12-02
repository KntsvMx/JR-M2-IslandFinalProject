package org.example.behaviour.generalBehaviorStatements;

import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.Cell;
import org.example.entities.map.InteractableCell;
import org.example.managers.CellManager;

import java.util.List;
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

            if (animal.getHealth() >= 60 && availableSpaceForSpecie(randomCellFromClosest, maxAmount)) {
                animal.move(randomCellFromClosest);
                animal.changeHealthAfterAction();
            }
        } finally {
            lock.unlock();
        }
    }

//  TODO: 2024-12-01(added) need to refactor code to avoid code duplication in MoveBehavior and ReproduceBehavior classes
    private boolean availableSpaceForSpecie(InteractableCell sameSpecie, Integer maxAmount) {
        int amountOfSpecie = sameSpecie.getResidents().getOrDefault(sameSpecie.getClass(), List.of()).size();
        return amountOfSpecie < maxAmount;
    }
}

