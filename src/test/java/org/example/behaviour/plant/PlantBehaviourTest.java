package org.example.behaviour.plant;

import org.example.abstraction.interfaces.GameObject;
import org.example.behaviour.generalBehaviorStatements.ReproduceBehavior;
import org.example.entities.map.Cell;
import org.example.entities.plants.Plant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlantBehaviourTest {

    @Mock
    private ReproduceBehavior reproduceBehavior;
    @InjectMocks
    private PlantBehaviour plantBehaviour;
    @Mock
    private Cell cell;
    @Mock
    private Plant plant;
    private HashMap<Class<? extends GameObject>, List<GameObject>> residents;

    @BeforeEach
    void setUp() {
        residents = new HashMap<>(Map.of(plant.getClass(), List.of(plant, plant, plant)));
        when(cell.getResidents()).thenReturn(residents);
    }

    @Test
    void grow_ShouldHandleEmptyResidents() {
        residents = new HashMap<>();
        when(cell.getResidents()).thenReturn(residents);

        plantBehaviour.grow(cell);

        verifyNoInteractions(reproduceBehavior);
    }

    @Test
    void grow_ShouldHandleNullOfResidents() {
        residents = null;
        when(cell.getResidents()).thenReturn(residents);

        plantBehaviour.grow(cell);

        verifyNoInteractions(reproduceBehavior);
        assertThrows(NullPointerException.class, () -> plantBehaviour.grow(cell));
    }

    @Test
    void grow_ShouldHandleNullOfCell() {
        cell = null;

        plantBehaviour.grow(cell);

        assertThrows(NullPointerException.class, () -> plantBehaviour.grow(cell));
    }

    @Test
    void grow_ShouldReproducePlants() {
        plantBehaviour.grow(cell);

        verify(reproduceBehavior).reproduce(plant, cell);
    }


    @Test
    void processPlants_ShouldBeCalledAndReturnedPlant() {
        plantBehaviour.grow(cell);

        verify(cell).getResidents();
    }

    @Test
    void processPlants_ShouldHandleNullAndThrowException() {
        when(cell.getResidents()).thenReturn(null);

        plantBehaviour.grow(cell);

        verifyNoInteractions(reproduceBehavior);
        assertThrows(NullPointerException.class, () -> plantBehaviour.grow(cell));
    }

}