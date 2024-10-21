package org.example.managers;

import org.example.behaviour.animal.AnimalBehaviour;
import org.example.behaviour.plant.PlantBehaviour;
import org.example.entities.map.GameField;

//      TODO: Realize multithreading in this class
public class EntityBehaviorManager {
    private static EntityBehaviorManager instance;
    private final AnimalBehaviour animalBehaviour;
    private final PlantBehaviour plantBehaviour;

    private EntityBehaviorManager() {
        animalBehaviour = new AnimalBehaviour();
        plantBehaviour = new PlantBehaviour();
    }

    public static EntityBehaviorManager getInstance() {
        if (instance == null) {
            instance = new EntityBehaviorManager();
        }
        return instance;
    }

    public void init(GameField gameField) {

    }
}
