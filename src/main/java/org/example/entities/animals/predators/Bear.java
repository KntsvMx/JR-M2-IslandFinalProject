package org.example.entities.animals.predators;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.example.abstraction.annotations.Config;
import org.example.abstraction.annotations.GameObjectEntity;
import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Predator.Predator;
import org.example.entities.map.InteractableCell;

@GameObjectEntity
@Config(fileName = "model/yamlFormat/Predators/bear.yaml")
@NoArgsConstructor
@SuperBuilder
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Bear extends Predator {
    @Override
    public void move(InteractableCell cell) {

    }

    @Override
    public GameObject reproduce() {
        return Bear.builder()
                .cell(getCell())
                .limits(getLimits())
                .target(getTarget())
                .icon(getIcon())
                .isAlive(true)
                .weight(50)
                .health(100)
                .age(1)
                .build();
    }
}
