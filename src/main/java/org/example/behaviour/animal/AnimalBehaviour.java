package org.example.behaviour.animal;


import org.example.behaviour.generalBehaviorStatements.EatBehavior;
import org.example.behaviour.generalBehaviorStatements.MoveBehavior;
import org.example.behaviour.generalBehaviorStatements.ReproduceBehavior;
import org.example.entities.animals.abstractions.Animal;


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
//        if (animal.getConsecutiveActions() >= MAX_CONSECUTIVE_ACTIONS) {
//            animal.recoverHealth();
//            animal.resetConsecutiveActions();
//            return;
//        }

        // Check if animal is too weak for actions
        if (animal.getHealth() < 20) {
            animal.recoverHealth();
            return;
        }

        moveBehavior.move(animal);
        eatBehavior.findFood(animal);
        reproduceBehavior.reproduce(animal);
        animal.exchangeWeightToHealth();
        animal.checkDeath();

        animal.incrementConsecutiveActions();
    }
}
