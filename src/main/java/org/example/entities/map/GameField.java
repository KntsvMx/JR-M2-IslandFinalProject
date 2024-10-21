package org.example.entities.map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.abstraction.annotations.Config;


@Config(fileName = "model/yamlFormat/gameField.yaml")
@Getter
@NoArgsConstructor
public class GameField {

    private int width;
    private int height;

    @Setter
    private Cell[][] cells;
}
