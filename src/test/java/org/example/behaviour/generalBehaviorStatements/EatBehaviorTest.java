package org.example.behaviour.generalBehaviorStatements;

import org.example.abstraction.interfaces.GameObject;
import org.example.entities.animals.abstractions.Animal;
import org.example.entities.map.InteractableCell;
import org.example.managers.CellManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EatBehaviorTest {


    @InjectMocks
    private EatBehavior eatBehavior;
    @Mock
    private CellManager cellManager;
    @Mock
    private Animal animal;
    @Mock
    private InteractableCell cell;
    @Mock
    private GameObject victim;

    @BeforeEach
    void setUp() {
    }


}