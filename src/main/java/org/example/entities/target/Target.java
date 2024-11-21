package org.example.entities.target;

import lombok.Data;
import org.example.abstraction.interfaces.GameObject;
import org.example.config.GameObjectScanner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class Target {
    private Map<String, Integer> stringTargetMatrix = new HashMap<>();
    private Map<Class<? extends GameObject>, Integer> targetMatrix = new HashMap<>();
    private GameObjectScanner gameObjectScanner;

    public Target() {
        gameObjectScanner = GameObjectScanner.getInstance();
    }

    private Map<Class<? extends GameObject>, Integer> transformToTargetMatrix() {
        Map<Class<? extends GameObject>, Integer> targetMatrix = new HashMap<>();
        Set<Class<? extends GameObject>> gameObjectClasses;
        if (stringTargetMatrix != null) {
            for (Map.Entry<String, Integer> target : stringTargetMatrix.entrySet()) {
                String targetKey = target.getKey();
                Integer targetValue = target.getValue();
                // TODO: getMatchGameObjectClasses don't return set of game objects which use in targetMatrix
                gameObjectClasses = getMatchGameObjectClasses(targetKey);
                for (Class<? extends GameObject> gameObjectClass : gameObjectClasses) {
                    targetMatrix.put(gameObjectClass, targetValue);
                }
            }
        }
        return targetMatrix;
    }

    private Set<Class<? extends GameObject>> getMatchGameObjectClasses(String target) {
        return gameObjectScanner.getAllGameObjectClasses()
                .stream()
                .filter(c -> (c.getSimpleName().equals(target)))
                .collect(Collectors.toSet());
    }

    public Map<Class<? extends GameObject>, Integer> getTargetMatrix() {
        targetMatrix = transformToTargetMatrix();
        return targetMatrix;
    }
}
