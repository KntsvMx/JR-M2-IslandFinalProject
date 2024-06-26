package org.example.entities.animals.abstractions.Herbivore;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.interfaces.Eatable;

@SuperBuilder
@NoArgsConstructor
public abstract class Herbivore extends Animal implements Eatable {

    @Override
    public void eat() {

    }

    @Override
    public void findFood() {

    }
}
