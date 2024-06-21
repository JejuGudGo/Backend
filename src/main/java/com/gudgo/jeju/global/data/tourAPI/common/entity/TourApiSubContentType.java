package com.gudgo.jeju.global.data.tourAPI.common.entity;

import com.gudgo.jeju.global.data.tourAPI.spot.entity.TourApiSpotData;
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
public class TourApiSubContentType {
    @Id
    private String id;

    private double latitude;

    private double longitude;

    private String updatedAt;


    @ManyToOne
    @JoinColumn(name = "tourApiCategory3Id")
    private TourApiCategory3 tourApiCategory3;

    @OneToOne
    @JoinColumn(name = "tourApiSpotDataId")
    private TourApiSpotData tourApiSpotData;


    public TourApiSubContentType withUpdatedAt(String updatedAt) {
        return TourApiSubContentType.builder()
                .id(this.id)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .updatedAt(updatedAt != null ? updatedAt : this.updatedAt)
                .tourApiCategory3(this.tourApiCategory3)
                .tourApiSpotData(this.tourApiSpotData)
                .build();
    }

    public TourApiSubContentType withTourApiSpotData(TourApiSpotData tourApiSpotData) {
        return TourApiSubContentType.builder()
                .id(this.id)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .updatedAt(this.updatedAt)
                .tourApiCategory3(this.tourApiCategory3)
                .tourApiSpotData(tourApiSpotData != null ? tourApiSpotData : this.tourApiSpotData)
                .build();
    }
}