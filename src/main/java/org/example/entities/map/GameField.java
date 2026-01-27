package org.example.entities.map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.abstraction.annotations.Config;


@Config(fileName = "model/yamlFormat/Map/gameField.yaml")
@Getter
@NoArgsConstructor
public class GameField {

    private int width;
    private int height;

    @Setter
    private volatile Cell[][] cells;

    public boolean isEcosystemDead() {
        for(Cell[] row : cells){
            for(Cell cell : row) {
                if(cell.hasAliveAnimals()) {
                    return false;
                }
            }
        }
        return true;
    }
}
