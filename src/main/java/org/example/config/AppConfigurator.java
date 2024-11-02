package org.example.config;

import org.example.entities.map.GameField;
import org.example.managers.EntityBehaviorManager;
import org.example.managers.EntityLoadManager;
import org.example.managers.CellManager;


public class AppConfigurator {
    private static AppConfigurator instance;
    private final EntityLoadManager loadManager;
    private final EntityBehaviorManager behaviorManager;
    private final CellManager cellManager;

    private AppConfigurator() {
        loadManager = EntityLoadManager.getInstant();
        behaviorManager = EntityBehaviorManager.getInstance();
        cellManager = CellManager.getInstance();
    }

    public static AppConfigurator getInstance() {
        if (instance == null) {
            instance = new AppConfigurator();
        }
        return instance;
    }

//    TODO: replace instead of returning gameField to loadManager.loadMapPrototype, otherwise islandManager await gameField that loadMap return;
    public void configure() {
        loadManager.loadPrototypes();
        GameField gameField = loadManager.loadMapPrototype();
        cellManager.init(gameField);
        behaviorManager.init(gameField);
    }
}
