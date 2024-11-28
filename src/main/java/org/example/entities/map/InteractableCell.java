package org.example.entities.map;

import org.example.abstraction.interfaces.GameObject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public interface InteractableCell {
    Map<Class<? extends GameObject>, List<GameObject>> getResidents();

    void setNext(Cell cell);

    Cell getNext(Integer next);

    Cell getRandomCellFromClosest();

    void removeGameObjectFromResidents(GameObject gameObject);

    void addGameObjectToResidents(Class<? extends GameObject> gameObjectClass, GameObject object);

    ReentrantLock getLock();


}
