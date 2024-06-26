package org.example.entities.map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.abstraction.annotations.Config;

@Config(fileName = "")
@Getter
@NoArgsConstructor
public class GameField {

    private int width;
    private int height;

    private Cell[][] cells;
}
