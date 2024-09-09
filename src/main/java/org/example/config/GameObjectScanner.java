package org.example.config;

import org.example.abstraction.annotations.Config;
import org.example.abstraction.annotations.GameObjectEntity;
import org.example.abstraction.interfaces.GameObject;
import org.example.entities.map.GameField;
import org.example.exceptions.InitGameException;
import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

public class GameObjectScanner {
    private static GameObjectScanner instance;
    private final Reflections reflections = new Reflections("org.example");

    private GameObjectScanner() {

    }

    public static GameObjectScanner getInstance() {
        if (instance == null) {
            instance = new GameObjectScanner();
        }
        return instance;
    }

    public Set<Class<? extends GameObject>> getAllGameObjectClasses() {
        return reflections.getSubTypesOf(GameObject.class)
                .stream()
                .filter(c -> c.isAnnotationPresent(GameObjectEntity.class))
                .filter(c -> c.isAnnotationPresent(Config.class))
                .collect(Collectors.toSet());
    }

    public Class<GameField> getGameFieldClass() {
        return reflections.getTypesAnnotatedWith(Config.class)
                .stream()
                .filter(GameField.class::equals)  // Проверяем, что класс — это сам GameField
                .map(c -> (Class<GameField>) c)
                .findFirst()
                .orElseThrow(() -> new InitGameException("GameField must have @Config annotation"));
    }


}
