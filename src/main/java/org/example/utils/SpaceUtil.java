package org.example.utils;

import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.InteractableCell;
import org.example.entities.plants.Plant;

import java.util.List;

public class SpaceUtil {
    private SpaceUtil() {
        // Private constructor to prevent instantiation
    }

    public static boolean availableSpaceForSpecie(InteractableCell cell, GameObject gameObject) {
        int amountOfSpecie = 0;
        int maxAmount = 0;

        if(gameObject instanceof Animal) {
            amountOfSpecie = cell.getResidents().getOrDefault(gameObject.getClass(), List.of()).size();
            maxAmount = ((Animal) gameObject).getLimits().getMaxAmount();
        } else {
            amountOfSpecie = cell.getResidents().getOrDefault(gameObject.getClass(), List.of()).size();
            maxAmount = ((Plant) gameObject).getMaxAmount();
        }

        return amountOfSpecie < maxAmount;
    }
} 