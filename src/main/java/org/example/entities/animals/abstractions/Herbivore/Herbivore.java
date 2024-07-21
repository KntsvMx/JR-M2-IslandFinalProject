package org.example.entities.animals.abstractions.Herbivore;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.entities.animals.abstractions.Animal;

@SuperBuilder
@NoArgsConstructor
public abstract class Herbivore extends Animal {
    @Override
    public void play() {
        super.play();

    }
}
