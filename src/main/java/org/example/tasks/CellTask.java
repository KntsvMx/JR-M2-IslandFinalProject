package org.example.tasks;

import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.Cell;

import java.util.concurrent.locks.Lock;

public class CellTask {
    private final Cell cell;

    public CellTask(Cell cell) {
        this.cell = cell;
    }

//    @Override
//    public void run() {
//        Lock lock = cell.getLock();

//        lock.lock();
//        try {
//            cell.getResidents().values().forEach(organism -> {
//                if (organism instanceof Animal) {
//                    Animal animal = (Animal) organism;
//
//                }
//            });
//        }
//    }
}
