package org.example.behaviour.animal;


import org.example.behaviour.generalBehaviorStatements.EatBehavior;
import org.example.behaviour.generalBehaviorStatements.MoveBehavior;
import org.example.behaviour.generalBehaviorStatements.ReproduceBehavior;
import org.example.entities.animals.abstractions.Animal;
import org.example.statistic.interfaces.Observer;
import org.example.statistic.interfaces.Subject;

import java.util.ArrayList;
import java.util.List;

public class AnimalBehaviour implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private final MoveBehavior moveBehavior;
    private final EatBehavior eatBehavior;
    private final ReproduceBehavior reproduceBehavior;

    public AnimalBehaviour() {
        this.moveBehavior = new MoveBehavior();
        this.eatBehavior = new EatBehavior();
        this.reproduceBehavior = new ReproduceBehavior();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {

    }

    public void act(Animal animal) {
        moveBehavior.move(animal);
        eatBehavior.findFood(animal);
        reproduceBehavior.reproduce(animal);
        animal.exchangeWeightToHealth();
        isAnimalAlive(animal);
    }

//    TODO 2024-12-01(added) need to change method by replacing in the Animal class or think about another way to implement it
    private void isAnimalAlive(Animal animal) {
        if (animal.getHealth() <= 0) {
            animal.setAlive(false);
            animal.getCell().removeGameObjectFromResidents(animal);
        }
        observers.forEach(Observer::updateDeath);
    }


}
