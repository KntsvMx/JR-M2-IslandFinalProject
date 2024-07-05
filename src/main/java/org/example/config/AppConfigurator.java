package org.example.config;

import org.example.entities.map.GameField;
import org.example.managers.EntityBehaviorManager;
import org.example.managers.EntityLoadManager;
import org.example.managers.IslandManager;


public class AppConfigurator {
    private static AppConfigurator instance;
    private final EntityLoadManager loadManager;
    private final EntityBehaviorManager behaviorManager;
    private final IslandManager islandManager;
    private GameField gameField;

    private AppConfigurator() {
        loadManager = EntityLoadManager.getInstant();
        behaviorManager = EntityBehaviorManager.getInstance();
        islandManager = IslandManager.getInstance();
    }

    public static AppConfigurator getInstance() {
        if (instance == null) {
            instance = new AppConfigurator();
        }
        return instance;
    }

    public void configure() {
        loadManager.loadPrototypes();
        gameField = loadManager.loadMapPrototype();
        islandManager.init(gameField);
        behaviorManager.init();
    }
}
