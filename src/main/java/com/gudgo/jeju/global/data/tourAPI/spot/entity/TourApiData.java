package com.gudgo.jeju.global.data.tourAPI.spot.entity;

import com.gudgo.jeju.global.data.tourAPI.common.entity.TourApiCategory3;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourApiData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long categoryId;

    private String title;

    private String address;

    private Long content;

    private Double latitude;

    private Double longitude;

    private String pageUrl;

    private String info;

    private String closeDay;

    private String organizerInfo;

    private String organizeNumber;

    private String eventStartDate;

    private String eventEndDate;

    private String fee;

    private String time;

    private String park;

    private String rentStroller;

    private String availablePet;

    private String eventOverview;

    private String eventContent;

    private String eventPlace;

    private String guideService;

    private String toilet;

    private String reserveInfo;


    @ManyToOne
    @JoinColumn(name = "tourApiCategory3Id")
    private TourApiCategory3 tourApiCategory3;
}
