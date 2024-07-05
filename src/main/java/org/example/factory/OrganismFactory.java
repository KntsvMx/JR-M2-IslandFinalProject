package org.example.factory;

import lombok.Getter;
import org.example.abstraction.interfaces.GameObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OrganismFactory implements PrototypeFactory<GameObject> {
    private static OrganismFactory instance;
    private final Map<Class<? extends GameObject>, GameObject> prototypes = new HashMap<>();
    private OrganismFactory() {

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
            throw new IllegalArgumentException();
        }
        return prototypes.get(type).reproduce();
    }

    public Set<Class<? extends GameObject>> getPrototypes() {
        return prototypes.keySet();
    }
}
