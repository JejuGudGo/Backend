package com.gudgo.jeju.global.data.tourAPI.spot.entity;

import com.gudgo.jeju.global.data.tourAPI.common.entity.TourApiSubContentType;
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

    private String title;

    private String address;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Double latitude;

    private Double longitude;

    @Column(columnDefinition = "TEXT")
    private String pageUrl;

    @Lob
    private String info;

    private String closeDay;

    private String organizerInfo;

    private String organizeNumber;

    private String eventStartDate;

    private String eventEndDate;

    @Column(columnDefinition = "TEXT")
    private String fee;

    private String time;

    private String park;

    private String rentStroller;

    private String availablePet;

    @Column(columnDefinition = "TEXT")
    private String eventContent;

    @Column(columnDefinition = "TEXT")
    private String eventFee;

    private String eventPlace;

    private String guideService;

    private String toilet;

    private String reserveInfo;


    @ManyToOne
    @JoinColumn(name = "tourApiSubContentTypeId")
    private TourApiSubContentType tourApiSubContentType;
}