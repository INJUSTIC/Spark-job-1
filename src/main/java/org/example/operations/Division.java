package org.example.operations;

public class Division implements Operation {
    @Override
    public double calculate(double value1, double value2) {
        if (value2 == 0) {
            throw new IllegalArgumentException("Nie można dzielić przez zero");
        }
        return value1 / value2;
    }
}