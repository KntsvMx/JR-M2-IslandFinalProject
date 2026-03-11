package org.example.entities.map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

@Builder
@Getter
public class Cell implements InteractableCell {
    private static AtomicLong serialUID = new AtomicLong(1);
    @Getter
    private final ReentrantLock lock = new ReentrantLock();

    @Builder.Default
    private final long UID = serialUID.getAndIncrement();

    @Setter
    private HashMap<Class<? extends GameObject>, List<GameObject>> residents;

    private final List<Cell> nextCells = new CopyOnWriteArrayList<>();

    public void addNeighbor(Cell cell) {
        nextCells.add(cell);
    }

    @Override
    public Cell getNext(Integer next) {
        return nextCells.get(next);
    }

    public Cell getRandomCellFromClosest() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
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
    }


}

