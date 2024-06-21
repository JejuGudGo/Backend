package com.gudgo.jeju.domain.course.repository;

import com.gudgo.jeju.domain.course.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
