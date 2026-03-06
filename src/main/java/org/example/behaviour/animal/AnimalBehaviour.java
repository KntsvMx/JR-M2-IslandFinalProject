package org.example.behaviour.animal;


import org.example.behaviour.generalBehaviorStatements.EatBehavior;
import org.example.behaviour.generalBehaviorStatements.MoveBehavior;
import org.example.behaviour.generalBehaviorStatements.ReproduceBehavior;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.Cell;
import org.example.entities.map.InteractableCell;
import org.example.statistic.AbstractSubject;
import org.example.statistic.interfaces.StatsType;


public class AnimalBehaviour extends AbstractSubject {
    private final MoveBehavior moveBehavior;
    private final EatBehavior eatBehavior;
    private final ReproduceBehavior reproduceBehavior;

    public AnimalBehaviour(MoveBehavior moveBehavior, EatBehavior eatBehavior, ReproduceBehavior reproduceBehavior) {
//        TODO: Refactor this to use dependency injection, and make it more flexible to add new behaviors in the future
        this.moveBehavior = moveBehavior;
        this.eatBehavior = eatBehavior;
        this.reproduceBehavior = reproduceBehavior;
    }

    public void act(Animal animal) {
        if (!animal.isAlive()) {
            return;
        }
        animal.reduceWeightPerTick();

        InteractableCell currentCell = animal.getCell();


        eatBehavior.eat(animal, currentCell);
        if (checkAndProcessDeath(animal, currentCell)) {
            return;
        }
        reproduceBehavior.reproduce(animal, currentCell);

        Cell targetCell = (Cell) currentCell.getRandomCellFromClosest();
        if (targetCell != null && targetCell != currentCell) {
            moveBehavior.move(animal, (Cell) currentCell, targetCell);
        }
    }


    private boolean checkAndProcessDeath(Animal animal, InteractableCell cell) {
        if (animal.isStarving() || animal.isFatallyInjured()) {
            animal.setAlive(false);
            cell.removeGameObjectFromResidents(animal);

            notifyObservers(StatsType.DIED_ANIMALS, 1);
            notifyObservers(StatsType.CURRENT_ANIMALS, -1);

            return true;
        }
        return false;
    }
}
