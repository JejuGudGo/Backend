package com.gudgo.jeju.global.data.olle.service;

import com.gudgo.jeju.domain.olle.entity.OlleType;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleCourseRepository;
import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import com.gudgo.jeju.domain.planner.course.repository.CourseRepository;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.global.data.common.entity.DataConfiguration;
import com.gudgo.jeju.global.data.tourAPI.repository.DataConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;
import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class OlleDataInitializationService {

    private final JejuOlleDatabaseService jejuOlleDatabaseService;
    private final HaYoungOlleDatabaseService haYoungOlleDatabaseService;
    private final CourseRepository courseRepository;
    private final PlannerRepository plannerRepository;
    private final JeJuOlleCourseRepository jeJuOlleCourseRepository;
    private final DataConfigurationRepository dataConfigurationRepository;


    @PostConstruct
    @Transactional
    public void initializeOlleData() {
        log.info("Starting Olle data initialization check...");
        try {
            if (isInitializationRequired()) {
                log.info("Initialization is required.");
                initializeJejuOlleData();
                initializeHaYoungOlleData();
                log.info("Olle data initialization completed successfully.");
            } else {
                log.info("Initialization not required. Checking for updates...");
                updateCoursesAndPlanners();
            }
        } catch (Exception e) {
            log.error("Error occurred during Olle data initialization", e);
        }
        log.info("Olle data initialization process completed.");
    }

    private boolean isInitializationRequired() {
        long jejuOlleCourseCount = jeJuOlleCourseRepository.countByOlleType(OlleType.JEJU);
        long haYoungOlleCourseCount = jeJuOlleCourseRepository.countByOlleType(OlleType.HAYOUNG);
        long courseCount = courseRepository.count();
        long plannerCount = plannerRepository.count();

        log.info("JeJu Olle course count: {}, HaYoung Olle course count: {}, Course count: {}, Planner count: {}",
                jejuOlleCourseCount, haYoungOlleCourseCount, courseCount, plannerCount);

        return (jejuOlleCourseCount == 0 || haYoungOlleCourseCount == 0 || courseCount == 0 || plannerCount == 0);
    }

    private void initializeJejuOlleData() {
        if (!isJejuOlleDataExist()) {
            log.info("Initializing Jeju Olle data...");
            jejuOlleDatabaseService.processJejuOlleData();
        } else {
            log.info("Jeju Olle data already exists. Updating courses and planners...");
            jejuOlleDatabaseService.createCoursesAndPlannersFromJejuOlleData();
        }
    }

    private void initializeHaYoungOlleData() {
        if (!isHaYoungOlleDataExist()) {
            log.info("Initializing HaYoung Olle data...");
            haYoungOlleDatabaseService.processHaYoungOlleData();
        } else {
            log.info("HaYoung Olle data already exists. Updating courses and planners...");
            haYoungOlleDatabaseService.createCoursesAndPlannersFromHaYoungOlleData();
        }
    }

    private void updateCoursesAndPlanners() {
        log.info("Updating courses and planners for existing Olle data...");
        jejuOlleDatabaseService.createCoursesAndPlannersFromJejuOlleData();
        haYoungOlleDatabaseService.createCoursesAndPlannersFromHaYoungOlleData();
    }

    private boolean isJejuOlleDataExist() {
        long count = jeJuOlleCourseRepository.countByOlleType(OlleType.JEJU);
        log.info("Found {} Jeju Olle courses", count);
        return count > 0;
    }

    private boolean isHaYoungOlleDataExist() {
        long count = jeJuOlleCourseRepository.countByOlleType(OlleType.HAYOUNG);
        log.info("Found {} HaYoung Olle courses", count);
        return count > 0;
    }
}