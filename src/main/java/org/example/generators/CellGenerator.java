package org.example.generators;

import org.example.abstraction.interfaces.GameObject;
import org.example.entities.map.Cell;
import org.example.entities.map.GameField;

import java.util.List;
import java.util.Map;

public class CellGenerator {
    private static CellGenerator instance;
    private final ResidenceGenerator residenceGenerator;
    private CellGenerator() {
        residenceGenerator = ResidenceGenerator.getInstance();

    }

    public static CellGenerator getInstance() {
        if (instance == null) {
            instance = new CellGenerator();
        }
        return instance;
    }

    public Cell[][] generate(GameField gameField) {
        Map<Class<? extends GameObject>, List<GameObject>> residents;
        int width = gameField.getWidth();
        int height = gameField.getHeight();

        Cell[][] cells = new Cell[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                residents = residenceGenerator.generateNewResidence();
                cells[i][j] = Cell.builder().residents(residents).build();
            }
        }

        initializeNeighbors(cells, width, height);

        return cells;
    }

    private void initializeNeighbors(Cell[][] cells, int width, int height) {
        int[] rowOffSets = {-1, 1, 0, 0};
        int[] colOffSets = {0, 0, -1, 1};

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell current = cells[i][j];

                for (int k = 0; k < 4; k++) {
                    int neighborRow = i + rowOffSets[k];
                    int neighborCol = j + colOffSets[k];

                    if (isValidCell(neighborRow, neighborCol, width, height)) {
                        current.setNext(cells[neighborRow][neighborCol]);
                    }
                }

            }
        }
    }

    private boolean isValidCell(int row, int col, int width, int height) {
        return row >= 0 && row < width && col >= 0 && col < height;
    }
}
