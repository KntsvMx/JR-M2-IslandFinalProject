package org.example.managers;

import org.example.entities.map.Cell;
import org.example.entities.map.GameField;
import org.example.generators.CellGenerator;
import org.example.generators.ResidentsGenerator;

public class CellManager {
    private static CellManager instance;
    private final CellGenerator cellGenerator;
    private final ResidentsGenerator residentsGenerator;

    private CellManager() {
        cellGenerator = new CellGenerator();
        residentsGenerator = new ResidentsGenerator();
    }

    public static CellManager getInstance() {
        if (instance == null) {
            instance = new CellManager();
        }
        return instance;
    }

    public void init(GameField gameField) {
        Cell[][] cells = cellGenerator.generateCells(gameField);
        residentsGenerator.generateNewResidence(cells);
        gameField.setCells(cells);
    }

    public Cell getRandomCell() {
        return null;
    }

    public void addGameObject() {

    }

    public void removeGameObject() {

    }


}
