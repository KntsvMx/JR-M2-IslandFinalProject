package org.example.entities.limits;

import lombok.Data;
@Data
public class Limits {
    private double maxWeight;
    private int maxAmount;
    private int maxSpeed;
    private int maxFood;
    private int maxAge;
    private double minWeight;
    private int minHealth;
}
