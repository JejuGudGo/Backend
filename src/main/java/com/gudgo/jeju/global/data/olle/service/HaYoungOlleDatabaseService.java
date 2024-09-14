package com.gudgo.jeju.global.data.olle.service;

import com.gudgo.jeju.domain.olle.entity.JeJuOlleCourse;
import com.gudgo.jeju.domain.olle.entity.JeJuOlleCourseData;
import com.gudgo.jeju.domain.olle.entity.OlleType;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleCourseDataRepository;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleCourseRepository;
import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import com.gudgo.jeju.domain.planner.course.repository.CourseRepository;
import com.gudgo.jeju.domain.planner.planner.service.PlannerService;
import com.gudgo.jeju.global.data.common.entity.DataConfiguration;
import com.gudgo.jeju.global.data.tourAPI.repository.DataConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class HaYoungOlleDatabaseService {
    private final DataConfigurationRepository dataConfigurationRepository;
    private final JeJuOlleCourseRepository jeJuOlleCourseRepository;
    private final JeJuOlleCourseDataRepository jeJuOlleCourseDataRepository;
    private final CourseRepository courseRepository;

    private final PlannerService plannerService;


    @Transactional
    public void processHaYoungOlleData() {
        try {
            log.info("Starting processHaYoungOlleData");
            if (isInitializationRequired()) {
                log.info("Creating Courses and Planners from existing HaYoung Olle data");
                createCoursesAndPlannersFromHaYoungOlleData();
                markInitializationComplete();
            } else {
                log.info("Initialization already completed, skipping process");
            }
            log.info("Finished processHaYoungOlleData");
        } catch (Exception e) {
            log.error("Unexpected error in processHaYoungOlleData", e);
            throw new RuntimeException("Failed to process HaYoung Olle data", e);
        }
    }

    private boolean isInitializationRequired() {
        DataConfiguration initConfig = dataConfigurationRepository.findByConfigKey("HaYoungOlleCoursePlannerInitializationCompleted")
                .orElse(null);
        return initConfig == null || !initConfig.isConfigValue();
    }

    private void markInitializationComplete() {
        DataConfiguration initConfig = dataConfigurationRepository.findByConfigKey("HaYoungOlleCoursePlannerInitializationCompleted")
                .orElse(DataConfiguration.builder()
                        .configKey("HaYoungOlleCoursePlannerInitializationCompleted")
                        .configValue(false)
                        .updatedAt(LocalDate.now())
                        .build());

        initConfig = initConfig.withConfigValue(true)
                .withUpdatedAt(LocalDate.now());

        dataConfigurationRepository.save(initConfig);
        log.info("Marked HaYoung Olle Course and Planner initialization as completed");
    }

    public void createCoursesAndPlannersFromHaYoungOlleData() {
        log.info("Starting createCoursesAndPlannersFromHaYoungOlleData");
        try {
            List<JeJuOlleCourse> olleCourses = jeJuOlleCourseRepository.findByOlleType(OlleType.HAYOUNG);
            log.info("Found {} HaYoung Olle courses", olleCourses.size());
            for (JeJuOlleCourse olleCourse : olleCourses) {
                if (!isOlleCourseAlreadyProcessed(olleCourse)) {
                    plannerService.createOllePlanner(olleCourse);
                    log.info("Created Course and Planner for HaYoung Olle course: {}", olleCourse.getCourseNumber());
                } else {
                    log.info("Course and Planner already exist for HaYoung Olle course: {}", olleCourse.getCourseNumber());
                }
            }
        } catch (Exception e) {
            log.error("Error occurred while creating Courses and Planners from HaYoung Olle data", e);
            throw new RuntimeException("Failed to create Courses and Planners from HaYoung Olle data", e);
        }
    }

    private boolean isOlleCourseAlreadyProcessed(JeJuOlleCourse olleCourse) {
        return courseRepository.existsByTypeAndOlleCourseId(CourseType.HAYOUNG, olleCourse.getId());
    }

    public boolean loadHaYoungData() {
        try {
            loadHaYoungOlle1Data();
            loadHaYoungOlle2Data();
            loadHaYoungOlle3Data();
            return true;
        } catch (Exception e) {
            log.error("Error occurred while loading HaYoung Olle data", e);
            return false;
        }
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
                        .totalTime("약 3시간30분")
                        .totalDistance("8.9km")
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
                        .totalTime("약 3시간")
                        .totalDistance("6.4km")
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
                checkDataConfig = checkDataConfig.withConfigValue(true);
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
                        .totalTime("약 2시간30분")
                        .totalDistance("7.5km")
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