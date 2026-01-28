package org.example.behaviour.animal;


import org.example.behaviour.generalBehaviorStatements.EatBehavior;
import org.example.behaviour.generalBehaviorStatements.MoveBehavior;
import org.example.behaviour.generalBehaviorStatements.ReproduceBehavior;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.Cell;
import org.example.entities.map.InteractableCell;


public class AnimalBehaviour {
    private final MoveBehavior moveBehavior;
    private final EatBehavior eatBehavior;
    private final ReproduceBehavior reproduceBehavior;

    public AnimalBehaviour() {
        this.moveBehavior = new MoveBehavior();
        this.eatBehavior = new EatBehavior();
        this.reproduceBehavior = new ReproduceBehavior();
    }

    public void act(Animal animal) {
        if (!animal.isAlive()) {
            return;
        }

        InteractableCell currentCell = animal.getCell();
        eatBehavior.eat(animal, currentCell);
        reproduceBehavior.reproduce(animal, currentCell);

        Cell targetCell = (Cell) currentCell.getRandomCellFromClosest();
        if (targetCell != null && targetCell != currentCell) {
            moveBehavior.move(animal, (Cell) currentCell, targetCell);
        }
        animal.reduceWeightPerTick();
        if (animal.getWeight() <= 0) {
            System.out.println(animal.getClass().getSimpleName() + " has died due to weight loss.");
            animal.checkDeath();
        }
    }
}
