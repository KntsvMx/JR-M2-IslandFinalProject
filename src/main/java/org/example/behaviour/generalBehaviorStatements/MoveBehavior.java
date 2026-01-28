package org.example.behaviour.generalBehaviorStatements;

import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.Cell;
import org.example.managers.CellManager;
import org.example.utils.SpaceUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class MoveBehavior {
    private final CellManager cellManager;

    public MoveBehavior() {
        cellManager = CellManager.getInstance();
    }

    public void move(Animal animal, Cell fromCell, Cell toCell) {
        ReentrantLock targetLock = toCell.getLock();
        boolean gotLock = false;

        try {
            gotLock = targetLock.tryLock(50, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }


        if (gotLock) {
            try {
                boolean hasSpace = SpaceUtil.availableSpaceForSpecie(toCell, animal.getLimits().getMaxAmount());
                boolean isHere = fromCell.getResidents().containsValue(animal);

                if (hasSpace && isHere) {
                    fromCell.removeGameObjectFromResidents(animal);
                    toCell.addGameObjectToResidents(animal.getClass(), animal);

                    animal.setCell(toCell);

                    animal.changeHealthAfterMove();
                }
            } finally {
                targetLock.unlock();
            }
        }
    }
}

