package org.example.entities.limits;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LimitsTest {

    private Limits limits;

    @BeforeEach
    void setUp() {
        limits = new Limits();
        limits.setMaxWeight(100);
        limits.setMaxAmount(100);
        limits.setMaxSpeed(100);
        limits.setMaxFood(100);
        limits.setMaxAge(100);
        limits.setMinWeight(100);
        limits.setMinHealth(100);
    }

    @Test
    void getMaxWeight() {
        assertEquals(100, limits.getMaxWeight());
    }

    @Test
    void getMaxAmount() {
        assertEquals(100, limits.getMaxAmount());
    }

    @Test
    void getMaxSpeed() {
        assertEquals(100, limits.getMaxSpeed());
    }

    @Test
    void getMaxFood() {
        assertEquals(100, limits.getMaxFood());
    }

    @Test
    void getMaxAge() {
        assertEquals(100, limits.getMaxAge());
    }

    @Test
    void getMinWeight() {
        assertEquals(100, limits.getMinWeight());
    }

    @Test
    void getMinHealth() {
        assertEquals(100, limits.getMinHealth());
    }

    @Test
    void setMaxWeight() {
        limits.setMaxWeight(200);
        assertEquals(200, limits.getMaxWeight());
    }

    @Test
    void setMaxAmount() {
        limits.setMaxAmount(200);
        assertEquals(200, limits.getMaxAmount());
    }

    @Test
    void setMaxSpeed() {
        limits.setMaxSpeed(200);
        assertEquals(200, limits.getMaxSpeed());
    }

    @Test
    void setMaxFood() {
        limits.setMaxFood(200);
        assertEquals(200, limits.getMaxFood());
    }

    @Test
    void setMaxAge() {
        limits.setMaxAge(1);
        assertEquals(1, limits.getMaxAge());
    }

    @Test
    void setMinWeight() {
        limits.setMinWeight(1);
        assertEquals(1, limits.getMinWeight());
    }

    @Test
    void setMinHealth() {
        limits.setMinHealth(1);
        assertEquals(1, limits.getMinHealth());
    }
}