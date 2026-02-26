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
import org.example.entities.map.InteractableCell;

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
                .cell(getCell())
                .limits(getLimits())
                .target(getTarget())
                .icon(getIcon())
                .isAlive(true)
                .weight(200)
                .health(100)
                .age(2)
                .build();
    }

    @Override
    public void move(InteractableCell cell) {

    }
}
