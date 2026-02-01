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
    private int cycleCount = 0;


    public CellTask(Cell cell, AnimalBehaviour animalBehaviour, PlantBehaviour plantBehaviour, int cycleCount) {
        this.cell = cell;
        this.animalBehaviour = animalBehaviour;
        this.plantBehaviour = plantBehaviour;
        this.cycleCount = cycleCount;
    }

    @Override
    public void run() {
        try {
            cell.getLock().lock();
            try {
                processAnimals();

                if(cycleCount % 4 == 0) {
                    processPlants();
                }
            } finally {
                cell.getLock().unlock();
            }
        } catch (Exception e) {
            System.err.println("Error processing cell: " + e.getMessage());
            e.printStackTrace();
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
