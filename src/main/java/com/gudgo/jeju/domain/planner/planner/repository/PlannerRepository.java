package com.gudgo.jeju.domain.planner.planner.repository;

import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlannerRepository extends JpaRepository<Planner, Long> {

    Optional<Planner> findById(Long id);

    Optional<Planner> findByCourseId(Long courseId);

    Planner findByChatRoomId(Long chatRoomId);

    List<Long> findPlannerIdsByCourseId(Long courseId);

    List<Planner> findAllByCourseId(Long courseId);

}
