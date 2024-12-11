package com.example.jejugudgo.global.data.course;

import com.example.jejugudgo.domain.course.common.entity.OlleCourse;
import com.example.jejugudgo.domain.course.common.entity.OlleCourseLineData;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseLineDataRepository;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseRepository;
import com.example.jejugudgo.global.data.common.entity.DataCommandLog;
import com.example.jejugudgo.global.data.common.repository.DataCommandLogRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class OlleGpxDataService {
    private final OlleCourseLineDataRepository olleCourseLineDataRepository;
    private final OlleCourseRepository olleCourseRepository;
    private final DataCommandLogRepository logRepository;

    @Transactional
    public void loadOlleGpxCsvToDatabase() {
        if (isDataAlreadyLoaded()) {
            log.info("===============================================================================");
            log.info("Olle gpx data is already loaded");
            log.info("===============================================================================");
            return;
        }

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            Resource[] resources = resolver.getResources("classpath:gpx/*.gpx");

            for (Resource resource : resources) {
                try (InputStream inputStream = resource.getInputStream()) {
                    String fileName = resource.getFilename();
                    Long courseId = getCourseIdx(fileName);
                    OlleCourse course = olleCourseRepository.findById(courseId)
                            .orElse(null);

                    if (course != null)
                        parseAndSaveGpxFile(inputStream, course);

                } catch (IOException e) {
                    throw new CustomException(RetCode.RET_CODE92);
                }
            }

            saveDataLoadLog();
            log.info("===============================================================================");
            log.info("GPX data has been successfully processed.");
            log.info("===============================================================================");
        } catch (IOException e) {
            throw new CustomException(RetCode.RET_CODE92);
        }
    }

    private boolean isDataAlreadyLoaded() {
        return logRepository.findByConfigKey("OlleGpxData")
                .map(DataCommandLog::isConfigValue)
                .orElse(false);
    }

    private void parseAndSaveGpxFile(InputStream inputStream, OlleCourse course) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            var doc = builder.parse(inputStream);
            var nodeList = doc.getElementsByTagName("trkpt");

            List<OlleCourseLineData> locationDataList = new ArrayList<>();

            for (int i = 0; i < nodeList.getLength(); i++) {
                var node = nodeList.item(i);
                var element = (Element) node;

                double latitude = Double.parseDouble(element.getAttribute("lat"));
                double longitude = Double.parseDouble(element.getAttribute("lon"));

                OlleCourseLineData locationData = OlleCourseLineData.builder()
                        .latitude(latitude)
                        .longitude(longitude)
                        .dataOrder((long) i + 1)
                        .olleCourse(course)
                        .build();

                locationDataList.add(locationData);
            }

            if (!locationDataList.isEmpty()) {
                olleCourseLineDataRepository.saveAll(locationDataList);
            }

        } catch (Exception e) {
            throw new CustomException(RetCode.RET_CODE92);
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

    private Long getCourseIdx(String title) {
        System.out.println("==============");
        System.out.println("title:" +title);
        System.out.println("==============");

        String header = "제주올레길_";
        String end = "코스.gpx";

        if (title.equals(header + "1" + end))
            return 1L;
        else if (title.equals(header + "1-1" + end))
            return 2L;
        else if (title.equals(header + "2" + end))
            return 3L;
        else if (title.equals(header + "3" + end))
            return 4L;
        else if (title.equals(header + "4" + end))
            return 5L;
        else if (title.equals(header + "5" + end))
            return 6L;
        else if (title.equals(header + "6" + end))
            return 7L;
        else if (title.equals(header + "7" + end))
            return 8L;
        else if (title.equals(header + "7-1" + end))
            return 9L;
        else if (title.equals(header + "8" + end))
            return 10L;
        else if (title.equals(header + "9" + end))
            return 11L;
        else if (title.equals(header + "10" + end))
            return 12L;
        else if (title.equals(header + "10-1" + end))
            return 13L;
        else if (title.equals(header + "11" + end))
            return 14L;
        else if (title.equals(header + "12" + end))
            return 15L;
        else if (title.equals(header + "13" + end))
            return 16L;
        else if (title.equals(header + "14" + end))
            return 17L;
        else if (title.equals(header + "14-1" + end))
            return 18L;
        else if (title.equals(header + "15-A" + end))
            return 19L;
        else if (title.equals(header + "15-B" + end))
            return 20L;
        else if (title.equals(header + "16" + end))
            return 21L;
        else if (title.equals(header + "17" + end))
            return 22L;
        else if (title.equals(header + "18" + end))
            return 23L;
        else if (title.equals(header + "18-1" + end))
            return 24L;
        else if (title.equals(header + "19" + end))
            return 25L;
        else if (title.equals(header + "20" + end))
            return 26L;
        else if (title.equals(header + "21" + end))
            return 27L;
        return null;
    }
}
