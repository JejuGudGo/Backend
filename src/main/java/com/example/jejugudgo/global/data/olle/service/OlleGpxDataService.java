package com.example.jejugudgo.global.data.olle.service;

import com.example.jejugudgo.domain.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.olle.entity.JejuOlleLocationData;
import com.example.jejugudgo.domain.olle.entity.OlleType;
import com.example.jejugudgo.domain.olle.repository.JejuOlleCourseRepository;
import com.example.jejugudgo.domain.olle.repository.JejuOlleLocationDataRepository;
import com.example.jejugudgo.global.data.common.entity.DataCommandLog;
import com.example.jejugudgo.global.data.common.repository.DataCommandLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OlleGpxDataService {

    private final JejuOlleLocationDataRepository locationDataRepository;
    private final JejuOlleCourseRepository courseRepository;
    private final DataCommandLogRepository logRepository;

    @Transactional
    public void loadGpxData() {
        if (isDataAlreadyLoaded()) {
            log.info("데이터가 이미 적재되어 있습니다. 추가 작업을 수행하지 않습니다.");
            return;
        }

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("classpath:gpx/*.gpx");

            for (Resource resource : resources) {
                try (InputStream inputStream = resource.getInputStream()) {
                    String fileName = resource.getFilename();

                    String courseTitle = fileName.replace("제주올레길_", "")
                            .replace(".gpx", "")
                            .replace("_", " ");

                    JejuOlleCourse course = courseRepository.findByTitle(courseTitle)
                            .orElseGet(() -> courseRepository.save(JejuOlleCourse.builder()
                                    .title(courseTitle)
                                    .olleType(OlleType.JEJU)
                                    .build()));

                    parseAndSaveGpxFile(inputStream, course);

                    log.info("Successfully processed GPX file: {}", fileName);
                } catch (IOException e) {
                    log.error("Error reading GPX file: {}", resource.getFilename(), e);
                }
            }

            saveDataLoadLog();
            log.info("GPX 데이터가 성공적으로 JejuOlleCourse와 JejuOlleLocationData 테이블에 저장되었습니다.");

        } catch (IOException e) {
            log.error("Error loading GPX resources", e);
        }
    }

    private boolean isDataAlreadyLoaded() {
        return logRepository.findByConfigKey("OlleGpxData")
                .map(DataCommandLog::isConfigValue)
                .orElse(false);
    }

    private void parseAndSaveGpxFile(InputStream inputStream, JejuOlleCourse course) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            var doc = builder.parse(inputStream);
            var nodeList = doc.getElementsByTagName("trkpt");

            List<JejuOlleLocationData> locationDataList = new ArrayList<>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                var node = nodeList.item(i);
                var element = (Element) node;

                double latitude = Double.parseDouble(element.getAttribute("lat"));
                double longitude = Double.parseDouble(element.getAttribute("lon"));

                // time 값 가져오기
                OffsetDateTime time = OffsetDateTime.now();
                if (element.getElementsByTagName("time").getLength() > 0 && element.getElementsByTagName("time").item(0) != null) {
                    String timeText = element.getElementsByTagName("time").item(0).getTextContent();
                    if (!timeText.isEmpty()) {  // time 값이 비어 있지 않은 경우에만 파싱
                        time = OffsetDateTime.parse(timeText);
                    } else {
                        log.warn("Time value is empty for a location point, setting current time as default.");
                    }
                } else {
                    log.warn("Time element not found for a location point, setting current time as default.");
                }

                JejuOlleLocationData locationData = JejuOlleLocationData.builder()
                        .latitude(latitude)
                        .longitude(longitude)
                        .time(time)
                        .locationOrder((long) i + 1)
                        .jejuOlleCourse(course)
                        .updatedAt(LocalDate.now())
                        .build();

                locationDataList.add(locationData);
            }

            if (!locationDataList.isEmpty()) {
                locationDataRepository.saveAll(locationDataList);
                log.info("Location data saved for course: {}", course.getTitle());
            }

        } catch (Exception e) {
            log.error("Error parsing GPX file for course: {}", course.getTitle(), e);
        }
    }

    private void saveDataLoadLog() {
        DataCommandLog dataCommandLog = DataCommandLog.builder()
                .configKey("OlleGpxData")
                .configValue(true)
                .updatedAt(LocalDate.now())
                .build();
        logRepository.save(dataCommandLog);
    }
}
