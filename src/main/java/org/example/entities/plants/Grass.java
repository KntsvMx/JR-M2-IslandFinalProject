package org.example.entities.plants;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.example.abstraction.annotations.Config;
import org.example.abstraction.annotations.GameObjectEntity;
import org.example.abstraction.interfaces.GameObject;

@GameObjectEntity
@Config(fileName = "model/yamlFormat/grass.yaml")
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Grass extends Plant {

    @Override
    public void play() {
        super.play();
    }

}


