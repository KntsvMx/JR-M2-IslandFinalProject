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

    private Map<Class<? extends GameObject>, Integer> buildClassMatrix() {
        Map<Class<? extends GameObject>, Integer> targetMatrix = new HashMap<>();
        if (stringTargetMatrix != null) {
            processMatrixConstruction(targetMatrix);
        }
        return targetMatrix;
    }

    private void processMatrixConstruction(Map<Class<? extends GameObject>, Integer> targetMatrix) {
        Set<Class<? extends GameObject>> gameObjectClasses;
        for (Map.Entry<String, Integer> target : stringTargetMatrix.entrySet()) {
            String targetClassName = target.getKey();
            Integer eatProbability = target.getValue();
            gameObjectClasses = getMatchGameObjectClasses(targetClassName);
            for (Class<? extends GameObject> gameObjectClass : gameObjectClasses) {
                targetMatrix.put(gameObjectClass, eatProbability);
            }
        }
    }

    private Set<Class<? extends GameObject>> getMatchGameObjectClasses(String target) {
        return gameObjectScanner.getAllGameObjectClasses()
                .stream()
                .filter(c -> (c.getSimpleName().equalsIgnoreCase(target)))
                .collect(Collectors.toSet());
    }

    public Map<Class<? extends GameObject>, Integer> getTargetMatrix() {
        if(targetMatrix == null || targetMatrix.isEmpty()) {
            targetMatrix = buildClassMatrix();
        }
        return targetMatrix;
    }
}
