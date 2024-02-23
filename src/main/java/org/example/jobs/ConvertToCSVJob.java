package org.example.jobs;

import cloud.ilum.job.Job;
import org.apache.spark.sql.SparkSession;
import org.example.model.PersonData;
import org.example.services.DataGenerationService;
import org.example.services.CSVConversionService;
import scala.Option;
import scala.Some;
import scala.collection.immutable.Map;
import java.util.List;

public class ConvertToCSVJob implements Job {
    @Override
    public Option<String> run(SparkSession sparkSession, Map<String, Object> config) {
        int size = Integer.parseInt((config.getOrElse("size", () -> "1000")));
        if (size < 0) throw new IllegalArgumentException("size is negative");
        DataGenerationService dataGenerationService = new DataGenerationService();
        List<PersonData> peopleData = dataGenerationService.generateRandomPersonDataList(size);
        CSVConversionService csvConverterService = new CSVConversionService();
        return Some.apply(csvConverterService.convertToCSV(peopleData));
    }
}
