package com.gudgo.jeju.domain.event.service;

import com.gudgo.jeju.domain.event.dto.response.EventResponse;
import com.gudgo.jeju.domain.event.entity.Event;
import com.gudgo.jeju.domain.event.entity.EventType;
import com.gudgo.jeju.domain.event.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventService {
    private final EventRepository eventRepository;

    public EventResponse getEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(EntityNotFoundException::new);

        return new EventResponse(
                eventId,
                event.getTitle(),
                event.getOrganization(),
                event.getStartDate(),
                event.getFinishDate(),
                event.getImageUrl(),
                event.getInformationUrl(),
                event.getType()
        );

    }

    @Transactional
    public void updateAllEventTypes() {
        List<Event> events = eventRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for (Event event : events) {
            EventType newType;
            if (currentDate.isAfter(event.getFinishDate())) {
                newType = EventType.END;
            } else if (currentDate.isBefore(event.getStartDate())) {
                newType = EventType.SCHEDULED;
            } else {
                newType = EventType.PROGRESS;
            }

            if (event.getType() != newType) {
                Event updatedEvent = event.withType(newType);
                eventRepository.save(updatedEvent);
                log.info("Updated event {} type to {}", event.getId(), newType);
            }
        }
    }
}
