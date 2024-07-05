package com.gudgo.jeju.global.data.olle.service;

import com.gudgo.jeju.domain.olle.entity.JeJuOlleSpot;
import com.gudgo.jeju.domain.olle.entity.JeJuOlleCourse;
import com.gudgo.jeju.domain.olle.entity.OlleType;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleCourseRepository;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleSpotRepository;
import com.gudgo.jeju.global.data.common.entity.DataConfiguration;
import com.gudgo.jeju.global.data.tourAPI.repository.DataConfigurationRepository;
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
public class JejuOlleSpotDatabaseService {
    private final DataConfigurationRepository dataConfigurationRepository;
    private final JeJuOlleCourseRepository jeJuOlleCourseRepository;
    private final JeJuOlleSpotRepository jeJuOlleSpotRepository;

    public void loadJeJuOlleSpotCsvData() throws Exception {
        DataConfiguration checkDataConfig = dataConfigurationRepository.findByConfigKey("JeJuOlleSpot")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("csv/olle_spot.csv").getInputStream()))) {
                String line;

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    String courseNumber = data[0];
                    String title = data[1];
                    String latitude = data[2];
                    String longitude = data[3];
                    String distance = data[4];
                    String order = data[5];

                    JeJuOlleCourse jeJuOlleCourse = jeJuOlleCourseRepository.findByOlleTypeAndCourseNumberAndWheelchairAccessible(OlleType.JEJU, courseNumber, false);

                    JeJuOlleSpot jeJuOlleSpot = JeJuOlleSpot.builder()
                            .jeJuOlleCourse(jeJuOlleCourse)
                            .title(title)
                            .latitude(Double.parseDouble(latitude))
                            .longitude(Double.parseDouble(longitude))
                            .distance(distance + "km")
                            .orderNumber(Long.parseLong(order))
                            .build();

                    jeJuOlleSpotRepository.save(jeJuOlleSpot);
                }
            }

            if (checkDataConfig == null) {
                DataConfiguration dataConfiguration = DataConfiguration.builder()
                        .configKey("JeJuOlleSpot")
                        .configValue(true)
                        .updatedAt(LocalDate.now())
                        .build();

                dataConfigurationRepository.save(dataConfiguration);

            } else if (!checkDataConfig.isConfigValue()){
                checkDataConfig = checkDataConfig.withConfigValue(true);
                dataConfigurationRepository.save(checkDataConfig);
            }

        } else {
            log.info("===============================================================================");
            log.info("JeJuOlle spot is already loaded");
            log.info("===============================================================================");
        }
    }
}