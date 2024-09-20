package com.gudgo.jeju.domain.event.service;

import com.gudgo.jeju.domain.event.dto.response.EventResponse;
import com.gudgo.jeju.domain.event.entity.Event;
import com.gudgo.jeju.domain.event.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
                event.getPeriod(),
                event.getImageUrl(),
                event.getInformationUrl(),
                event.getType()
        );

    }
}
