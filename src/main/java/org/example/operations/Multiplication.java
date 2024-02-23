package org.example.operations;

public class Multiplication implements Operation {
    @Override
    public double calculate(double value1, double value2) {
        return value1 * value2;
    }
}
