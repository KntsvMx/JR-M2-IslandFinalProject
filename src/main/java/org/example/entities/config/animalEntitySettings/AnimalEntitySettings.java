package org.example.entities.config.animalEntitySettings;

import lombok.Getter;
import org.example.entities.config.EntitySettings;
import org.example.entities.limits.Limits;
import org.example.entities.map.InteractableCell;
import org.example.entities.target.Target;

import java.util.concurrent.atomic.AtomicLong;

@Getter
public class AnimalEntitySettings extends EntitySettings {

    // TODO: understand why we need this field to be final.
    private static final AtomicLong serialUID = new AtomicLong(1);
    private final long UID = serialUID.getAndIncrement();
    private final Limits limits;
    private final Target target;

    protected AnimalEntitySettings(boolean isAlive, double weight, int health, int age, InteractableCell cell, Limits limits, Target target) {
        super(isAlive, weight, health, age, cell);
        this.limits = limits;
        this.target = target;
    }
}
