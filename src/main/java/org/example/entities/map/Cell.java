package org.example.entities.map;

import lombok.Builder;
import lombok.Getter;
import org.example.abstraction.interfaces.GameObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
@Getter
public class Cell {
    private static long serialUID = 1l;

    @Builder.Default
    private final long UID = serialUID++;

    private final Map<Class<? extends GameObject>, Set<GameObject>> residents;

    private final List<Cell> nextCells = new ArrayList<>();

    public void setNext(Cell cell) {
        nextCells.add(cell);
    }
}
