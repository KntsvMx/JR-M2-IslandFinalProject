package org.example.behaviour.generalBehaviorStatements;

import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.InteractableCell;
import org.example.entities.plants.Plant;
import org.example.factory.OrganismFactory;
import org.example.managers.CellManager;
import org.example.statistic.AbstractSubject;
import org.example.statistic.interfaces.StatsType;
import org.example.utils.SpaceUtil;

import java.util.List;
import java.util.Optional;

import static org.example.entities.animals.constants.AnimalConstants.HEALTH_AFTER_REPRODUCE;

public class ReproduceBehavior extends AbstractSubject {
    private final CellManager cellManager;
    private final OrganismFactory organismFactory;

    public ReproduceBehavior() {
        cellManager = CellManager.getInstance();
        organismFactory = OrganismFactory.getInstance();
    }

    public void reproduce(GameObject gameObject, InteractableCell cell) {
        if (gameObject instanceof Animal animal) {
            reproduceAnimal(animal, cell);
        } else if (gameObject instanceof Plant plantGameObject) {
            reproducePlant(plantGameObject, cell);
        }

    }

    private void reproduceAnimal(Animal animal, InteractableCell currentCell) {
        if (!SpaceUtil.availableSpaceForSpecie(currentCell, animal)) {
            return;
        }

        Optional<Animal> partnerOpt = findPartner(animal, currentCell);

        if (partnerOpt.isPresent()) {
            Animal partner = partnerOpt.get();

            if (canReproduce(animal, partner)) {
                GameObject baby = animal.reproduce();

                cellManager.addGameObject(currentCell, baby);

                animal.decreaseHealth(HEALTH_AFTER_REPRODUCE);
                partner.decreaseHealth(HEALTH_AFTER_REPRODUCE);

                notifyObservers(StatsType.BORN_ANIMALS, 1);
                notifyObservers(StatsType.CURRENT_ANIMALS, 1);
            }
        }

    }

    private void reproducePlant(Plant plantGameObject, InteractableCell currentCell) {
        if (SpaceUtil.availableSpaceForSpecie(currentCell, plantGameObject)) {
            GameObject newPlant = organismFactory.create(plantGameObject.getClass());
            cellManager.addGameObject(currentCell, newPlant);

            notifyObservers(StatsType.BORN_PLANT, 1);
            notifyObservers(StatsType.CURRENT_PLANTS, 1);
        }
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
}
