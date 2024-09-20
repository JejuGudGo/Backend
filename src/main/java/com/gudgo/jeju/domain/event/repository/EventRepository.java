package com.gudgo.jeju.domain.event.repository;

import com.gudgo.jeju.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
