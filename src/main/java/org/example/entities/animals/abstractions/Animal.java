package org.example.entities.animals.abstractions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.example.entities.interfaces.Movable;
import org.example.entities.interfaces.Organism;
import org.example.entities.limits.Limits;
import org.example.entities.map.Cell;

@NoArgsConstructor
@SuperBuilder
@Getter
@EqualsAndHashCode
@ToString

public abstract class Animal implements Organism, Movable{
    private static long serialUID = 1L;

    @Builder.Default
    private final long UID = serialUID++;

    @JsonIgnore
    private Cell cell;

    private Limits limits;

    private String icon;
    private boolean isAlive = true;
    private int weight;
    private int health;
    private int age;

    public void play() {
        System.out.println("Animal play");
        // TODO: add implementation
    }


    public void move() {

    }
}
