package com.gudgo.jeju.global.data.olle.service;

import com.gudgo.jeju.domain.olle.entity.JeJuOlleCourse;
import com.gudgo.jeju.domain.olle.entity.JeJuOlleCourseData;
import com.gudgo.jeju.domain.olle.entity.OlleType;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleCourseDataRepository;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleCourseRepository;
import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import com.gudgo.jeju.domain.planner.course.repository.CourseRepository;
import com.gudgo.jeju.domain.planner.planner.service.PlannerService;
import com.gudgo.jeju.global.data.common.entity.DataConfiguration;
import com.gudgo.jeju.global.data.tourAPI.repository.DataConfigurationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class JejuOlleDatabaseService {
    private final JeJuOlleCourseRepository jeJuOlleCourseRepository;
    private final JeJuOlleCourseDataRepository jeJuOlleCourseDataRepository;
    private final DataConfigurationRepository dataConfigurationRepository;
    private final CourseRepository courseRepository;
    private final PlannerService plannerService;

    @Transactional
    public void processJejuOlleData() {
        try {
            log.info("Starting processJejuOlleData");
            if (!isOlleDataLoaded()) {
                log.info("Loading Jeju Olle data");
                loadJejuOlleData();
            } else {
                log.info("Olle data already loaded");
            }

            if (isInitializationRequired()) {
                log.info("Creating Courses and Planners from existing Jeju Olle data");
                createCoursesAndPlannersFromJejuOlleData();
                markInitializationComplete();
            } else {
                log.info("Initialization already completed, skipping process");
            }
            log.info("Finished processJejuOlleData");
        } catch (Exception e) {
            log.error("Unexpected error in processJejuOlleData", e);
            throw new RuntimeException("Failed to process Jeju Olle data", e);
        }
    }

    private boolean isOlleDataLoaded() {
        DataConfiguration dataConfig = dataConfigurationRepository.findByConfigKey("OlleDataLoaded")
                .orElse(null);
        return dataConfig != null && dataConfig.isConfigValue();
    }

    public boolean loadJejuOlleData() {
        log.info("Starting loadJejuOlleData");
        try {
            log.info("Starting data loading process");
            convertGpxToDatabase();
            addAdditionalData();
            updateDataLoadedStatus();
            log.info("Successfully loaded Jeju Olle courses");
            return true;
        } catch (IOException e) {
            log.error("Error occurred while loading Jeju Olle data", e);
            return false;
        }
    }

    private boolean isInitializationRequired() {
        DataConfiguration initConfig = dataConfigurationRepository.findByConfigKey("JejuOlleCoursePlannerInitializationCompleted")
                .orElse(null);
        return initConfig == null || !initConfig.isConfigValue();
    }

    private void markInitializationComplete() {
        DataConfiguration initConfig = dataConfigurationRepository.findByConfigKey("JejuOlleCoursePlannerInitializationCompleted")
                .orElse(DataConfiguration.builder()
                        .configKey("JejuOlleCoursePlannerInitializationCompleted")
                        .configValue(false)
                        .updatedAt(LocalDate.now())
                        .build());

        initConfig = initConfig.withConfigValue(true)
                .withUpdatedAt(LocalDate.now());

        dataConfigurationRepository.save(initConfig);
        log.info("Marked Jeju Olle Course and Planner initialization as completed");
    }

    public void createCoursesAndPlannersFromJejuOlleData() {
        log.info("Starting createCoursesAndPlannersFromJejuOlleData");
        try {
            List<JeJuOlleCourse> olleCourses = jeJuOlleCourseRepository.findByOlleType(OlleType.JEJU);
            for (JeJuOlleCourse olleCourse : olleCourses) {
                if (!isOlleCourseAlreadyProcessed(olleCourse)) {
                    plannerService.createOllePlanner(olleCourse);
                    log.info("Created Course and Planner for Jeju Olle course: {}", olleCourse.getCourseNumber());
                } else {
                    log.info("Course and Planner already exist for Jeju Olle course: {}", olleCourse.getCourseNumber());
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while creating Courses and Planners from Jeju Olle data", e);
            throw new RuntimeException("Failed to create Courses and Planners from Jeju Olle data", e);
        }
    }

    private boolean isOlleCourseAlreadyProcessed(JeJuOlleCourse olleCourse) {
        return courseRepository.existsByTypeAndOlleCourseId(CourseType.JEJU, olleCourse.getId());
    }

    private void updateDataLoadedStatus() {
        log.info("Updating data loaded status");
        DataConfiguration dataConfig = dataConfigurationRepository.findByConfigKey("OlleDataLoaded")
                .orElse(DataConfiguration.builder()
                        .configKey("OlleDataLoaded")
                        .configValue(false)
                        .updatedAt(LocalDate.now())
                        .build());

        DataConfiguration updatedConfig = dataConfig
                .withConfigValue(true)
                .withUpdatedAt(LocalDate.now());

        dataConfigurationRepository.save(updatedConfig);
        log.info("Data loaded status updated successfully");
    }

    @Transactional
    public void convertGpxToDatabase() throws IOException {
        DataConfiguration checkDataConfig = dataConfigurationRepository.findByConfigKey("OlleDataLoaded")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:gpx/*.gpx");

            for (Resource resource : resources) {
                try (InputStream inputStream = resource.getInputStream()) {
                    String fileName = resource.getFilename();

                    boolean wheelchairAccessible = fileName.contains("휠체어구간");
                    String courseTitle = fileName.replace("제주올레길_", "")
                            .replace("휠체어구간_", "")
                            .replace(".gpx", "")
                            .replace("_", " ");

                    String courseNumber = extractCourseNumberFromTitle(courseTitle);

                    if (courseNumber != null) {
                        parseAndSaveGpxFile(inputStream, courseNumber, courseTitle, wheelchairAccessible);
                    } else {
                        log.warn("===============================================================================");
                        log.warn("Course number not found for file: {}", fileName);
                        log.warn("===============================================================================");
                    }

                } catch (IOException e) {
                    log.error("Error reading GPX file: {}", resource.getFilename(), e);
                }
            }

            updateDataLoadedStatus();
            log.info("===============================================================================");
            log.info("OlleData loaded successfully");
            log.info("===============================================================================");
        } else {
            log.info("===============================================================================");
            log.info("OlleData is already loaded");
            log.info("===============================================================================");
        }
    }

    private String extractCourseNumberFromTitle(String title) {
        Pattern englishPattern = Pattern.compile("[a-zA-Z]");
        Matcher englishMatcher = englishPattern.matcher(title);

        if (englishMatcher.find()) {
            Pattern coursePattern = Pattern.compile("([0-9]+(-[A-Z]))코스");
            Matcher courseMatcher = coursePattern.matcher(title);

            if (courseMatcher.find()) {
                return courseMatcher.group(1);
            }
        } else {
            Pattern coursePattern = Pattern.compile("([0-9]+(-[0-9]?)?)코스");
            Matcher courseMatcher = coursePattern.matcher(title);

            if (courseMatcher.find()) {
                return courseMatcher.group(1);
            }
        }

        return null;
    }

    private void parseAndSaveGpxFile(InputStream inputStream, String courseNumber, String title, boolean wheelchairAccessible) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(inputStream);

            List<JeJuOlleCourseData> jeJuOlleCourseDataList = new ArrayList<>();
            jeJuOlleCourseDataList.addAll(parseWaypoints(document, "wpt"));
            jeJuOlleCourseDataList.addAll(parseWaypoints(document, "trkpt"));

            if (!jeJuOlleCourseDataList.isEmpty()) {
                JeJuOlleCourse course = JeJuOlleCourse.builder()
                        .olleType(OlleType.JEJU)
                        .courseNumber(courseNumber)
                        .title(title)
                        .startLatitude(jeJuOlleCourseDataList.get(0).getLatitude())
                        .startLongitude(jeJuOlleCourseDataList.get(0).getLongitude())
                        .endLatitude(jeJuOlleCourseDataList.get(jeJuOlleCourseDataList.size() - 1).getLatitude())
                        .endLongitude(jeJuOlleCourseDataList.get(jeJuOlleCourseDataList.size() - 1).getLongitude())
                        .wheelchairAccessible(wheelchairAccessible)
                        .build();

                course = jeJuOlleCourseRepository.save(course);

                for (JeJuOlleCourseData jeJuOlleCourseData : jeJuOlleCourseDataList) {
                    jeJuOlleCourseData.withJeJuOlleCourse(course);
                }

                jeJuOlleCourseDataRepository.saveAll(jeJuOlleCourseDataList);
            }
        } catch (Exception e) {
            log.error("===============================================================================");
            log.error("Error parsing GPX file for course: {}", courseNumber, e);
            log.error("===============================================================================");
        }
    }

    private List<JeJuOlleCourseData> parseWaypoints(Document document, String tagName) {
        NodeList wptNodes = document.getElementsByTagName(tagName);
        List<JeJuOlleCourseData> jeJuOlleCourseDataList = new ArrayList<>();

        for (int i = 0; i < wptNodes.getLength(); i++) {
            Element wptElement = (Element) wptNodes.item(i);

            double lat = Double.parseDouble(wptElement.getAttribute("lat"));
            double lon = Double.parseDouble(wptElement.getAttribute("lon"));
            double ele = parseDouble(getElementTextContent(wptElement, "ele"));
            OffsetDateTime time = parseOffsetDateTime(getElementTextContent(wptElement, "time"));

            JeJuOlleCourseData jeJuOlleCourseData = JeJuOlleCourseData.builder()
                    .latitude(lat)
                    .longitude(lon)
                    .altitude(ele)
                    .time(time)
                    .orderNumber((long) (i + 1))
                    .updatedAt(LocalDate.now())
                    .build();

            jeJuOlleCourseDataList.add(jeJuOlleCourseData);
        }

        return jeJuOlleCourseDataList;
    }

    private double parseDouble(String value) {
        if (value == null || value.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(value);
    }

    private OffsetDateTime parseOffsetDateTime(String value) {
        if (value == null || value.isEmpty()) {
            return OffsetDateTime.now();
        }
        return OffsetDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME);
    }

    private String getElementTextContent(Element parentElement, String tagName) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);

        if (nodeList.getLength() > 0) {
            return nodeList.item(0).getTextContent();
        }
        return "";
    }

    public void addAdditionalData() throws IOException {
        DataConfiguration checkDataConfig = dataConfigurationRepository.findByConfigKey("OlleAdditionalDataLoaded")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("csv/olle_course.csv").getInputStream()))) {
                String line;

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    String courseNumber = data[0];
                    String totalDistance = data[1];
                    String totalTime = data[2];

                    JeJuOlleCourse jeJuOlleCourse = jeJuOlleCourseRepository.findByOlleTypeAndCourseNumberAndWheelchairAccessible(OlleType.JEJU, courseNumber, false);

                    if (jeJuOlleCourse != null) {
                        jeJuOlleCourse = jeJuOlleCourse.withTotalDistanceAndTotalTime(totalDistance, totalTime);
                        jeJuOlleCourseRepository.save(jeJuOlleCourse);
                    } else {
                        log.warn("Course not found for number: {}", courseNumber);
                    }
                }
            }

            updateAdditionalDataLoadedStatus();
            log.info("===============================================================================");

        } else {
            log.info("===============================================================================");
            log.info("Olle additional Data is already loaded");
            log.info("===============================================================================");
        }
    }

    private void updateAdditionalDataLoadedStatus() {
        DataConfiguration dataConfig = dataConfigurationRepository.findByConfigKey("OlleAdditionalDataLoaded")
                .orElse(DataConfiguration.builder()
                        .configKey("OlleAdditionalDataLoaded")
                        .configValue(false)
                        .updatedAt(LocalDate.now())
                        .build());

        DataConfiguration updatedConfig = dataConfig
                .withConfigValue(true)
                .withUpdatedAt(LocalDate.now());

        dataConfigurationRepository.save(updatedConfig);
    }
}