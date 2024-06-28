package org.example.managers;

public class IslandManager {
    private static IslandManager instance;

    private IslandManager() {

    }

    public static IslandManager getInstance() {
        if (instance == null) {
            instance = new IslandManager();
        }
        return instance;
    }
}
