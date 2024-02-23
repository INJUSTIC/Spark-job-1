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

public class ConvertToCSVWithGivenColumnsJob implements Job {
    @Override
    public Option<String> run(SparkSession sparkSession, Map<String, Object> config) {
        int size = Integer.parseInt((config.getOrElse("size", () -> "1000")));
        String columns = config.getOrElse("columns", () -> "_type, key, name, latitude, longitude");
        String[] columnArray = columns.split("\\s*,\\s*");
        DataGenerationService dataGenerationService = new DataGenerationService();
        List<PersonData> peopleData = dataGenerationService.generateRandomPersonDataList(size);
        CSVConversionService csvConverterService = new CSVConversionService();
        return Some.apply(csvConverterService.convertToCSVWithGivenColumns(peopleData, columnArray));
    }
}
