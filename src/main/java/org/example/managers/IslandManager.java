package org.example.managers;

import org.example.entities.map.Cell;
import org.example.entities.map.GameField;
import org.example.generators.CellGenerator;

public class IslandManager {
    private static IslandManager instance;
    private CellGenerator cellGenerator;

    private IslandManager() {

    }

    public static IslandManager getInstance() {
        if (instance == null) {
            instance = new IslandManager();
        }
        return instance;
    }

    public void init(GameField gameField) {
        Cell[][] cells = cellGenerator.generate(gameField);
        gameField.setCells(cells);
    }
}
