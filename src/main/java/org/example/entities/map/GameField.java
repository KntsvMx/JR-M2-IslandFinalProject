package org.example.entities.map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.abstraction.annotations.Config;


@Setter
@Getter
@Config(fileName = "model/yamlFormat/Map/gameField.yaml")
@NoArgsConstructor
public class GameField {

    private int width;
    private int height;

    private volatile Cell[][] cells;
}
