package org.example.entities.interfaces;

import org.example.abstraction.interfaces.GameObject;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Eatable {
    void eat(Optional<Map.Entry<Class<? extends GameObject>, Integer>> matchingTarget, List<GameObject> gameObjects);

    void findFood();
}
