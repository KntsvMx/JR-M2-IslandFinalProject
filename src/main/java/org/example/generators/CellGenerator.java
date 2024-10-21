package org.example.generators;

import org.example.entities.map.Cell;
import org.example.entities.map.GameField;

public class CellGenerator {

    public CellGenerator() {

    }

//    TODO: Simplify this generate method
    public Cell[][] generateCells(GameField gameField) {
        int width = gameField.getWidth();
        int height = gameField.getHeight();

        Cell[][] cells = new Cell[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                cells[i][j] = Cell.builder().build();
            }
        }

        initializeNeighbors(cells, width, height);

        return cells;
    }
//    TODO: Refactor these methods
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
