package com.example.jejugudgo.domain.course.jejugudgo.service;

import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourseSpot;
import com.example.jejugudgo.domain.course.jejugudgo.repository.JejuGudgoCourseSpotRepository;
import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourseSpot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JejuGudgoCourseSpotService {
    private final JejuGudgoCourseSpotRepository jejuGudgoCourseSpotRepository;

    public List<JejuGudgoCourseSpot> createJejuGudgoCourseSpotFromUser(JejuGudgoCourse course, List<UserJejuGudgoCourseSpot> userSpots) {
        List<JejuGudgoCourseSpot> courseSpots = new ArrayList<>();

        for (UserJejuGudgoCourseSpot spot : userSpots) {
            JejuGudgoCourseSpot jejuGudgoCourseSpot = JejuGudgoCourseSpot.builder()
                    .jejuGudgoCourse(course)
                    .title(spot.getTitle())
                    .spotType(spot.getSpotType())
                    .orderNumber(spot.getOrderNumber())
                    .address(spot.getAddress())
                    .latitude(spot.getLatitude())
                    .longitude(spot.getLongitude())
                    .build();

            jejuGudgoCourseSpotRepository.save(jejuGudgoCourseSpot);
            courseSpots.add(jejuGudgoCourseSpot);
        }

        return courseSpots;
    }
}
