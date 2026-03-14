package org.example.behaviour.animal;


import org.example.behaviour.generalBehaviorStatements.EatBehavior;
import org.example.behaviour.generalBehaviorStatements.MoveBehavior;
import org.example.behaviour.generalBehaviorStatements.ReproduceBehavior;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.InteractableCell;
import org.example.managers.CellManager;
import org.example.managers.DeathManager;
import org.example.statistic.AbstractSubject;


public class AnimalBehaviour extends AbstractSubject {
    private final MoveBehavior moveBehavior;
    private final EatBehavior eatBehavior;
    private final ReproduceBehavior reproduceBehavior;
    private final CellManager cellManager = CellManager.getInstance();

    public AnimalBehaviour(MoveBehavior moveBehavior, EatBehavior eatBehavior, ReproduceBehavior reproduceBehavior) {
        this.moveBehavior = moveBehavior;
        this.eatBehavior = eatBehavior;
        this.reproduceBehavior = reproduceBehavior;
    }

    public void act(Animal animal) {
        if (!animal.isAlive()) {
            return;
        }
        InteractableCell currentCell = cellManager.getAnimalCell(animal);
        InteractableCell targetCell = cellManager.getRandomCellFromClosest(currentCell);

        if (animal.metabolize()) {
            DeathManager.getInstant().registerDeath(animal, currentCell);
            return;
        }

        eatBehavior.eat(animal, currentCell);
        if (!animal.isAlive()) return;
        animal.recoverHealth();
        reproduceBehavior.reproduce(animal, currentCell);
        if (!animal.isAlive()) return;
        attemptToMove(animal, targetCell, currentCell);

    }

    private void attemptToMove(Animal animal, InteractableCell targetCell, InteractableCell currentCell) {
        if (targetCell != null && targetCell != currentCell) {
            moveBehavior.move(animal, currentCell, targetCell);
        }
    }

}
