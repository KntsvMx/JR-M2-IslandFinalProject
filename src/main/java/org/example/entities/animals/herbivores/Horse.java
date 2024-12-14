package org.example.entities.animals.herbivores;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.example.abstraction.annotations.Config;
import org.example.abstraction.annotations.GameObjectEntity;
import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Herbivore.Herbivore;

@GameObjectEntity
@Config(fileName = "model/yamlFormat/Herbivore/horse.yaml")
@NoArgsConstructor
@SuperBuilder
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)

public class Horse extends Herbivore {

//    TODO: update reproduce builder with right structure
    @Override
    public GameObject reproduce() {
        return Horse.builder()
                .limits(getLimits())
                .target(getTarget())
                .health(100)
                .age(1)
                .isAlive(true)
                .build();
    }
}
