package com.gudgo.jeju.domain.planner.repository;

import com.gudgo.jeju.domain.planner.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Optional<Participant> findByParticipantUserIdAndCourseId(Long userId, Long PostId);

    List<Participant> findByCourseIdAndApprovedFalseAndIsAppliedTrue(Long courseId);

    List<Participant> findByCourseIdAndApprovedTrueAndIsDeletedFalse(Long courseId);

    Long countByCourseIdAndApprovedTrue(Long courseId);

}
