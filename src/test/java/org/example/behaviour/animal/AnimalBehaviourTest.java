package org.example.behaviour.animal;

import org.example.behaviour.generalBehaviorStatements.EatBehavior;
import org.example.behaviour.generalBehaviorStatements.MoveBehavior;
import org.example.behaviour.generalBehaviorStatements.ReproduceBehavior;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.Cell;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnimalBehaviourTest {

    @InjectMocks
    private AnimalBehaviour animalBehaviour;
    @Mock
    private EatBehavior eatBehavior;
    @Mock
    private ReproduceBehavior reproduceBehavior;
    @Mock
    private MoveBehavior moveBehavior;
    @Mock
    private Animal animal;
    @Mock
    private Cell cell;
    @Mock
    private Cell targetCell;

    @Test
    void act_ShouldCallEatAndReproduce_WhenAnimalIsAlive() {
        when(animal.isAlive()).thenReturn(true);
        when(animal.getCell()).thenReturn(cell);

        animalBehaviour.act(animal);
        verify(eatBehavior).eat(animal, cell);
    }

    @Test
    void act_ShouldNotCallEatAndReproduce_WhenAnimalIsNotAlive() {
        animalBehaviour.act(animal);
        verifyNoInteractions(eatBehavior, reproduceBehavior);
    }

    @Test
    void act_ShouldCallMove_WhenTargetCellIsPresent() {
        when(animal.isAlive()).thenReturn(true);
        when(animal.getCell()).thenReturn(cell);
        when(cell.getRandomCellFromClosest()).thenReturn(targetCell);

        animalBehaviour.act(animal);

        verify(moveBehavior).move(animal, cell, targetCell);
    }

    @Test
    void act_ShouldNotCallMove_WhenTargetCellIsNull() {
        when(animal.isAlive()).thenReturn(true);
        when(animal.getCell()).thenReturn(cell);
        when(cell.getRandomCellFromClosest()).thenReturn(null);

        animalBehaviour.act(animal);

        verifyNoInteractions(moveBehavior);
    }

    @Test
    void act_ShouldNotCallMove_WhenTargetCellIsSameAsCurrentCell() {
        when(animal.isAlive()).thenReturn(true);
        when(animal.getCell()).thenReturn(cell);
        when(cell.getRandomCellFromClosest()).thenReturn(cell);

        animalBehaviour.act(animal);

        verifyNoInteractions(moveBehavior);
    }

    @Test
    void act_ShouldCheckDeath_WhenWeightIsZeroOrLess() {
        when(animal.isAlive()).thenReturn(true);
        when(animal.getCell()).thenReturn(cell);
        when(cell.getRandomCellFromClosest()).thenReturn(targetCell);
        when(animal.getWeight()).thenReturn(0.0);

        animalBehaviour.act(animal);

        verify(animal).reduceWeightPerTick();
        verify(animal).checkDeath();
    }

    @Test
    void act_ShouldNotCheckDeath_WhenWeightIsAboveZero() {
        when(animal.isAlive()).thenReturn(true);
        when(animal.getCell()).thenReturn(cell);
        when(cell.getRandomCellFromClosest()).thenReturn(targetCell);
        when(animal.getWeight()).thenReturn(10.0);

        animalBehaviour.act(animal);

        verify(animal).reduceWeightPerTick();
        verify(animal, never()).checkDeath();
    }
}