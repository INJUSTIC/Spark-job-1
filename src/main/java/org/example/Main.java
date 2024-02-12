package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.example.model.PersonData;
import org.example.requests.RequestController;
import scala.Some;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(RequestController.monitorPerformance());
    }
}