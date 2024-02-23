package org.example.services;

import org.example.model.PersonData;
import org.example.operations.Operation;
import java.util.*;

public class OperationPerformingService {

    private final Map<String, Operation> operations;
    public OperationPerformingService(Map<String, Operation> operations) {
        this.operations = operations;
    }

    //podstawowa metoda, która przyjmuje listę danych oraz operacji i zwraca wynik w csv formacie
    public String performOperations(List<PersonData> personDataList, String[] operations) {
        StringBuilder resultOutput = new StringBuilder();
        Set<String> operandValues = extractOperands(operations);

        // Dołączanie nazw operandów oraz operacji do wyniku, czyli tworzenie nagłówku
        for (String operand : operandValues) {
            if (!isNumericLiteral(operand)) {
                resultOutput.append(operand).append(";");
            }
        }
        resultOutput.append(String.join(";", operations)).append("\n");

        String processedData = processDataAndCalculateResults(personDataList, operandValues, operations);
        resultOutput.append(processedData);

        return resultOutput.toString();
    }

    //Metoda która zwraca unikalne operandy z listy operacji
    private Set<String> extractOperands(String[] operations) {
        LinkedHashSet<String> operands = new LinkedHashSet<>();

        for (String operation : operations) {
            if (operation.startsWith("sqrt")) {
                String operand = operation.substring(operation.indexOf('(') + 1, operation.indexOf(')'));
                operands.add(operand);
            } else {
                String[] parts = operation.split("\\W");
                operands.add(parts[0]);
                if (parts.length > 1) {
                    operands.add(parts[1]);
                }
            }
        }
        return operands;
    }

    //Metoda która oblicza wyniki dla poszczególnego wierszu i zwraca wiersze w formacie csv
    private String processDataAndCalculateResults(List<PersonData> personDataList, Set<String> operands, String[] operations) {
        StringBuilder result = new StringBuilder();

        for (PersonData personData : personDataList) {
            StringBuilder rowData = processRowData(personData, operands, operations);
            result.append(rowData).append("\n");
        }

        return result.toString();
    }

    //Metoda która przetwarza dane w jednym wierszu, oblicza wyniki i zwraca wiersz w formacie csv
    private StringBuilder processRowData(PersonData personData, Set<String> operands, String[] operations) {
        StringBuilder rowData = new StringBuilder();
        Map<String, Double> operandValues = new HashMap<>();
        for (String operand : operands) {
            double value = getValue(operand, personData);
            operandValues.put(operand, value);
            if (!isNumericLiteral(operand)) {
                rowData.append(value).append(";");
            }
        }
        for (String operation : operations) {
            double resultValue = calculateOperationResult(operation, operandValues);
            rowData.append(resultValue).append(";");
        }
        rowData.deleteCharAt(rowData.length() - 1);
        return rowData;
    }

    private boolean isNumericLiteral(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private double calculateOperationResult(String operation, Map<String, Double> operandValues) {
        if (!isCorrectOperationSyntax(operation)) {
            throw new IllegalArgumentException("Niepoprawna składnia operacji: " + operation);
        }
        //Sprawdzamy czy operacja jest pierwiastkiem kwadratowym
        if (operation.startsWith("sqrt")) {
            String operand = operation.substring(operation.indexOf('(') + 1, operation.indexOf(')'));
            double value = operandValues.get(operand);
            return performOperation(value, 0, "sqrt");
        }
        //Jeśli nie to wykonujemy operację z 2 operandami
        else {
            String[] operandsAndOperator = extractOperandsAndOperator(operation);
            double value1 = operandValues.get(operandsAndOperator[0]);
            double value2 = operandValues.get(operandsAndOperator[1]);
            String operator = operandsAndOperator[2];
            return performOperation(value1, value2, operator);
        }
    }

    private boolean isCorrectOperationSyntax(String operation) {
        return (operation.matches("^[a-zA-Z_0-9]+\\W[a-zA-Z_0-9]+$") || operation.startsWith("sqrt"));
    }

    //Metoda która zwraca operandy oraz operator z operacji
    private String[] extractOperandsAndOperator(String operation) {
        String[] parts = operation.split("\\W");
        String operator = operation.replaceAll("[a-zA-Z_0-9]+", "").trim();
        return new String[]{parts[0], parts[1], operator};
    }

    private double performOperation(double value1, double value2, String operator) {
        Operation operation = operations.get(operator);
        if (operation == null) {
            throw new IllegalArgumentException("Niepoprawny operator: " + operator);
        }
        return operation.calculate(value1, value2);
    }
    //otrzymanie wartości na podstawie nazwy pola
    private double getValue(String field, PersonData personData){
        return switch (field) {
            case "_id" -> personData.get_id();
            case "latitude" -> personData.getGeoPosition().getLatitude();
            case "longitude" -> personData.getGeoPosition().getLongitude();
            case "distance" -> personData.getDistanceInKm();
            case "location_id" -> personData.getLocation_id();
            default -> {
                //Próba sparsować do liczby
                try {
                    yield Double.parseDouble(field);
                }
                catch (NumberFormatException ex) {
                    throw new IllegalArgumentException("Niepoprawne pole: " + field);
                }
            }
        };
    }
}
