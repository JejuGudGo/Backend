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

    private Long orderNumber;

    private String address;

    private double latitude;

    private double longitude;

    private boolean isDeleted = false;

    private boolean isCompleted = false;

    private long count;

//    public void softDelete() {
//        this.isDeleted = true;
//    }
//    public void updateIsCompleted() { this.isCompleted = true; }
//
//    public void updateCount() { this.count++; }

    public Spot withDeleted() {
        return Spot.builder()
                .id(this.id)
                .course(this.course)
                .tourApiCategory1(this.tourApiCategory1)
                .title(this.title)
                .courseType(this.courseType)
                .orderNumber(this.orderNumber)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .isDeleted(true)
                .isCompleted(this.isCompleted)
                .count(this.count)
                .build();
    }

    public Spot withCompleted() {
        return Spot.builder()
                .id(this.id)
                .course(this.course)
                .tourApiCategory1(this.tourApiCategory1)
                .title(this.title)
                .courseType(this.courseType)
                .orderNumber(this.orderNumber)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .isDeleted(this.isDeleted)
                .isCompleted(true)
                .count(this.count)
                .build();
    }

    public Spot withIncreasedCount() {
        return Spot.builder()
                .id(this.id)
                .course(this.course)
                .tourApiCategory1(this.tourApiCategory1)
                .title(this.title)
                .courseType(this.courseType)
                .orderNumber(this.orderNumber)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .isDeleted(this.isDeleted)
                .isCompleted(this.isCompleted)
                .count(this.count + 1)
                .build();
    }

}
