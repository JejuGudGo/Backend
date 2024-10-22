package com.example.jejugudgo.domain.event.service;

import com.example.jejugudgo.domain.event.dto.EventListResponse;
import com.example.jejugudgo.domain.event.entity.EventStatus;
import com.example.jejugudgo.domain.event.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;

    public List<EventListResponse> getEvents(String query) {
        if (query.equals("전체")) {
            return eventRepository.findAll().stream()
                    .map(event -> new EventListResponse(
                            event.getId(),
                            event.getTitle(),
                            event.getEventStatus().getCode(),
                            event.getThumbnail(),
                            event.getLink()
                    ))
                    .collect(Collectors.toList());
        } else {
            EventStatus eventStatus = EventStatus.fromCode(query);
            return eventRepository.findByEventStatus(eventStatus).stream()
                    .map(event -> new EventListResponse(
                            event.getId(),
                            event.getTitle(),
                            event.getEventStatus().getCode(),
                            event.getThumbnail(),
                            event.getLink()
                    ))
                    .collect(Collectors.toList());
        }
    }
}
