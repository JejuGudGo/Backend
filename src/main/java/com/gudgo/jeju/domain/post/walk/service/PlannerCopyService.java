package com.gudgo.jeju.domain.post.walk.service;

import com.gudgo.jeju.domain.olle.entity.JeJuOlleSpot;
import com.gudgo.jeju.domain.olle.entity.OlleType;
import com.gudgo.jeju.domain.olle.repository.JeJuOlleSpotRepository;
import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.planner.course.entity.CourseType;
import com.gudgo.jeju.domain.planner.course.repository.CourseRepository;
import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import com.gudgo.jeju.domain.planner.planner.repository.PlannerRepository;
import com.gudgo.jeju.domain.planner.spot.entity.Spot;
import com.gudgo.jeju.domain.planner.spot.entity.SpotType;
import com.gudgo.jeju.domain.planner.spot.repository.SpotRepository;
import com.gudgo.jeju.domain.post.chat.entity.ChatRoom;
import com.gudgo.jeju.domain.post.walk.dto.response.CoursePostSpotResponse;
import com.gudgo.jeju.domain.user.entity.User;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlannerCopyService {
    private final PlannerRepository plannerRepository;
    private final CourseRepository courseRepository;
    private final SpotRepository spotRepository;
    private final JeJuOlleSpotRepository juOlleSpotRepository;

    @Transactional
    public Planner copyPlanner(ChatRoom chatRoom, User user, Long selectedPlannerId) {
        Planner selectedPlanner = plannerRepository.findById(selectedPlannerId)
                .orElseThrow(EntityExistsException::new);

        Course course = new Course();

        if (selectedPlanner.getCourse().getType().equals(CourseType.USER)) {
            course = copyUserCourse(selectedPlanner.getCourse());
            copyUserCourseSpot(course);

        } else {
            course = copyJejuOlleCourse(selectedPlanner.getCourse());
            copyOlleCourseSpot(course);
        }

        Planner planner = Planner.builder()
                .user(user)
                .chatRoom(chatRoom)
                .course(course)
                .time(selectedPlanner.getTime())
                .isDeleted(false)
                .isPrivate(true)
                .isCompleted(false)
                .build();

        plannerRepository.save(planner);
        return planner;
    }

    @Transactional
    public Course copyUserCourse(Course selectedCourse) {
        Course course = Course.builder()
                .type(selectedCourse.getType())
                .title(selectedCourse.getTitle()) // TODO: 아이디어 받기
                .createdAt(LocalDate.now())
                .originalCreatorId(selectedCourse.getOriginalCreatorId())
                .originalCourseId(selectedCourse.getOriginalCourseId())
                .build();

        courseRepository.save(course);
        return course;
    }

    @Transactional
    public Course copyJejuOlleCourse(Course selectedCourse) {
        Course course = Course.builder()
                .type(selectedCourse.getType())
                .title(selectedCourse.getTitle())
                .createdAt(LocalDate.now())
                .olleCourseId(selectedCourse.getOlleCourseId())
                .imageUrl(selectedCourse.getImageUrl())
                .totalDistance(selectedCourse.getTotalDistance())
                .content(selectedCourse.getContent())
                .build();

        courseRepository.save(course);
        return course;
    }

    @Transactional
    public void copyUserCourseSpot(Course course) {
        List<Spot> selectedSpots = spotRepository.findByCourseOrderByOrderNumberAsc(course);

        for (Spot selectedSpot : selectedSpots) {

            selectedSpot = selectedSpot.withIncreasedCount();

            spotRepository.save(selectedSpot);

            Spot spot = Spot.builder()
                    .spotType(selectedSpot.getSpotType())
                    .orderNumber(selectedSpot.getOrderNumber())
                    .title(selectedSpot.getTitle())
                    .address(selectedSpot.getAddress())
                    .latitude(selectedSpot.getLatitude())
                    .longitude(selectedSpot.getLongitude())
                    .isDeleted(false)
                    .isCompleted(false)
                    .contentId(selectedSpot.getContentId())
                    .build();

            spotRepository.save(spot);
        }

        String startSpotTitle = selectedSpots.get(0).getTitle();
        String lastSpotTitle = selectedSpots.get(selectedSpots.size() - 1).getTitle();
        String courseContent = startSpotTitle + "부터 " + lastSpotTitle  + "까지";

        course = course.withContent(courseContent);
        courseRepository.save(course);
    }

    @Transactional
    public void copyOlleCourseSpot(Course course) {
        List<JeJuOlleSpot> selectedSpots = juOlleSpotRepository.findAllByJeJuOlleCourseId(course.getOlleCourseId());
        SpotType spotType = SpotType.JEJU;

        for (JeJuOlleSpot selectedSpot : selectedSpots) {
            if (selectedSpot.getJeJuOlleCourse().getOlleType().equals(OlleType.JEJU)) spotType = SpotType.JEJU;
            else spotType = SpotType.HAYOUNG;

            Spot spot = Spot.builder()
                    .spotType(spotType)
                    .orderNumber(selectedSpot.getOrderNumber())
                    .distance(selectedSpot.getDistance())
                    .title(selectedSpot.getTitle())
                    .latitude(selectedSpot.getLatitude())
                    .longitude(selectedSpot.getLongitude())
                    .isDeleted(false)
                    .isCompleted(false)
                    .build();

            spotRepository.save(spot);
        }

        String startSpotTitle = selectedSpots.get(0).getTitle();
        String lastSpotTitle = selectedSpots.get(selectedSpots.size() - 1).getTitle();
        String courseContent = startSpotTitle + "부터 " + lastSpotTitle  + "까지 걷는 코스에요.";

        course = course.withContent(courseContent);
        courseRepository.save(course);
    }
}
