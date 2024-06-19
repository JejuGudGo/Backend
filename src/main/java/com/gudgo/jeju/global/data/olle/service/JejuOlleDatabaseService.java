package com.gudgo.jeju.global.data.olle.service;

import com.gudgo.jeju.global.data.olle.entity.JeJuOlleCourse;
import com.gudgo.jeju.global.data.olle.entity.JeJuOlleCourseData;
import com.gudgo.jeju.global.data.olle.entity.OlleType;
import com.gudgo.jeju.global.data.olle.repository.JeJuOlleCourseDataRepository;
import com.gudgo.jeju.global.data.olle.repository.JeJuOlleCourseRepository;
import com.gudgo.jeju.global.data.tourAPI.common.entity.DataConfiguration;
import com.gudgo.jeju.global.data.tourAPI.common.repository.DataConfigurationRepository;
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
    private final JeJuOlleCourseRepository courseRepository;
    private final JeJuOlleCourseDataRepository courseDataRepository;
    private final DataConfigurationRepository dataConfigurationRepository;

    public void convertGpxToDatabase() throws IOException {
        DataConfiguration checkDataConfig = dataConfigurationRepository.findByConfigKey("OlleDataLoaded")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources("classpath:gpx/*.gpx");

            for (Resource resource : resources) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                    String fileName = resource.getFilename();
                    boolean wheelchairAccessible = fileName.contains("휠체어구간");
                    String courseTitle = fileName.replace("제주올레길_", "")
                            .replace("휠체어구간 ", "")
                            .replace(".gpx", "")
                            .replace("_", " ");
                    String courseNumber = extractCourseNumber(fileName);

                    if (courseNumber != null) {
                        parseAndSaveGpxFile(resource, courseNumber, courseTitle, wheelchairAccessible);
                    } else {
                        log.warn("===============================================================================");
                        log.warn("Course number not found for file: {}", fileName);
                        log.warn("===============================================================================");
                    }
                }
            }

            if (checkDataConfig == null) {
                DataConfiguration dataConfiguration = DataConfiguration.builder()
                        .configKey("OlleDataLoaded")
                        .configValue(true)
                        .updatedAt(LocalDate.now())
                        .build();

                dataConfigurationRepository.save(dataConfiguration);

            } else if (!checkDataConfig.isConfigValue()){
                checkDataConfig.withConfigValue(true);
                dataConfigurationRepository.save(checkDataConfig);
            }

            log.info("===============================================================================");
            log.info("OlleData loaded successfully");
            log.info("===============================================================================");

        } else {
            log.info("===============================================================================");
            log.info("OlleData is already loaded");
            log.info("===============================================================================");
        }
    }

    private String extractCourseNumber(String fileName) {
        Pattern pattern = Pattern.compile("제주올레길(?:휠체어구간 )?_([0-9]+(-[0-9]+)?)코스");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private void parseAndSaveGpxFile(Resource resource, String courseNumber, String title, boolean wheelchairAccessible) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(resource.getInputStream());

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

                course = courseRepository.save(course);

                for (JeJuOlleCourseData jeJuOlleCourseData : jeJuOlleCourseDataList) {
                    jeJuOlleCourseData.withJeJuOlleCourse(course);
                }

                courseDataRepository.saveAll(jeJuOlleCourseDataList);
            }
        } catch (Exception e) {
            log.error("===============================================================================");
            log.error("Error parsing GPX file: {}", resource.getFilename(), e);
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
}