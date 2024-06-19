package com.gudgo.jeju.global.data.olle.service;

import com.gudgo.jeju.global.data.olle.entity.HaYoungOlleSpot;
import com.gudgo.jeju.global.data.olle.entity.JeJuOlleCourse;
import com.gudgo.jeju.global.data.olle.repository.HaYoungOlleSpotRepository;
import com.gudgo.jeju.global.data.olle.repository.JeJuOlleCourseRepository;
import com.gudgo.jeju.global.data.tourAPI.common.entity.DataConfiguration;
import com.gudgo.jeju.global.data.tourAPI.common.repository.DataConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class HaYoungOlleSpotDatabaseService {
    private final DataConfigurationRepository dataConfigurationRepository;
    private final JeJuOlleCourseRepository jeJuOlleCourseRepository;
    private final HaYoungOlleSpotRepository haYoungOlleSpotRepository;

    public void loadHaYoungOlleSpotCsvData() throws Exception {
        DataConfiguration checkDataConfig = dataConfigurationRepository.findByConfigKey("HaYoungOlleSpot")
                .orElse(null);

        JeJuOlleCourse jeJuOlleCourse1 = jeJuOlleCourseRepository.findByTitle("하영 올레 1 코스");
        JeJuOlleCourse jeJuOlleCourse2 = jeJuOlleCourseRepository.findByTitle("하영 올레 2 코스");
        JeJuOlleCourse jeJuOlleCourse3 = jeJuOlleCourseRepository.findByTitle("하영 올레 3 코스");

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("csv/hayoung_olle_spot.csv").getInputStream()))) {
                String line;

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    String courseNumber = data[0];
                    String title = data[1];
                    String latitude = data[2];
                    String longitude = data[3];

                    JeJuOlleCourse jeJuOlleCourse = new JeJuOlleCourse();

                    if (courseNumber.equals("1")) {
                        jeJuOlleCourse = jeJuOlleCourse1;

                    } else if (courseNumber.equals("2")) {
                        jeJuOlleCourse = jeJuOlleCourse2;

                    } else if (courseNumber.equals("3")) {
                        jeJuOlleCourse = jeJuOlleCourse3;
                    }

                    HaYoungOlleSpot haYoungOlleSpot = HaYoungOlleSpot.builder()
                            .jeJuOlleCourse(jeJuOlleCourse)
                            .title(title)
                            .longitude(Double.parseDouble(longitude))
                            .latitude(Double.parseDouble(latitude))
                            .build();

                    haYoungOlleSpotRepository.save(haYoungOlleSpot);
                }
            }

            if (checkDataConfig == null) {
                DataConfiguration dataConfiguration = DataConfiguration.builder()
                        .configKey("HaYoungOlleSpot")
                        .configValue(true)
                        .updatedAt(LocalDate.now())
                        .build();

                dataConfigurationRepository.save(dataConfiguration);

            } else if (!checkDataConfig.isConfigValue()){
                checkDataConfig.withConfigValue(true);
                dataConfigurationRepository.save(checkDataConfig);
            }

        } else {
            log.info("===============================================================================");
            log.info("HaYoungOlle spot is already loaded");
            log.info("===============================================================================");
        }
    }
}