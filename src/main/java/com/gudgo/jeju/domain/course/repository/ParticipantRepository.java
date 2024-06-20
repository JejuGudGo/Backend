package com.gudgo.jeju.domain.course.repository;

import com.gudgo.jeju.domain.course.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    Optional<Participant> findByParticipantUserIdAndCourseId(Long userId, Long PostId);

    List<Participant> findByCourseIdAndApprovedFalseAndIsAppliedTrue(Long courseId);

    List<Participant> findByCourseIdAndApprovedTrue(Long courseId);

    Long countByCourseIdAndApprovedTrue(Long courseId);

}
