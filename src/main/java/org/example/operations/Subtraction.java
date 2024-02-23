package org.example.operations;

public class Subtraction implements Operation{
    @Override
    public double calculate(double value1, double value2) {
        return value1 - value2;
    }
}
