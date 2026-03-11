package org.example.managers;

import lombok.Getter;
import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.Cell;
import org.example.entities.map.GameField;
import org.example.entities.map.InteractableCell;
import org.example.generators.CellGenerator;
import org.example.generators.ResidentsGenerator;

import java.util.List;
import java.util.Map;

@Getter
public class CellManager {
    @Getter
    private static final CellManager instance = new CellManager();
    private final CellGenerator cellGenerator;
    private final ResidentsGenerator residentsGenerator;
    private Cell[][] cells;

    private CellManager() {
        cellGenerator = new CellGenerator();
        residentsGenerator = new ResidentsGenerator();
    }

    public void init(GameField gameField) {
        cells = cellGenerator.generateCells(gameField);
        residentsGenerator.generateNewResidence(cells);
        gameField.setCells(cells);
    }

    public Cell getRandomCellFromClosest(InteractableCell cell) {
        if (cell == null) {
            throw new IllegalArgumentException("Cell is null");
        }
        return cell.getRandomCellFromClosest();
    }

    public void addGameObject(InteractableCell cell, GameObject gameObject) {
        if (cell == null) {
            throw new IllegalArgumentException("Cell is null");
        }

        cell.getLock().lock();

        try {
            if (gameObject != null && cell.getResidents() != null) {
                cell.addGameObjectToResidents(gameObject.getClass(), gameObject);
            } else {
                throw new IllegalArgumentException("Cell or GameObject is null");
            }
        } finally {
            cell.getLock().unlock();
        }
    }

    public void removeGameObject(InteractableCell cell, GameObject resident) {
        if (cell == null) {
            throw new IllegalArgumentException("Cell is null");
        }

        cell.getLock().lock();
        try {
            cell.removeGameObjectFromResidents(resident);
        } finally {
            cell.getLock().unlock();
        }
    }

    public InteractableCell getAnimalCell(Animal animal) {
        return animal.getCell();
    }

    public boolean containsGameObject(InteractableCell cell, GameObject gameObject) {
        if (cell == null || gameObject == null) return false;

        cell.getLock().lock();
        try {
            Map<Class<? extends GameObject>, List<GameObject>> residents = cell.getResidents();
            if (residents == null) return false;

            List<GameObject> speciesList = residents.get(gameObject.getClass());
            return speciesList != null && speciesList.contains(gameObject);
        } finally {
            cell.getLock().unlock();
        }
    }

    public boolean hasAliveAnimals(InteractableCell cell) {
        for (List<GameObject> list : cell.getResidents().values()) {
            for (GameObject obj : list) {
                if (obj instanceof Animal && ((Animal) obj).isAlive()) {
                    return true;
                }
            }
        }
        return false;
    }

}
