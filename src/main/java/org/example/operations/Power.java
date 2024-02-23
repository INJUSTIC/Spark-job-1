package org.example.operations;

public class Power implements Operation {
    @Override
    public double calculate(double value1, double value2) {
        return Math.pow(value1, value2);
    }
}