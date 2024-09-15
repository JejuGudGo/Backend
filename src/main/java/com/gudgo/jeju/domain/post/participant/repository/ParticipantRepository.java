package com.gudgo.jeju.domain.post.participant.repository;

import com.gudgo.jeju.domain.post.participant.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    Participant findByUserIdAndPlannerId(Long userId, Long PostId);
}
