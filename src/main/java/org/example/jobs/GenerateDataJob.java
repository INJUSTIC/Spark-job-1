package org.example.jobs;

import cloud.ilum.job.Job;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.spark.sql.SparkSession;
import org.example.model.PersonData;
import org.example.services.DataGenerationService;
import scala.Option;
import scala.Some;
import scala.collection.immutable.Map;

import java.util.List;

public class GenerateDataJob implements Job {

    @Override
    public Option<String> run(SparkSession sparkSession, Map<String, Object> config) {
        int size = Integer.parseInt((config.getOrElse("size", () -> "1000")));
        if (size < 0) throw new IllegalArgumentException("size is negative");
        DataGenerationService dataGenerationService = new DataGenerationService();
        List<PersonData> peopleData = dataGenerationService.generateRandomPersonDataList(size);
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
        try {
            return Some.apply(writer.writeValueAsString(peopleData));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
