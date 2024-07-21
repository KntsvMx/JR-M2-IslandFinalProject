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
        int width = gameField.getWidth();
        int height = gameField.getHeight();
        Cell[][] cells = new Cell[width][height];

        fillCells(width, height, cells);
        initializeNeighbors(cells, width, height);

        return cells;
    }

    private void fillCells(int width, int height, Cell[][] cells) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                generateResidentsAndPutThemIntoCell(j, cells[i]);
            }
        }
    }

    private void generateResidentsAndPutThemIntoCell(int j, Cell[] cells) {
        Map<Class<? extends GameObject>, List<GameObject>> residents = residenceGenerator.generateNewResidence();
        cells[j] = Cell.builder().residents(residents).build();
    }

    private void initializeNeighbors(Cell[][] cells, int width, int height) {
        int[][] directions = {
                {-1, 0},
                {1, 0},
                {0, -1},
                {0, 1}
        };

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell current = cells[i][j];
                addNeighbors(cells, width, height, directions, i, j, current);
            }
        }
    }

    private void addNeighbors(Cell[][] cells, int width, int height, int[][] directions, int i, int j, Cell current) {
        for (int[] direction: directions) {
            int neighborRow = i + direction[0];
            int neighborCol = j + direction[1];

            if (isValidCell(neighborRow, neighborCol, width, height)) {
                current.setNext(cells[neighborRow][neighborCol]);
            }
        }
    }

    private boolean isValidCell(int row, int col, int width, int height) {
        return row >= 0 && row < width && col >= 0 && col < height;
    }
}
