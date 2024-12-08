package com.example.jejugudgo.global.data.course;

import com.example.jejugudgo.domain.course.common.entity.OlleCourse;
import com.example.jejugudgo.domain.course.common.enums.OlleType;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseRepository;
import com.example.jejugudgo.global.data.common.entity.DataCommandLog;
import com.example.jejugudgo.global.data.common.repository.DataCommandLogRepository;
import com.example.jejugudgo.global.exception.enums.RetCode;
import com.example.jejugudgo.global.exception.exception.CustomException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Component
@RequiredArgsConstructor
public class OlleCourseDataService {
    private final DataCommandLogRepository dataCommandLogRepository;
    private final OlleCourseRepository olleCourseRepository;

    public void loadOlleCourseCsvToDatabase() throws IOException, CsvException {
        DataCommandLog checkDataConfig = dataCommandLogRepository.findByConfigKey("OlleCourseData")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (CSVReader csvReader = new CSVReader(new InputStreamReader(new ClassPathResource("csv/course/olleCourse.csv").getInputStream()))) {
                List<OlleCourse> olleCourses = csvReader.readAll().stream()
                        .skip(1)
                        .map(fields -> {
                            try {
                                return OlleCourse.builder()
                                        .id(Long.parseLong(fields[0]))
                                        .olleType(OlleType.fromType(fields[2]))
                                        .title(fields[1])
                                        .distance(fields[3] != null && !fields[3].isBlank() ? fields[3] : null)
                                        .time(fields[4] != null && !fields[4].isBlank() ? fields[4] : null)
                                        .summary(fields[5] != null && !fields[5].isBlank() ? fields[5] : null)
                                        .content(fields[6] != null && !fields[6].isBlank() ? fields[6] : null)
                                        .latitude(fields[7] != null && !fields[7].isBlank() ? Double.parseDouble(fields[7]) : null)
                                        .longitude(fields[8] != null && !fields[8].isBlank() ? Double.parseDouble(fields[8]) : null)
                                        .address(fields[9] != null && !fields[9].isBlank() ? fields[9] : null)
                                        .openTime(fields[10] != null && !fields[10].isBlank() ? fields[10] : null)
                                        .tel(fields[11] != null && !fields[11].isBlank() ? fields[11] : null)
                                        .build();
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .toList();

                olleCourseRepository.saveAll(olleCourses);

            } catch (IOException | CsvException e) {
                throw new CustomException(RetCode.RET_CODE92);
            }

            DataCommandLog dataCommandLog = DataCommandLog.builder()
                    .configKey("OlleCourseData")
                    .configValue(true)
                    .updatedAt(LocalDate.now())
                    .build();

            dataCommandLogRepository.save(dataCommandLog);

            log.info("===============================================================================");
            log.info("All olle course data is uploaded!");
            log.info("===============================================================================");

        } else {
            log.info("===============================================================================");
            log.info("Olle course data is already loaded");
            log.info("===============================================================================");
        }
    }
}
