package org.example.utils;

import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.limits.Limits;
import org.example.entities.map.InteractableCell;
import org.example.entities.plants.Plant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpaceUtilTest {

    @Mock
    private InteractableCell cell;
    @Mock
    private Animal animal;
    @Mock
    private Plant plant;

    private Map<Class<? extends GameObject>, List<GameObject>> fakeAnimalResidents;
    private Map<Class<? extends GameObject>, List<GameObject>> fakePlantResidents;

    private Limits limits;
    private final int maxAmountOfTen = 10;

    @BeforeEach
    void setUp() {
        limits = new Limits();
        limits.setMaxAmount(10);

        fakeAnimalResidents = Map.of(animal.getClass(), List.of(animal, animal, animal, animal, animal));
        fakePlantResidents = Map.of(plant.getClass(), List.of(plant, plant, plant, plant, plant));
    }

    @Test
    void availableSpaceForSpecieReturnTrue() {
        when(cell.getResidents()).thenReturn(fakeAnimalResidents);
        when((animal).getLimits()).thenReturn(limits);

        boolean result = SpaceUtil.availableSpaceForSpecie(cell, animal);

        assertTrue(result);
    }

    @Test
    void availableSpaceForSpecieReturnFalse() {
        when(cell.getResidents()).thenReturn(fakeAnimalResidents);
        when(animal.getLimits()).thenReturn(limits);

        limits.setMaxAmount(5);
        boolean result = SpaceUtil.availableSpaceForSpecie(cell, animal);

        assertFalse(result);
    }

    @Test
    void availableSpaceForSpecieCheckIfGameObjectIsAnimal() {
        when(cell.getResidents()).thenReturn(fakeAnimalResidents);
        when(animal.getLimits()).thenReturn(limits);

        boolean result = SpaceUtil.availableSpaceForSpecie(cell, animal);

        verify(animal).getLimits();
    }

    @Test
    void availableSpaceForSpecieCheckIfGameObjectIsPlant() {
        when(cell.getResidents()).thenReturn(fakePlantResidents);
        when(plant.getMaxAmount()).thenReturn(maxAmountOfTen);

        boolean result = SpaceUtil.availableSpaceForSpecie(cell, plant);

        verify(plant).getMaxAmount();
    }
}