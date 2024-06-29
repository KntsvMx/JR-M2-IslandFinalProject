package org.example.managers;

import org.example.config.GameObjectScanner;
import org.example.config.PrototypeLoader;
import org.example.entities.map.GameField;
import org.example.factory.OrganismFactory;

public class EntityLoadManager {
    private static EntityLoadManager instance;
    private GameObjectScanner gameObjectScanner;
    private PrototypeLoader prototypeLoader;
    private OrganismFactory organismFactory;

    private EntityLoadManager() {
        gameObjectScanner = GameObjectScanner.getInstance();
        prototypeLoader = PrototypeLoader.getInstance();
        organismFactory = OrganismFactory.getInstance();
    }

    public static EntityLoadManager getInstant() {
        if (instance == null) {
            instance = new EntityLoadManager();
        }
        return instance;
    }

    public void loadPrototypes() {
        gameObjectScanner
                .getAllGameObjectClasses()
                .stream()
                .map(prototypeLoader::loadPrototype)
                .forEach(organismFactory::registerPrototype);
    }

    public GameField loadMapPrototype() {
        Class<? extends GameField> gameFieldClass = gameObjectScanner.getGameFieldClass();
        return prototypeLoader.loadPrototype(gameFieldClass);
    }

}
