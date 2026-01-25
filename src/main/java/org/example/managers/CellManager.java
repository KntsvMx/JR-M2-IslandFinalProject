package org.example.managers;

import lombok.Getter;
import lombok.Setter;
import org.example.abstraction.interfaces.GameObject;
import org.example.entities.map.Cell;
import org.example.entities.map.GameField;
import org.example.entities.map.InteractableCell;
import org.example.generators.CellGenerator;
import org.example.generators.ResidentsGenerator;

@Getter
@Setter
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

    public Cell[] getAllCells() {
        return
    }

    public Cell getRandomCellFromClosest(InteractableCell cell) {
        return cell.getRandomCellFromClosest();
    }

    public void addGameObject(InteractableCell cell, GameObject gameObject) {
        if (cell != null && gameObject != null && cell.getResidents() != null) {
            cell.addGameObjectToResidents(gameObject.getClass(), gameObject);
        } else {
            throw new IllegalArgumentException("Cell or GameObject is null");
        }
    }

    public void removeGameObject(InteractableCell cell, GameObject resident) {
        cell.removeGameObjectFromResidents(resident);
    }


}
