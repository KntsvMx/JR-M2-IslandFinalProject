package org.example.entities.map;

import lombok.Builder;
import lombok.Getter;
import org.example.abstraction.interfaces.GameObject;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Builder
@Getter
public class Cell {
    private static long serialUID = 1L;

    @Builder.Default
    private final long UID = serialUID++;

    private Map<Class<? extends GameObject>, List<GameObject>> residents;

    private final List<Cell> nextCells = new CopyOnWriteArrayList<>();

    public void setNext(Cell cell) {
        nextCells.add(cell);
    }

    public void addNewResident(Class<? extends GameObject> gameObjectClass, GameObject object) {
        residents.computeIfPresent(gameObjectClass, (key, list) -> {
            list.add(object);
            return list;
        });
    }

    public Cell getRandomCell() {
        Random random = new Random();
        int randomIndex = random.nextInt(nextCells.size());
        return nextCells.get(randomIndex);
    }

    public void removeGameObjectFromCell(GameObject object) {
        getResidents().get(object.getClass()).remove(object);
    }
}
