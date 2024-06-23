package com.gudgo.jeju.domain.course.entity;

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

    @Enumerated(value = EnumType.STRING)
    private SpotType courseType;

    private Long orderNumber;

    private String title;

    private String address;

    private double latitude;

    private double longitude;

    private boolean isDeleted = false;

    private boolean isCompleted = false;

    private Long count;

    private String contentId = "None";


    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

//    public void softDelete() {
//        this.isDeleted = true;
//    }
//    public void updateIsCompleted() { this.isCompleted = true; }
//
//    public void updateCount() { this.count++; }

    public Spot withOrderNumber(Long orderNumber) {
        return Spot.builder()
                .id(this.id)
                .course(this.course)
                .title(this.title)
                .courseType(this.courseType)
                .orderNumber(orderNumber)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .isDeleted(true)
                .isCompleted(this.isCompleted)
                .count(this.count)
                .contentId(this.contentId)
                .build();
    }

    public Spot withDeleted() {
        return Spot.builder()
                .id(this.id)
                .course(this.course)
                .title(this.title)
                .courseType(this.courseType)
                .orderNumber(this.orderNumber)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .isDeleted(true)
                .isCompleted(this.isCompleted)
                .count(this.count)
                .contentId(this.contentId)
                .build();
    }

    public Spot withCompleted() {
        return Spot.builder()
                .id(this.id)
                .course(this.course)
                .title(this.title)
                .courseType(this.courseType)
                .orderNumber(this.orderNumber)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .isDeleted(this.isDeleted)
                .isCompleted(true)
                .count(this.count)
                .contentId(this.contentId)
                .build();
    }

    public Spot withIncreasedCount() {
        return Spot.builder()
                .id(this.id)
                .course(this.course)
                .title(this.title)
                .courseType(this.courseType)
                .orderNumber(this.orderNumber)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .isDeleted(this.isDeleted)
                .isCompleted(this.isCompleted)
                .count(this.count + 1L)
                .contentId(this.contentId)
                .build();
    }

}
