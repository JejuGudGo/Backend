package com.gudgo.jeju.domain.trail.scheduler;

import com.gudgo.jeju.domain.planner.course.service.CourseService;
import com.gudgo.jeju.domain.trail.service.TrailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TrailStarAvgScheduler {
    private final TrailService trailService;

    @Scheduled(cron = "0 0 0 * * ?")  // 매일 자정에 실행
    public void updateCourseStarAvg() {
        log.info("Updating original course star averages...");
        trailService.updateAllOriginalTrailStarAvg();
        log.info("Original trail star averages updated successfully.");
    }
}
