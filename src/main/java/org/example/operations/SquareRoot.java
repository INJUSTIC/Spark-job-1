package org.example.operations;

public class SquareRoot implements Operation {
    @Override
    public double calculate(double value1, double value2) {
        return Math.sqrt(value1);
    }
}