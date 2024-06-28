package org.example.managers;

public class EntityBehaviorManager {
    private static EntityBehaviorManager instance;

    private EntityBehaviorManager() {

    }

    public static EntityBehaviorManager getInstance() {
        if (instance == null) {
            instance = new EntityBehaviorManager();
        }
        return instance;
    }
}
