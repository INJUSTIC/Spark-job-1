package org.example.dataGeneration;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.example.requests.RequestController;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class PerformanceMonitoringService {
    private static final SimpleMeterRegistry meterRegistry;
    private static final Map<Long, Double> usedMemoryByTime;
    //private final Map<Long, Double> usedCPUByTime;
    private static long startTime, endTime;
    private static ScheduledFuture<?> scheduledFuture;
    private static ScheduledExecutorService executor;
    private static StringBuilder finalReport;

    static {
        meterRegistry = new SimpleMeterRegistry();
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        usedMemoryByTime = new LinkedHashMap<>();
        Gauge.builder("jvm.memory.used", memoryBean, bean -> bean.getHeapMemoryUsage().getUsed())
                .baseUnit("bytes")
                .register(meterRegistry);
    }

    public static String monitorPerformance() {
        //Usuwamy stare dane
        finalReport = new StringBuilder();
        //monitorowanie pamięci i tworzenie raportu dla size = 1000, 10 000 oraz 100 000 obiektów JSON
        for (int size = 1000; size <= 100000; size*=10) {
            finalReport.append(size).append(" JSON objects \n\n\n");
            finalReport.append("First service (3->1)\n\n");
            finalReport.append("JSON generation endpoint \n\n");
            usedMemoryByTime.clear();
            startMonitoring();
            RequestController.generateJson(size);
            stopMonitoring();
            finalReport.append(generatePerformanceReport());
            finalReport.append("\n\nSecond service (3->2->1)\n\n");
            finalReport.append("CSV generation with predefined columns \n\n");
            usedMemoryByTime.clear();
            startMonitoring();
            RequestController.convertToCSV(size);
            stopMonitoring();
            finalReport.append(generatePerformanceReport());
            finalReport.append("\n\nCSV generation with given columns (for this example columns are: _type, key, name, latitude, longitude) \n\n");
            usedMemoryByTime.clear();
            startMonitoring();
            RequestController.convertToCSVWithGivenColumns(Arrays.asList("_type","key","name","latitude","longitude"), size);
            stopMonitoring();
            finalReport.append(generatePerformanceReport());
            finalReport.append("\n\nOperation performer (for this example operations are: latitude*longitude, distance-latitude, sqrt(distance), latitude^2) \n\n");
            usedMemoryByTime.clear();
            startMonitoring();
            RequestController.performOperations(size, "latitude*longitude,distance-latitude,sqrt(distance),latitude^2");
            stopMonitoring();
            finalReport.append(generatePerformanceReport()).append("\n\n\n");
        }

        return finalReport.toString();
    }

    //początek monitorowania zasobów
    private static void startMonitoring() {
        //ustawianie funkcji która będzie się wywoływała po każdym update'u
        Runnable monitorMemoryAndCpu = () -> {
            long currTime = System.currentTimeMillis();
            long timeDiff = currTime - startTime;
            double usedMemory = meterRegistry.get("jvm.memory.used").gauge().value();
            usedMemoryByTime.put(timeDiff, usedMemory);
        };
        executor = Executors.newScheduledThreadPool(1);
        scheduledFuture = executor.scheduleAtFixedRate(
                monitorMemoryAndCpu,
                0,
                10, // update co 10 milisekund
                TimeUnit.MILLISECONDS
        );
        startTime = System.currentTimeMillis();
    }

    //skończenie monitorowania zasobów
    private static void stopMonitoring() {
        endTime = System.currentTimeMillis();
        scheduledFuture.cancel(true);
        executor.shutdownNow();
        try {
            //czekamy dopóki nie skończy się
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    //generowanie raportu dla poszczególnego endpoint'u
    private static String generatePerformanceReport() {
        StringBuilder report = new StringBuilder();
        report.append("Request time: ").append(endTime - startTime).append(" ms").append("\n\n");
        report.append("Memory used\n");
        for (long monitoredTime: usedMemoryByTime.keySet()) {
            double memoryUsed = (usedMemoryByTime.get(monitoredTime) / (1024.0 * 1024.0));
            String memoryUsedToShow = String.format("%.1f", memoryUsed);
            report.append(monitoredTime).append("ms: ").append(memoryUsedToShow).append(" MB").append("\n");
        }
        return report.toString();
    }
}
