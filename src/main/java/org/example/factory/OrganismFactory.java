package org.example.factory;

import org.example.abstraction.interfaces.GameObject;

import java.util.HashMap;
import java.util.Map;

public class OrganismFactory implements PrototypeFactory<GameObject> {
    private static OrganismFactory instance;
    private final Map<Class<? extends GameObject>, GameObject> prototypes = new HashMap<>();
    
    private OrganismFactory() {
        // Private constructor to prevent instantiation
    }

    public static OrganismFactory getInstance() {
        if (instance == null) {
            instance = new OrganismFactory();
        }
        return instance;
    }

    public void registerPrototype(GameObject prototype) {
        prototypes.put(prototype.getClass(), prototype);
    }

    @Override
    public GameObject create(Class<? extends GameObject> type) {
        if (!prototypes.containsKey(type)) {
            throw new IllegalArgumentException("No prototype registered for type: " + type.getName());
        }
        // TODO: change reproduce() to copy() in the next line
        return prototypes.get(type).copy();
    }

    public Map<Class<? extends GameObject>, GameObject> getPrototypes() {
        return prototypes;
    }
}
