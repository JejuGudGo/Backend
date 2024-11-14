package com.example.jejugudgo.global.data.olle.service;

import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.course.olle.entity.JejuOlleSpot;
import com.example.jejugudgo.domain.course.olle.entity.OlleType;
import com.example.jejugudgo.domain.course.olle.repository.JejuOlleCourseRepository;
import com.example.jejugudgo.domain.course.olle.repository.JejuOlleSpotRepository;
import com.example.jejugudgo.global.data.common.entity.DataCommandLog;
import com.example.jejugudgo.global.data.common.repository.DataCommandLogRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OlleCsvDataService {

    private final JejuOlleCourseRepository courseRepository;
    private final JejuOlleSpotRepository spotRepository;
    private final DataCommandLogRepository logRepository;

    @Transactional
    public void loadOlleCourseData() {
        if (isDataAlreadyLoaded()) {
            log.info("===============================================================================");
            log.info("Olle csv data is already loaded");
            log.info("===============================================================================");
            return;
        }

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(new ClassPathResource("csv/olle/ollecourse.csv").getInputStream()))) {
            Map<String, JejuOlleCourse> courseCache = new HashMap<>();
            Map<String, JejuOlleSpot> minSpotOrderMap = new HashMap<>();
            Map<String, JejuOlleSpot> maxSpotOrderMap = new HashMap<>();

            String[] data;
            while ((data = csvReader.readNext()) != null) {
                processCsvRow(data, courseCache, minSpotOrderMap, maxSpotOrderMap);
            }

            updateCourseStartAndEndSpots(courseCache, minSpotOrderMap, maxSpotOrderMap);
            saveDataLoadLog();

            log.info("===============================================================================");
            log.info("CSV data has been successfully stored in the JejuOlleCourse and JejuOlleSpot tables.");
            log.info("===============================================================================");

        } catch (IOException | CsvValidationException e) {
            log.info("===============================================================================");
            log.error("An error occurred while reading the CSV file.", e);
            log.info("===============================================================================");
        }
    }

    private boolean isDataAlreadyLoaded() {
        return logRepository.findByConfigKey("OlleCourseData")
                .map(DataCommandLog::isConfigValue)
                .orElse(false);
    }

    private void processCsvRow(String[] data, Map<String, JejuOlleCourse> courseCache,
                               Map<String, JejuOlleSpot> minSpotOrderMap, Map<String, JejuOlleSpot> maxSpotOrderMap) {
        OlleType olleType = determineOlleType(data[0]);
        String title = formatTitleWithPrefix(olleType, sanitizeField(data[1]));
        String totalDistance = sanitizeField(data[2]);
        String totalTime = sanitizeField(data[3]);
        String spotTitle = sanitizeField(data[4]);
        Double latitude = parseDoubleOrNull(data[5]);
        Double longitude = parseDoubleOrNull(data[6]);
        Long spotOrder = parseLongOrNull(data[7]);
        String summary = sanitizeField(data[8]);
        String infoAddress = sanitizeField(data[9]);
        String infoOpenTime = sanitizeField(data[10]);

        JejuOlleCourse course = courseCache.computeIfAbsent(title, key -> getOrSaveCourse(olleType, title, totalDistance, totalTime, summary, infoAddress, infoOpenTime));
        JejuOlleSpot spot = saveSpot(spotTitle, latitude, longitude, spotOrder, course);

        updateSpotOrderMaps(title, spot, minSpotOrderMap, maxSpotOrderMap);
    }

    private JejuOlleCourse getOrSaveCourse(OlleType olleType, String title, String totalDistance, String totalTime, String summary, String infoAddress, String infoOpenTime) {
        return courseRepository.findByTitle(title).orElseGet(() -> courseRepository.save(
                JejuOlleCourse.builder()
                        .olleType(olleType)
                        .title(title)
                        .totalDistance(totalDistance)
                        .totalTime(totalTime)
                        .summary(summary)
                        .infoAddress(infoAddress)
                        .infoOpenTime(infoOpenTime)
                        .build()
        ));
    }

    private JejuOlleSpot saveSpot(String spotTitle, Double latitude, Double longitude, Long spotOrder, JejuOlleCourse course) {
        return spotRepository.save(JejuOlleSpot.builder()
                .title(spotTitle)
                .latitude(latitude)
                .longitude(longitude)
                .spotOrder(spotOrder)
                .jejuOlleCourse(course)
                .build()
        );
    }

    private void updateSpotOrderMaps(String title, JejuOlleSpot spot, Map<String, JejuOlleSpot> minSpotOrderMap, Map<String, JejuOlleSpot> maxSpotOrderMap) {
        minSpotOrderMap.merge(title, spot, (existingSpot, newSpot) -> existingSpot.getSpotOrder() < newSpot.getSpotOrder() ? existingSpot : newSpot);
        maxSpotOrderMap.merge(title, spot, (existingSpot, newSpot) -> existingSpot.getSpotOrder() > newSpot.getSpotOrder() ? existingSpot : newSpot);
    }

    private void updateCourseStartAndEndSpots(Map<String, JejuOlleCourse> courseCache,
                                              Map<String, JejuOlleSpot> minSpotOrderMap, Map<String, JejuOlleSpot> maxSpotOrderMap) {
        courseCache.forEach((title, course) -> {
            JejuOlleSpot startSpot = minSpotOrderMap.get(title);
            JejuOlleSpot endSpot = maxSpotOrderMap.get(title);
            JejuOlleCourse updatedCourse = course.updateStartSpotEndSpot(
                    startSpot.getTitle(), startSpot.getLatitude(), startSpot.getLongitude(),
                    endSpot.getTitle(), endSpot.getLatitude(), endSpot.getLongitude()
            );
            courseRepository.save(updatedCourse);
        });
    }

    private void saveDataLoadLog() {
        DataCommandLog dataCommandLog = DataCommandLog.builder()
                .configKey("OlleCourseData")
                .configValue(true)
                .updatedAt(LocalDate.now())
                .build();
        logRepository.save(dataCommandLog);
    }

    private OlleType determineOlleType(String type) {
        return "제주올레".equals(type) ? OlleType.JEJU : OlleType.HAYOUNG;
    }

    private String formatTitleWithPrefix(OlleType olleType, String title) {
        return title == null ? null : (olleType == OlleType.JEJU ? "제주올레길_" : "하영올레길_") + title;
    }

    private String sanitizeField(String value) {
        if (value == null || value.trim().isEmpty() || "null".equalsIgnoreCase(value)) {
            return null;
        }
        return value.startsWith("\"") && value.endsWith("\"") ? value.substring(1, value.length() - 1) : value;
    }

    private Double parseDoubleOrNull(String value) {
        String sanitizedValue = sanitizeField(value);
        return sanitizedValue != null ? Double.parseDouble(sanitizedValue) : null;
    }

    private Long parseLongOrNull(String value) {
        String sanitizedValue = sanitizeField(value);
        return sanitizedValue != null ? Long.parseLong(sanitizedValue) : null;
    }
}
