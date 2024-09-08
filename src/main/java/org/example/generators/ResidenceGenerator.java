package org.example.generators;

import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.limits.Limits;
import org.example.entities.plants.Plant;
import org.example.factory.OrganismFactory;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ResidenceGenerator {
    private static ResidenceGenerator instance;
    private final OrganismFactory organismFactory;

    private ResidenceGenerator() {
        organismFactory = OrganismFactory.getInstance();
    }

    public static ResidenceGenerator getInstance() {
        if (instance == null) {
            instance = new ResidenceGenerator();
        }
        return instance;
    }

    public Map<Class<? extends GameObject>, List<GameObject>> generateNewResidence() {
        Map<Class<? extends GameObject>, List<GameObject>> residents = new ConcurrentHashMap<>();
        Set<Class<? extends GameObject>> prototypes = organismFactory.getPrototypes();
        List<GameObject> organisms = new CopyOnWriteArrayList<>();
        Random randomAmount = new Random();
        int maxCount = 0;
        int count;


        for (Class<? extends GameObject> prototype : prototypes) {
            GameObject gameObjectPrototypeInstance = organismFactory.create(prototype);
            count = randomAmount.nextInt(maxCount + 1);
            Limits limits = null;

            if (gameObjectPrototypeInstance instanceof Animal animal) {
                limits = animal.getLimits();
            } else if (gameObjectPrototypeInstance instanceof Plant plant) {
                maxCount = plant.getMaxAmount();
            }

            if (limits != null) {
                maxCount = limits.getMaxAmount();
            }

            for (int i = 0; i < count; i++) {
                organisms.add(organismFactory.create(prototype));
            }
            residents.put(prototype, organisms);

        }

        return residents;
    }
}
