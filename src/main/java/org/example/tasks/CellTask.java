package org.example.tasks;

import org.example.abstraction.interfaces.GameObject;
import org.example.behaviour.animal.AnimalBehaviour;
import org.example.behaviour.plant.PlantBehaviour;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

public class CellTask implements Runnable{
    private final Cell cell;
    private final AnimalBehaviour animalBehaviour;
    private final PlantBehaviour plantBehaviour;


    public CellTask(Cell cell, AnimalBehaviour animalBehaviour, PlantBehaviour plantBehaviour) {
        this.cell = cell;
        this.animalBehaviour = animalBehaviour;
        this.plantBehaviour = plantBehaviour;
    }

    @Override
    public void run() {
        cell.getLock().lock();
        try {
            processAnimals();

            processPlants();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cell.getLock().unlock();
        }
    }

    private void processPlants() {
        plantBehaviour.grow(cell);
    }

    private void processAnimals() {
        List<Animal> animals = getAllAnimals(cell);
        for(Animal animal : animals) {
            if (!animal.isAlive()) {
                continue;
            }
            animalBehaviour.act(animal);
        }
    }

    private List<Animal> getAllAnimals(Cell cell) {
        List<Animal> allAnimals = new ArrayList<>();
        for (Map.Entry<Class<? extends GameObject>, List<GameObject>> entry : cell.getResidents().entrySet()) {
            if (Animal.class.isAssignableFrom(entry.getKey())) {
                for (GameObject obj : entry.getValue()) {
                    if(obj instanceof Animal) {
                        allAnimals.add((Animal) obj);
                    }
                }
            }
        }
        return allAnimals;
    }
}
