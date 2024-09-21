package com.gudgo.jeju.domain.trail.entity;

import com.gudgo.jeju.domain.planner.course.entity.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String summary;

    private String address;

    private String time;

    private String content;

    private double avgStar;

    private String tel;

    private double latitude;

    private double longitude;

    public Trail withStarAvg(double avgStar) {
        return Trail.builder()
                .id(id)
                .title(title)
                .summary(summary)
                .address(address)
                .time(time)
                .content(content)
                .avgStar(avgStar)
                .tel(tel)
                .latitude(latitude)
                .longitude(longitude)
                .build();

    }
}
