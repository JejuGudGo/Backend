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
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class HaYoungOlleDatabaseService {
    private final DataConfigurationRepository dataConfigurationRepository;
    private final JeJuOlleCourseRepository jeJuOlleCourseRepository;
    private final JeJuOlleCourseDataRepository jeJuOlleCourseDataRepository;

    public void loadHaYoungData() throws Exception {
        loadHaYoungOlle1Data();
        loadHaYoungOlle2Data();
        loadHaYoungOlle3Data();
    }

    private void loadHaYoungOlle1Data() throws Exception {
        DataConfiguration checkDataConfig = dataConfigurationRepository.findByConfigKey("HaYoungOlle1")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("csv/hayoung_olle_1.csv").getInputStream()))) {
                String line;

                JeJuOlleCourse jeJuOlleCourse = JeJuOlleCourse.builder()
                        .olleType(OlleType.HAYOUNG)
                        .courseNumber("1")
                        .title("하영 올레 1 코스")
                        .startLatitude(Double.parseDouble("33.2403833"))
                        .startLongitude(Double.parseDouble("126.5588624"))
                        .endLatitude(Double.parseDouble("33.2531475"))
                        .endLongitude(Double.parseDouble("126.5609703"))
                        .wheelchairAccessible(false)
                        .build();

                jeJuOlleCourseRepository.save(jeJuOlleCourse);

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    String longitude = data[0];
                    String latitude = data[1];
                    String orderNumber = data[2];

                    JeJuOlleCourseData jeJuOlleCourseData = JeJuOlleCourseData.builder()
                            .jeJuOlleCourse(jeJuOlleCourse)
                            .orderNumber(Long.parseLong(orderNumber))
                            .latitude(Double.parseDouble(latitude))
                            .longitude(Double.parseDouble(longitude))
                            .altitude(0.0)
                            .updatedAt(LocalDate.now())
                            .time(OffsetDateTime.now())
                            .build();

                    jeJuOlleCourseDataRepository.save(jeJuOlleCourseData);
                }
            }

            if (checkDataConfig == null) {
                DataConfiguration dataConfiguration = DataConfiguration.builder()
                        .configKey("HaYoungOlle1")
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
            log.info("HaYoungOlle1 is already loaded");
            log.info("===============================================================================");
        }
    }

    private void loadHaYoungOlle2Data() throws Exception {
        DataConfiguration checkDataConfig = dataConfigurationRepository.findByConfigKey("HaYoungOlle2")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("csv/hayoung_olle_2.csv").getInputStream()))) {
                String line;

                JeJuOlleCourse jeJuOlleCourse = JeJuOlleCourse.builder()
                        .olleType(OlleType.HAYOUNG)
                        .courseNumber("2")
                        .title("하영 올레 2 코스")
                        .startLatitude(Double.parseDouble("33.2544083"))
                        .startLongitude(Double.parseDouble("126.5607624"))
                        .endLatitude(Double.parseDouble("33.2527143"))
                        .endLongitude(Double.parseDouble("126.5612169"))
                        .wheelchairAccessible(false)
                        .build();

                jeJuOlleCourseRepository.save(jeJuOlleCourse);

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    String longitude = data[0];
                    String latitude = data[1];
                    String orderNumber = data[2];

                    JeJuOlleCourseData jeJuOlleCourseData = JeJuOlleCourseData.builder()
                            .jeJuOlleCourse(jeJuOlleCourse)
                            .orderNumber(Long.parseLong(orderNumber))
                            .latitude(Double.parseDouble(latitude))
                            .longitude(Double.parseDouble(longitude))
                            .altitude(0.0)
                            .updatedAt(LocalDate.now())
                            .time(OffsetDateTime.now())
                            .build();

                    jeJuOlleCourseDataRepository.save(jeJuOlleCourseData);
                }
            }

            if (checkDataConfig == null) {
                DataConfiguration dataConfiguration = DataConfiguration.builder()
                        .configKey("HaYoungOlle2")
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
            log.info("HaYoungOlle2 is already loaded");
            log.info("===============================================================================");
        }
    }

    private void loadHaYoungOlle3Data() throws Exception {
        DataConfiguration checkDataConfig = dataConfigurationRepository.findByConfigKey("HaYoungOlle3")
                .orElse(null);

        if (checkDataConfig == null || !checkDataConfig.isConfigValue()) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("csv/hayoung_olle_2.csv").getInputStream()))) {
                String line;

                JeJuOlleCourse jeJuOlleCourse = JeJuOlleCourse.builder()
                        .olleType(OlleType.HAYOUNG)
                        .courseNumber("3")
                        .title("하영 올레 3 코스")
                        .startLatitude(Double.parseDouble("33.2647366"))
                        .startLongitude(Double.parseDouble("126.5531553"))
                        .endLatitude(Double.parseDouble("33.2647366"))
                        .endLongitude(Double.parseDouble("126.5531553"))
                        .wheelchairAccessible(false)
                        .build();

                jeJuOlleCourseRepository.save(jeJuOlleCourse);

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    String longitude = data[0];
                    String latitude = data[1];
                    String orderNumber = data[2];

                    JeJuOlleCourseData jeJuOlleCourseData = JeJuOlleCourseData.builder()
                            .jeJuOlleCourse(jeJuOlleCourse)
                            .orderNumber(Long.parseLong(orderNumber))
                            .latitude(Double.parseDouble(latitude))
                            .longitude(Double.parseDouble(longitude))
                            .altitude(0.0)
                            .updatedAt(LocalDate.now())
                            .time(OffsetDateTime.now())
                            .build();

                    jeJuOlleCourseDataRepository.save(jeJuOlleCourseData);
                }
            }

            if (checkDataConfig == null) {
                DataConfiguration dataConfiguration = DataConfiguration.builder()
                        .configKey("HaYoungOlle3")
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
            log.info("HaYoungOlle3 is already loaded");
            log.info("===============================================================================");
        }
    }
}
