package org.example.behaviour.generalBehaviorStatements;

import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.Cell;
import org.example.entities.map.InteractableCell;
import org.example.managers.CellManager;

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

//          TODO: 2024-11-28(added) need to add check for available space in cell
            if (animal.getHealth() >= 60) {
                Cell randomCellFromClosest = cellManager.getRandomCellFromClosest(currentCell);
                animal.move(randomCellFromClosest);
                animal.changeHealthAfterAction();
            }
        } finally {
            lock.unlock();
        }
    }
}

