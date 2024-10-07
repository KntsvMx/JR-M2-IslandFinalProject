package org.example.abstraction.interfaces;

import org.example.entities.interfaces.Reproducible;
import org.example.entities.map.Cell;

public interface GameObject extends Reproducible {
    void play();
    void setCell(Cell cell);
}
