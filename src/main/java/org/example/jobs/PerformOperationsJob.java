package org.example.jobs;

import cloud.ilum.job.Job;
import org.apache.spark.sql.SparkSession;
import org.example.model.PersonData;
import org.example.operations.*;
import org.example.services.OperationPerformingService;
import org.example.services.DataGenerationService;
import scala.Option;
import scala.Some;
import scala.collection.immutable.Map;
import java.util.List;

public class PerformOperationsJob implements Job {
    @Override
    public Option<String> run(SparkSession sparkSession, Map<String, Object> config) {
        int size = Integer.parseInt((config.getOrElse("size", () -> "1000")));
        String operations = config.getOrElse("operations", () -> "latitude*longitude, distance-latitude, sqrt(distance), latitude^2");
        String[] operationsArray = operations.split("\\s*,\\s*");
        DataGenerationService dataGenerationService = new DataGenerationService();
        List<PersonData> peopleData = dataGenerationService.generateRandomPersonDataList(size);
        java.util.Map<String, Operation> operationsMap = java.util.Map.of(
                "+", new Addition(),
                "-", new Subtraction(),
                "*", new Multiplication(),
                "/", new Division(),
                "^", new Power(),
                "sqrt", new SquareRoot()
        );
        OperationPerformingService operationPerformer = new OperationPerformingService(operationsMap);
        return Some.apply(operationPerformer.performOperations(peopleData, operationsArray));
    }
}
