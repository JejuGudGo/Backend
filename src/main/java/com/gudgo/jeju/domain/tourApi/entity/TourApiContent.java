package com.gudgo.jeju.domain.tourApi.entity;

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
public class TourApiContent {
    @Id
    private String id;

    private String contentTypeId;

    private double latitude;

    private double longitude;

    private String updatedAt;

    private Long count;


    @ManyToOne
    @JoinColumn(name = "tourApiCategory3Id")
    private TourApiCategory3 tourApiCategory3;

    @OneToOne
    @JoinColumn(name = "tourApicontentInfoId")
    private TourApiContentInfo tourApiContentInfo;

    public TourApiContent withUpdatedAt(String updatedAt) {
        return TourApiContent.builder()
                .id(this.id)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .updatedAt(updatedAt != null ? updatedAt : this.updatedAt)
                .tourApiCategory3(this.tourApiCategory3)
                .tourApiContentInfo(this.tourApiContentInfo)
                .contentTypeId(this.contentTypeId)
                .build();
    }

    public TourApiContent withTourApiSpotDate(TourApiContentInfo tourApiContentInfo) {
        return TourApiContent.builder()
                .id(this.id)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .updatedAt(this.updatedAt)
                .tourApiCategory3(this.tourApiCategory3)
                .contentTypeId(this.contentTypeId)
                .tourApiContentInfo(tourApiContentInfo)
                .build();
    }
}