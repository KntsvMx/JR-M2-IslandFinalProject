package org.example.utils;

import org.example.entities.map.InteractableCell;

import java.util.List;

public class SpaceUtil {
    private SpaceUtil() {
        // Private constructor to prevent instantiation
    }

    public static boolean availableSpaceForSpecie(InteractableCell cell, Integer maxAmount) {
        int amountOfSpecie = cell.getResidents().getOrDefault(cell.getClass(), List.of()).size();
        return amountOfSpecie < maxAmount;
    }
} 