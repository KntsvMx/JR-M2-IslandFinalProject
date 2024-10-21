package org.example.managers;

import org.example.entities.map.Cell;
import org.example.entities.map.GameField;
import org.example.generators.CellGenerator;
import org.example.generators.ResidentsGenerator;

import java.util.Random;

public class CellManager {
    private static CellManager instance;
    private final CellGenerator cellGenerator;
    private final ResidentsGenerator residentsGenerator;
    private Cell[][] cells;

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
        cells = cellGenerator.generateCells(gameField);
        residentsGenerator.generateNewResidence(cells);
        gameField.setCells(cells);
    }

    public Cell getRandomCell() {
        Random random = new Random();
        return cells[random.nextInt(cells.length)][random.nextInt(cells.length)];
    }

    public void addGameObject() {
//        TODO: Realize add GameObject to the cell
    }

    public void removeGameObject() {
//        TODO: Realize remove GameObject from the cell
    }


}
