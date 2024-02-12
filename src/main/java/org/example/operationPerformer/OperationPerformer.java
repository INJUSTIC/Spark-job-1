package org.example.operationPerformer;

import org.example.model.PersonData;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OperationPerformer {

    //podstawowa metoda, która przyjmuje listę danych oraz operacji i zwraca wynik w csv formacie
    public static String performOperations(List<PersonData> personDataList, List<String> operations) {
        StringBuilder result = new StringBuilder();
        Map<String, Double> operandValues = extractOperands(operations);

        // Dołączanie nazww operandów oraz operacji do wyniku, czyli tworzenie nagłówku
        result.append(String.join(";", operandValues.keySet()))
                .append(";")
                .append(String.join(";", operations))
                .append("\n");

        String processedData = processDataAndCalculateResults(personDataList, operandValues, operations);
        result.append(processedData);

        return result.toString();
    }

    //Metoda która wyodrębnia wartości argumentów
    private static Map<String, Double> extractOperands(List<String> operations) {
        Map<String, Double> operandValues = new LinkedHashMap<>();

        for (String operation : operations) {
            if (isMathExpression(operation)) {
                String[] parts = operation.split("[*/+\\-^]");
                operandValues.put(parts[0], null);
                operandValues.put(parts[1], null);
            } else if (isSquareRootExpression(operation)) {
                String fieldName = operation.substring(operation.indexOf('(') + 1, operation.indexOf(')'));
                operandValues.put(fieldName, null);
            } else {
                throw new IllegalArgumentException("Niepoprawna operacja matematyczna: " + operation);
            }
        }

        return operandValues;
    }

    //Metoda która zwraca czy wyrażenie matematyczne jest jakieś z następnych (a+b;a-b;a/b;a*b;a^b)
    private static boolean isMathExpression(String operation) {
        return operation.matches("\\b([a-zA-Z0-9_]+(\\.\\d+)?\\s*[+\\-*/^]\\s*[a-zA-Z0-9_]+(\\.\\d+)?)\\b");
    }

    //Metoda która zwraca czy wyrażenie matematyczne jest pierwiastkiem
    private static boolean isSquareRootExpression(String operation) {
        return operation.matches("sqrt\\(\\w+\\)");
    }

    //Metoda która przetwarza dane i oblicza wyniki
    private static String processDataAndCalculateResults(List<PersonData> personDataList, Map<String, Double> operandValues, List<String> operations) {
        StringBuilder result = new StringBuilder();

        for (PersonData personData : personDataList) {
            StringBuilder rowData = new StringBuilder();

            for (String operand : operandValues.keySet()) {
                double value = getValue(operand, personData);
                operandValues.put(operand, value);
                rowData.append(value).append(";");
            }

            for (String operation : operations) {
                double resultValue;
                //pobieramy operandy i wykonujemy operację
                if (isMathExpression(operation)) {
                    //otrzymujemy tablicę operandów
                    String[] parts = operation.split("[*/+\\-^]");
                    //pobieramy operator
                    String operator = operation.replaceAll("[a-zA-Z_0-9]+", "").trim();
                    double value1 = operandValues.get(parts[0]);
                    double value2 = operandValues.get(parts[1]);
                    resultValue = performOperation(value1, value2, operator);
                } else if (isSquareRootExpression(operation)) {
                    String fieldName = operation.substring(operation.indexOf('(') + 1, operation.indexOf(')'));
                    double value = operandValues.get(fieldName);
                    resultValue = performOperation(value, 0, "sqrt");
                } else {
                    resultValue = operandValues.get(operation);
                }
                rowData.append(resultValue).append(";");
            }

            rowData.deleteCharAt(rowData.length() - 1);
            result.append(rowData).append("\n");
        }

        return result.toString();
    }

    //otrzymanie wartości na podstawie nazwy pola
    private static double getValue (String field, PersonData personData){
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

    private static double performOperation(double value1, double value2, String operator) {
        return switch (operator) {
            case "+" -> value1 + value2;
            case "-" -> value1 - value2;
            case "*" -> value1 * value2;
            case "/" -> value1 / value2;
            case "^" -> Math.pow(value1, value2);
            case "sqrt" -> Math.sqrt(value1);
            default -> throw new IllegalArgumentException("Niepoprawny operator: " + operator);
        };
    }
}
