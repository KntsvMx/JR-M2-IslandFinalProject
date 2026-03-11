package org.example.generators;

import org.example.entities.map.Cell;
import org.example.entities.map.GameField;

public class CellGenerator {
    private static final int[] ROW_OFF_SETS = {-1, 1, 0, 0};
    private static final int[] COL_OFF_SETS = {0, 0, -1, 1};

    public Cell[][] generateCells(GameField gameField) {
        int width = gameField.getWidth();
        int height = gameField.getHeight();
        Cell[][] cells = new Cell[height][width];

        generateNewCellsForMap(width, height, cells);
        initializeNeighbors(cells, width, height);

        return cells;
    }

    private static void generateNewCellsForMap(int width, int height, Cell[][] cells) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                cells[row][col] = Cell.builder().build();
            }
        }
    }

    private void initializeNeighbors(Cell[][] cells, int width, int height) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                assignNeighboursCells(cells, width, height, row, col);
            }
        }
    }

    private void assignNeighboursCells(Cell[][] cells, int width, int height, int i, int j) {
        Cell current = cells[i][j];
        for (int k = 0; k < ROW_OFF_SETS.length; k++) {
            int neighborRow = i + ROW_OFF_SETS[k];
            int neighborCol = j + COL_OFF_SETS[k];

            if (isValidCell(neighborRow, neighborCol, width, height)) {
                current.addNeighbor(cells[neighborRow][neighborCol]);
            }
        }
    }

    private boolean isValidCell(int row, int col, int width, int height) {
        boolean isRowValid = row >= 0 && row < height;
        boolean isColValid = col >= 0 && col < width;

        return isRowValid && isColValid;
    }
}
