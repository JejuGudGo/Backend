package com.gudgo.jeju.global.data.tourAPI.spot.entity;

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
public class TourApiSpotImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String imageUrl;


    @ManyToOne
    @JoinColumn(name = "tourApiSpotDataId")
    private TourApiSpotData tourApiSpotData;
}
