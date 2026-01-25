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

    public void move(Animal animal, Cell fromCell, Cell toCell) {
        ReentrantLock firstLock;
        ReentrantLock secondLock;

        if (System.identityHashCode(fromCell) < System.identityHashCode(toCell)) {
            firstLock = fromCell.getLock();
            secondLock = toCell.getLock();
        } else {
            firstLock = toCell.getLock();
            secondLock = fromCell.getLock();
        }

        firstLock.lock();
        try {
            secondLock.lock();
            try {
                boolean hasSpace = SpaceUtil.availableSpaceForSpecie(toCell, animal.getLimits().getMaxAmount());
                boolean isHere = fromCell.getResidents().contains(animal);

                if (hasSpace && isHere) {
                   fromCell.removeGameObjectFromResidents(animal);
                   toCell.addGameObjectToResidents(animal.getClass(), animal);

                   animal.setCell(toCell);

                   animal.changeHealthAfterMove();
                }
            } finally {
                secondLock.unlock();
            }
        } finally {
            firstLock.unlock();
        }
    }
}

