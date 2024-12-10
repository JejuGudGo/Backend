package com.example.jejugudgo.global.data.course;

import com.example.jejugudgo.domain.course.common.entity.OlleCourse;
import com.example.jejugudgo.domain.course.common.entity.OlleCourseLineData;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseLineDataRepository;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseRepository;
import com.example.jejugudgo.global.data.common.entity.DataCommandLog;
import com.example.jejugudgo.global.data.common.repository.DataCommandLogRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


@Service
@Slf4j
@RequiredArgsConstructor
public class HayongOlleGpxDataComponent {
    private final DataCommandLogRepository dataCommandLogRepository;
    private final OlleCourseRepository olleCourseRepository;
    private final OlleCourseLineDataRepository olleCourseLineDataRepository;

    public void loadHaYoungOlleSpotCsvData() throws Exception {
        DataCommandLog checkDataConfig = dataCommandLogRepository.findByConfigKey("HaYoungOlleLineData")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try  {
                for (int i = 0; i < 3; i++) {
                    String fileName = getHayoungOlleFile(i);
                    OlleCourse course = getOlleCourse(fileName);
                    CSVReader csvReader = new CSVReader(new InputStreamReader(new ClassPathResource(fileName).getInputStream()));
                    List<OlleCourseLineData> hayoungOlle = csvReader.readAll().stream()
                            .map(fields -> { // long, lat, order
                                return OlleCourseLineData.builder()
                                        .dataOrder(Long.parseLong(fields[2]))
                                        .olleCourse(course)
                                        .longitude(Double.parseDouble(fields[1]))
                                        .latitude(Double.parseDouble(fields[0]))
                                        .build();
                            })
                            .filter(Objects::nonNull)
                            .toList();

                    olleCourseLineDataRepository.saveAll(hayoungOlle);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            DataCommandLog dataCommandLog = DataCommandLog.builder()
                    .configKey("HaYoungOlleLineData")
                    .configValue(true)
                    .updatedAt(LocalDate.now())
                    .build();

            dataCommandLogRepository.save(dataCommandLog);

            log.info("===============================================================================");
            log.info("All hayoung olle course data is uploaded!");
            log.info("===============================================================================");

        } else {
            log.info("===============================================================================");
            log.info("Hayoung olle course data is already loaded");
            log.info("===============================================================================");
        }
    }

    private String getHayoungOlleFile(int id) {
        return switch (id) {
            case 0 -> "csv/course/hayoung_olle_1.csv";
            case 1 -> "csv/course/hayoung_olle_2.csv";
            case 2 -> "csv/course/hayoung_olle_3.csv";
            default -> null;
        };
    }

    private OlleCourse getOlleCourse(String fileName) {
        long courseId = switch (fileName) {
            case "csv/course/hayoung_olle_1.csv" -> 28L;
            case "csv/course/hayoung_olle_2.csv" -> 29L;
            case "csv/course/hayoung_olle_3.csv" -> 30L;
            default -> 0L;
        };

        return olleCourseRepository.findById(courseId)
                .orElseThrow(() -> new CustomException(RetCode.RET_CODE92));
    }
}