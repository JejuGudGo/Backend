package com.example.jejugudgo.global.data.course;

import com.example.jejugudgo.domain.course.common.entity.OlleCourse;
import com.example.jejugudgo.domain.course.common.entity.OlleCourseTag;
import com.example.jejugudgo.domain.course.common.enums.OlleTag;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseRepository;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseTagRepository;
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
import java.util.ArrayList;
import java.util.List;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Component
@RequiredArgsConstructor
public class OlleCourseTagDataService {
    private final DataCommandLogRepository dataCommandLogRepository;
    private final OlleCourseRepository olleCourseRepository;
    private final OlleCourseTagRepository olleCourseTagRepository;

    public void loadOlleTagCsvToDatabase() throws IOException, CsvException {
        DataCommandLog checkDataConfig = dataCommandLogRepository.findByConfigKey("OlleTagData")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (CSVReader csvReader = new CSVReader(new InputStreamReader(new ClassPathResource("csv/course/olleTag.csv").getInputStream()))) {
                List<String[]> rows = csvReader.readAll();

                long courseId = 1L;

                for (String[] fields : rows) {
                    try {
                        OlleCourse olleCourse = olleCourseRepository.findById(courseId)
                                .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

                        List<OlleCourseTag> olleCourseTags = new ArrayList<>();

                        for (String value : fields) {
                            if (value != null && !value.isBlank()) {
                                OlleCourseTag olleCourseTag = OlleCourseTag.builder()
                                        .olleCourse(olleCourse)
                                        .title(OlleTag.fromInput(value.trim()))
                                        .build();
                                olleCourseTags.add(olleCourseTag);
                            }
                        }

                        olleCourseTagRepository.saveAll(olleCourseTags);

                        courseId++; // Move to the next course for the next row

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            DataCommandLog dataCommandLog = DataCommandLog.builder()
                    .configKey("OlleTagData")
                    .configValue(true)
                    .updatedAt(LocalDate.now())
                    .build();

            dataCommandLogRepository.save(dataCommandLog);

            log.info("===============================================================================");
            log.info("All olle tag data is uploaded!");
            log.info("===============================================================================");

        } else {
            log.info("===============================================================================");
            log.info("Olle tag data is already loaded");
            log.info("===============================================================================");
        }
    }
}