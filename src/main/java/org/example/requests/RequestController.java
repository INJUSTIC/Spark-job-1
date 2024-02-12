package org.example.requests;

import org.example.dataGeneration.DataGenerationService;
import org.example.dataGeneration.DataProcessingService;
import org.example.dataGeneration.PerformanceMonitoringService;
import org.example.model.PersonData;

import java.util.List;

public class RequestController {
    public static List<PersonData> generateJson(int size) {
        if (size < 0) throw new IllegalArgumentException("size is negative");
        return DataGenerationService.generateRandomPersonDataList(size);
    }

    public static String performOperations(int size, String operations) {
        if (size < 0) throw new IllegalArgumentException("size is negative");
        List<PersonData> generatedData = generateJson(size);
        return DataProcessingService.performOperations(generatedData, operations);
    }

    public static String convertToCSV(int size) {
        if (size < 0) throw new IllegalArgumentException("size is negative");
        List<PersonData> generatedData = generateJson(size);
        return DataProcessingService.convertToCSV(generatedData);
    }

    public static String convertToCSVWithGivenColumns(List<String> columns, int size) {
        if (size < 0) throw new IllegalArgumentException("size is negative");
        List<PersonData> generatedData = generateJson(size);
        return DataProcessingService.convertToCSVWithGivenColumns(generatedData, columns);
    }

    public static String monitorPerformance() {
        return PerformanceMonitoringService.monitorPerformance();
    }
}
