package org.example.entities.map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.abstraction.interfaces.GameObject;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Builder
@Getter
public class Cell implements InteractableCell{
    private static long serialUID = 1L;

    @Builder.Default
    private final long UID = serialUID++;

    @Setter
    private Map<Class<? extends GameObject>, List<GameObject>> residents;

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
//        TODO: Add check nullPointerException for List<GameObject> for example if structure hasn't created or absent
        getResidents().get(object.getClass()).remove(object);
    }

    public void addGameObjectToResidents(Class<? extends GameObject> gameObjectClass, GameObject object) {
//        TODO: Probably change computeIfPresent to computeIfAbsent as List could be absent
        residents.computeIfPresent(gameObjectClass, (key, list) -> {
            list.add(object);
            return list;
        });
    }
}
