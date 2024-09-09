package org.example.entities.animals.herbivores;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.example.abstraction.annotations.Config;
import org.example.abstraction.annotations.GameObjectEntity;
import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Herbivore.Herbivore;

@GameObjectEntity
@Config(fileName = "model/yamlFormat/horse.yaml")
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)

public class Horse extends Herbivore {

    @Override
    public GameObject reproduce() {
        return Horse.builder()
                .limits(getLimits())
                .health(100)
                .age(0)
                .isAlive(true)
                .build();
    }
}
