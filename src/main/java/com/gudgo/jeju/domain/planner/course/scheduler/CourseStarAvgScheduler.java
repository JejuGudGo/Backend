package com.gudgo.jeju.domain.planner.course.scheduler;


import com.gudgo.jeju.domain.planner.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class CourseStarAvgScheduler {
    private final CourseService courseService;

    @Scheduled(cron = "0 0 0 * * ?")  // 매일 자정에 실행
    public void updateCourseStarAvg() {
        log.info("Updating original course star averages...");
        courseService.updateAllOriginalCourseStarAvg();
        log.info("Original course star averages updated successfully.");
    }
}
