package com.gudgo.jeju.domain.planner.event;

import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.profile.entity.Profile;
import com.gudgo.jeju.domain.profile.repository.ProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class PlannerCompletedEventListener {

    private final PlannerRepository plannerRepository;
    private final ProfileRepository profileRepository;


    @EventListener
    @Transactional
    public void handlePlannerCompletedEvent(PlannerCompletedEvent event) {
        Planner planner = plannerRepository.findById(event.getPlannerId())
                .orElseThrow(EntityNotFoundException::new);

        Profile profile = profileRepository.findById(planner.getUser().getProfile().getId())
                .orElseThrow(EntityNotFoundException::new);

        // walkingTime 업데이트
        Time currentWalkingTime = profile.getWalkingTime();
        LocalTime plannerTime = planner.getTime();

        long newTimeInMillis = currentWalkingTime.getTime() +
                (plannerTime.toSecondOfDay() * 1000L);
        Time newWalkingTime = new Time(newTimeInMillis);

        // walkingCount 증가
        Long newWalkingCount = profile.getWalkingCount() + 1;

        // 새로운 Profile 객체 생성 및 저장
        Profile updatedProfile = profile.withWalkingTime(newWalkingTime)
                .withWalkingCount(newWalkingCount);

        profileRepository.save(updatedProfile);

    }
}
