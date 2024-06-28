package com.gudgo.jeju.domain.planner.entity;

import com.gudgo.jeju.domain.tourApi.entity.TourApiCategory1;
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
    private SpotType spotType;

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

    @ManyToOne
    @JoinColumn(name = "TourApiCategory1Id")
    private TourApiCategory1 tourApiCategory1;

    public Spot withOrderNumber(Long orderNumber) {

        return Spot.builder()
                .id(this.id)
                .course(this.course)
                .title(this.title)
                .spotType(this.spotType)
                .orderNumber(orderNumber)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .isDeleted(true)
                .isCompleted(this.isCompleted)
                .count(this.count)
                .contentId(this.contentId)
                .tourApiCategory1(this.tourApiCategory1)
                .build();
    }

    public Spot withDeleted() {
        return Spot.builder()
                .id(this.id)
                .course(this.course)
                .title(this.title)
                .spotType(this.spotType)
                .orderNumber(this.orderNumber)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .isDeleted(true)
                .isCompleted(this.isCompleted)
                .count(this.count)
                .contentId(this.contentId)
                .tourApiCategory1(this.tourApiCategory1)
                .build();
    }

    public Spot withCompleted() {
        return Spot.builder()
                .id(this.id)
                .course(this.course)
                .title(this.title)
                .spotType(this.spotType)
                .orderNumber(this.orderNumber)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .isDeleted(this.isDeleted)
                .isCompleted(true)
                .count(this.count)
                .contentId(this.contentId)
                .tourApiCategory1(this.tourApiCategory1)
                .build();
    }

    public Spot withIncreasedCount() {
        return Spot.builder()
                .id(this.id)
                .course(this.course)
                .title(this.title)
                .spotType(this.spotType)
                .orderNumber(this.orderNumber)
                .address(this.address)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .isDeleted(this.isDeleted)
                .isCompleted(this.isCompleted)
                .count(this.count + 1L)
                .contentId(this.contentId)
                .tourApiCategory1(this.tourApiCategory1)
                .build();
    }

}
