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


    @Setter
    private String title;

    @Setter
    @Enumerated(value = EnumType.STRING)
    private CourseType courseType;

    @Setter
    private Long orderNumber;

    @Setter
    private String address;

    @Setter
    private double latitude;

    @Setter
    private double longitude;

    @Setter
    private boolean isDeleted;

    @Setter
    private boolean isCompleted;

    @Setter
    private long count;



}
