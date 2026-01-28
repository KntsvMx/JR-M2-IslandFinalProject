package org.example.behaviour.generalBehaviorStatements;

import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.InteractableCell;
import org.example.entities.plants.Plant;
import org.example.factory.OrganismFactory;
import org.example.managers.CellManager;
import org.example.statistic.StatisticMonitor;
import org.example.statistic.interfaces.Observer;
import org.example.statistic.interfaces.Subject;
import org.example.utils.SpaceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReproduceBehavior implements Subject {
    private List<Observer> observers = new ArrayList<>();
    private final CellManager cellManager;
    private final OrganismFactory organismFactory;
    private final StatisticMonitor statisticMonitor;

    public ReproduceBehavior() {
        cellManager = CellManager.getInstance();
        organismFactory = OrganismFactory.getInstance();
        statisticMonitor = StatisticMonitor.getInstance();
        addObserver(statisticMonitor);
    }

    public void reproduce(GameObject gameObject, InteractableCell cell) {
        if (gameObject instanceof Animal animal) {
            reproduceAnimal(animal, cell);
        } else if (gameObject instanceof Plant plantGameObject) {
            reproducePlant(plantGameObject, cell);
        }

    }

    private void reproduceAnimal(Animal animal, InteractableCell currentCell) {
        if (!SpaceUtil.availableSpaceForSpecie(currentCell, animal.getLimits().getMaxAmount())) {
            return;
        }

        Optional<Animal> partnerOpt = findPartner(animal, currentCell);

        if (partnerOpt.isPresent()) {
            Animal partner = partnerOpt.get();

            if (canReproduce(animal, partner)) {
                GameObject baby = organismFactory.create(animal.getClass());

                currentCell.addGameObjectToResidents(baby.getClass(), baby);

                animal.decreaseHealthAfterReproduction();
                partner.decreaseHealthAfterReproduction();

                notifyObservers();
            }
        }

    }

    private void reproducePlant(Plant plantGameObject, InteractableCell currentCell) {
        if (SpaceUtil.availableSpaceForSpecie(currentCell, plantGameObject.getMaxAmount())) {
            GameObject newPlant = organismFactory.create(plantGameObject.getClass());
            cellManager.addGameObject(currentCell, newPlant);
        } else {
            throw new IllegalArgumentException("Plants not available");
        }
    }


    private boolean isMatureEnough(Animal animal, Animal sameSpecie) {
        int minReproductionAge = 2;
        //TODO: check this logic later
        return animal.getAge() >= minReproductionAge && sameSpecie.getAge() >= minReproductionAge;
    }


    private static boolean isEnoughHealth(Animal sameSpecie, Animal gameObject) {
        final int MINIMUM_HEALTH_FOR_REPRODUCE = 50;
        return gameObject.getHealth() > MINIMUM_HEALTH_FOR_REPRODUCE && sameSpecie.getHealth() > MINIMUM_HEALTH_FOR_REPRODUCE;
    }

    private Optional<Animal> findPartner(Animal seeker, InteractableCell cell) {
        List<GameObject> speciesList = cell.getResidents().get(seeker.getClass());

        if (speciesList == null) {
            return Optional.empty();
        }

        return speciesList.stream()
                .filter(obj -> obj instanceof Animal)
                .map(obj -> (Animal) obj)
                .filter(candidate -> candidate != seeker &&
                        candidate.isAlive() &&
                        candidate.getClass().equals(seeker.getClass())

                ).findAny();
    }

    private boolean canReproduce(Animal parent1, Animal parent2) {
        int minHealth = 50;

        return parent1.getHealth() >= minHealth && parent2.getHealth() >= minHealth;
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
        observers.forEach(Observer::updateBorn);
    }
}
