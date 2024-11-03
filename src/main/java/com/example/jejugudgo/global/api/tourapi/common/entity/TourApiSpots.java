package com.example.jejugudgo.global.api.tourapi.common.entity;

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
public class TourApiSpots {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String contentId;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String summary;

    private String imageUrl;

    private String address;

    private double latitude;

    private double longitude;

    private String phone;

    private String homepage;

    private String fee;

    private String openingHours;

    // 행사의 경우---------------------
    private String eventStartDate;

    private String eventEndDate;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String eventContent;

    private String eventPlace;

    private String eventFee;

    private String sponsor;


    @ManyToOne
    @JoinColumn(name = "tourApiContentTypeId")
    private TourApiContentType tourApiContentType;


    public TourApiSpots updateTourApiSpots(TourApiSpots tourApiSpots) {
        return tourApiSpots;
    }
}
