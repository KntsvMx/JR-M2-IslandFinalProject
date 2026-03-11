package org.example.entities.map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.abstraction.annotations.Config;
import org.example.managers.CellManager;


@Setter
@Getter
@Config(fileName = "model/yamlFormat/Map/gameField.yaml")
@NoArgsConstructor
public class GameField {

    private int width;
    private int height;

    private volatile Cell[][] cells;

//    TODO: use Statistic logic to determine if ecosystem is dead (O(1) instead of O(N^3))
    public boolean isEcosystemDead() {
        for(Cell[] row : cells){
            for(Cell cell : row) {
                if(CellManager.getInstance().hasAliveAnimals(cell)) {
                    return false;
                }
            }
        }
        return true;
    }
}
