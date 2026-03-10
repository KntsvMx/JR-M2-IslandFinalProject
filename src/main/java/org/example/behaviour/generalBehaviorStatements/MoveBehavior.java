package org.example.behaviour.generalBehaviorStatements;

import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.InteractableCell;
import org.example.utils.SpaceUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static org.example.entities.animals.constants.AnimalConstants.HEALTH_AFTER_MOVE;

//TODO: fix bug with move when target cell isn't locked which can cause of ConcurrentModificationException

public class MoveBehavior {

    public void move(Animal animal, InteractableCell fromCell, InteractableCell toCell) {
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
                boolean hasSpace = SpaceUtil.availableSpaceForSpecie(toCell, animal);
//                TODO: optimize this check by change method from.getResidents() to CellManager
                boolean isHere = fromCell.getResidents().get(animal.getClass()).contains(animal);
                executeMovementIfValid(animal, fromCell, toCell, hasSpace, isHere);
            } finally {
                targetLock.unlock();
            }
        }
    }

    private static void executeMovementIfValid(Animal animal, InteractableCell fromCell, InteractableCell toCell, boolean hasSpace, boolean isHere) {
        if (hasSpace && isHere) {
            fromCell.removeGameObjectFromResidents(animal);
            toCell.addGameObjectToResidents(animal.getClass(), animal);

//                  TODO: optimize this by change method animal.setCell() to CellManager
            animal.setCell(toCell);

            animal.decreaseHealth(HEALTH_AFTER_MOVE);
        }
    }
}

