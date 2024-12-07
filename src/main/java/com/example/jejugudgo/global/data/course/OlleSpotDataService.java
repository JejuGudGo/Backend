package com.example.jejugudgo.global.data.course;

import com.example.jejugudgo.domain.course.common.entity.OlleCourse;
import com.example.jejugudgo.domain.course.common.entity.OlleSpot;
import com.example.jejugudgo.domain.course.common.repository.OlleCourseRepository;
import com.example.jejugudgo.domain.course.common.repository.OlleSpotRepository;
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
public class OlleSpotDataService {
    private final DataCommandLogRepository dataCommandLogRepository;
    private final OlleCourseRepository olleCourseRepository;
    private final OlleSpotRepository olleSpotRepository;

    public void loadOlleSpotCsvToDatabase() throws IOException, CsvException {
        DataCommandLog checkDataConfig = dataCommandLogRepository.findByConfigKey("OlleSpotData")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (CSVReader csvReader = new CSVReader(new InputStreamReader(new ClassPathResource("csv/course/olleSpot.csv").getInputStream()))) {
                List<OlleSpot> olleSpots = csvReader.readAll().stream()
                        .skip(1)
                        .map(fields -> {
                            try { // olleCourseId,olleSpotId,title,latitude,longitude,orders
                                Long olleCourseId = Long.parseLong(fields[0]);
                                OlleCourse ollecourse = olleCourseRepository.findById(olleCourseId)
                                        .orElseThrow(() -> new CustomException(RetCode.RET_CODE97));

                                return OlleSpot.builder()
                                        .olleCourse(ollecourse)
                                        .id(Long.parseLong(fields[1]))
                                        .title(fields[2])
                                        .latitude(Double.parseDouble(fields[3]))
                                        .longitude(Double.parseDouble(fields[4]))
                                        .spotOrder(Long.parseLong(fields[5]))
                                        .build();

                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .toList();

                olleSpotRepository.saveAll(olleSpots);

            } catch (Exception e) {
                throw new CustomException(RetCode.RET_CODE92);
            }

            DataCommandLog dataCommandLog = DataCommandLog.builder()
                    .configKey("OlleSpotData")
                    .configValue(true)
                    .updatedAt(LocalDate.now())
                    .build();

            dataCommandLogRepository.save(dataCommandLog);

            log.info("===============================================================================");
            log.info("All olle spot data is uploaded!");
            log.info("===============================================================================");

        } else {
            log.info("===============================================================================");
            log.info("Olle spot data is already loaded");
            log.info("===============================================================================");
        }
    }
}
