package org.example.behaviour.generalBehaviorStatements;

import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.InteractableCell;
import org.example.managers.CellManager;
import org.example.utils.SpaceUtil;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static org.example.entities.animals.constants.AnimalConstants.HEALTH_AFTER_MOVE;

public class MoveBehavior {
    private static final CellManager cellManager = CellManager.getInstance();

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
                boolean isHere = cellManager.containsGameObject(fromCell, animal);
                executeMovementIfValid(animal, fromCell, toCell, hasSpace, isHere);
            } finally {
                targetLock.unlock();
            }
        }
    }

    private static void executeMovementIfValid(Animal animal, InteractableCell fromCell, InteractableCell toCell, boolean hasSpace, boolean isHere) {
        if (hasSpace && isHere) {
            cellManager.removeGameObject(fromCell, animal);
            cellManager.addGameObject(toCell, animal);

//            TODO: move it to DeathService.
            animal.decreaseHealth(HEALTH_AFTER_MOVE);
        }
    }
}

