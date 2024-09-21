package com.gudgo.jeju.domain.event.scheduler;

import com.gudgo.jeju.domain.event.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventTypeScheduler {

    private final EventService eventService;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void updateEventTypes() {
        log.info("Starting daily event type update");
        eventService.updateAllEventTypes();
        log.info("Completed daily event type update");
    }
}