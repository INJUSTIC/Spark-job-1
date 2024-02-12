package org.example.dataGeneration;

import org.example.model.PersonData;
import org.example.operationPerformer.OperationPerformer;

import java.util.List;

public class DataProcessingService {
    public static String performOperations(List<PersonData> generatedData, String operations) {
        operations = operations.replaceAll("\\s", "");
        List<String> operationList = List.of(operations.split(","));
        return OperationPerformer.performOperations(generatedData, operationList);
    }

    public static String convertToCSV(List<PersonData> generatedData) {
        String headers = "_type, _id, Name, Type, Latitude, Longitude \n";

        // Tworzenie danych w formacie CSV
        StringBuilder csvData = new StringBuilder();
        for (PersonData data : generatedData) {
            csvData.append(String.format("%s; %d; %s; %s; %f; %f \n",
                    data.get_type(), data.get_id(), data.getName(), data.getType(),
                    data.getGeoPosition().getLatitude(), data.getGeoPosition().getLongitude()));
        }

        return headers + csvData;
    }

    public static String convertToCSVWithGivenColumns(List<PersonData> generatedData, List<String> columns) {
        StringBuilder csvData = new StringBuilder();

        // Nagłówki
        String headers = String.join(",", columns) + "\n";
        csvData.append(headers);

        for (PersonData personData : generatedData) {
            StringBuilder rowData = new StringBuilder();

            //szukamy pola które mają taką samą nazwę jak kolumne
            for (String column : columns) {
                Object colValue = getColValue(personData, column);
                rowData.append(colValue).append(";");
            }

            // Usuwanie ostatniego przecinku w wierszu
            rowData.deleteCharAt(rowData.length() - 1);
            csvData.append(rowData).append("\n");
        }

        return csvData.toString();
    }

    private static Object getColValue(PersonData personData, String column) {
        return switch(column) {
            case "_id" -> personData.get_id();
            case "_type" -> personData.get_type();
            case "key" -> personData.getKey();
            case "name" -> personData.getName();
            case "fullName" -> personData.getFullName();
            case "location_id" -> personData.getLocation_id();
            case "iata_airport_code" -> personData.getIata_airport_code();
            case "type" -> personData.getType();
            case "coreCountry" -> personData.getCountryData();
            case "distance" -> personData.getDistanceInKm();
            case "geo_position" -> personData.getGeoPosition();
            case "latitude" -> personData.getGeoPosition().getLatitude();
            case "longitude" -> personData.getGeoPosition().getLongitude();
            case "country" -> personData.getCountryData().getCountry();
            case "inEurope" -> personData.getCountryData().isInEurope();
            case "countryCode" -> personData.getCountryData().getCountryCode();
            //w przypadku niepoprawnej kolumny
            default -> throw new IllegalArgumentException("Incorrect column: " + column);
        };
    }
}
