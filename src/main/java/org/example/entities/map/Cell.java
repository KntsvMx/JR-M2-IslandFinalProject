package org.example.entities.map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantLock;

@Builder
@Getter
public class Cell implements InteractableCell {
    private static long serialUID = 1L;
    @Getter
    private final ReentrantLock lock = new ReentrantLock();

    @Builder.Default
    private final long UID = serialUID++;

    @Setter
    private HashMap<Class<? extends GameObject>, List<GameObject>> residents;

    private final List<Cell> nextCells = new CopyOnWriteArrayList<>();

    public void setNext(Cell cell) {
        nextCells.add(cell);
    }

    @Override
    public Cell getNext(Integer next) {
        return nextCells.get(next);
    }

    public Cell getRandomCellFromClosest() {
        Random random = new Random();
        int randomIndex = random.nextInt(nextCells.size());
        return nextCells.get(randomIndex);
    }

    public void removeGameObjectFromResidents(GameObject object) {
        List<GameObject> speciesList = residents.get(object.getClass());
        if (speciesList != null) {
            speciesList.remove(object);
        }
    }

    public void addGameObjectToResidents(Class<? extends GameObject> gameObjectClass, GameObject object) {
        residents.computeIfAbsent(gameObjectClass, k -> new ArrayList<>()).add(object);
        object.setCell(this);
    };

    public boolean hasAliveAnimals() {
        for(List<GameObject> list : residents.values()) {
            for(GameObject obj : list) {
                if (obj instanceof Animal && ((Animal) obj).isAlive()) {
                    return true;
                }
            }
        }
        return false;
    }
}

