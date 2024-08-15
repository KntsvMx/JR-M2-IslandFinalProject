package org.example.entities.animals.abstractions.Predator;

import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.interfaces.Eatable;

@SuperBuilder
@NoArgsConstructor
public abstract class Predator extends Animal {

    @Override
    public void play() {
        super.play();
    }
}
