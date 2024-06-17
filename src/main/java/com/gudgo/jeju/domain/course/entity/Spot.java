package com.gudgo.jeju.domain.course.entity;

import com.gudgo.jeju.global.data.tourAPI.common.entity.TourApiCategory1;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "categoryName")
    private TourApiCategory1 tourApiCategory1;


    private String title;

    @Enumerated(value = EnumType.STRING)
    private CourseType courseType;

    private Long order;

    private String address;

    private double latitude;

    private double longitude;

    private boolean isDeleted = false;

    private boolean isCompleted = false;

    private long count;



}
