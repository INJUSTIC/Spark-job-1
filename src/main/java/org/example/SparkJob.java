package org.example;

import cloud.ilum.job.Job;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.spark.sql.SparkSession;
import org.example.model.PersonData;
import org.example.requests.RequestController;
import scala.Option;
import scala.Some;
import scala.collection.immutable.Map;

import java.util.List;

public class SparkJob implements Job {

    @Override
    public Option<String> run(SparkSession sparkSession, Map<String, Object> config) {
        String userParam = config.getOrElse("requestUrl", () -> "generate/json/1000");
        if (userParam.matches("^generate/json/\\d+$")) {
            int number = Integer.parseInt(userParam.split("/")[2]);
            List<PersonData> peopleData = RequestController.generateJson(number);
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectWriter writer = objectMapper.writer(new DefaultPrettyPrinter());
            try {
                return Some.apply(writer.writeValueAsString(peopleData));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        else if (userParam.matches("^data/csv/\\d+$")) {
            int number = Integer.parseInt(userParam.split("/")[2]);
            return Some.apply(RequestController.convertToCSV(number));
        }
        else if (userParam.matches("^data/customCsv/\\d+\\?columns=.+$")) {
            int number = Integer.parseInt(userParam.split("\\?columns")[0].split("/")[2]);
            List<String> columns = List.of(userParam.split("\\?columns=")[1].split("\\s*,\\s*"));
            return Some.apply(RequestController.convertToCSVWithGivenColumns(columns, number));
        }
        else if (userParam.matches("^calculate/\\d+\\?operations=.+$")) {
            int number = Integer.parseInt(userParam.split("\\?operations")[0].split("/")[1]);
            String operations = userParam.split("\\?operations=")[1];
            return Some.apply(RequestController.performOperations(number, operations));
        }
        else if (userParam.equals("statistics")) {
            return Some.apply(RequestController.monitorPerformance());
        }
        else {
            return Some.apply("Invalid request");
        }
    }
}
