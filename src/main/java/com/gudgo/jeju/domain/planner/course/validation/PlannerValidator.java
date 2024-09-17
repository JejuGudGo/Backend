package com.gudgo.jeju.domain.planner.course.validation;

import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlannerValidator {
    private final PlannerRepository plannerRepository;

    public Planner validatePlanner(Long plannerId, Long userId) {
        Planner planner = plannerRepository.findById(plannerId)
                .orElseThrow(EntityNotFoundException::new);

        if (planner.getCourse().getOriginalCreatorId() != userId) { // 원작자가 아닌경우
            planner = plannerRepository.findByCourseId(planner.getCourse().getId()).get();
            return planner;
        }

        return planner;
    }
}
