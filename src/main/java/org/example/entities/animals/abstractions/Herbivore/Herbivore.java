package org.example.entities.animals.abstractions.Herbivore;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.abstraction.interfaces.GameObject;
import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.interfaces.Eatable;

import java.util.*;

@SuperBuilder
@NoArgsConstructor
public abstract class Herbivore extends Animal {
    @Override
    public void play() {
        super.play();
    }
}
