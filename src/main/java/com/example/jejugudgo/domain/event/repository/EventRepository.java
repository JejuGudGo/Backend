package com.example.jejugudgo.domain.event.repository;

import com.example.jejugudgo.domain.event.entity.Event;
import com.example.jejugudgo.domain.event.entity.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByEventStatus(EventStatus eventStatus);
}
