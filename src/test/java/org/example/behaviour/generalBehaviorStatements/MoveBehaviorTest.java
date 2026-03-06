package org.example.behaviour.generalBehaviorStatements;

import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.Cell;
import org.example.managers.CellManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.*;

class MoveBehaviorTest {

    @InjectMocks
    private MoveBehavior moveBehavior;
    @Mock
    private CellManager cellManager;
    @Mock
    private Animal animal;
    @Mock
    private Cell cell;
    @Mock
    private ReentrantLock lock;


    @BeforeEach
    void setUp() {

    }

    @Test
    void move_ShouldThrowsNullPointerException_WhenAnimalIsNull() {

    }

    @Test
    void move_ShouldThrowsNullPointerException_WhenFromCellIsNull() {

    }

    @Test
    void move_ShouldThrowsNullPointerException_WhenToCellIsNull() {

    }

    @Test
    void move_ShouldTrowsException_WhenTargetedLockIsNull() {

    }

    @Test
    void move_ShouldMoveAnimal_WhenHasSpaceAndIsHereConditionsAreTrue() {

    }



}