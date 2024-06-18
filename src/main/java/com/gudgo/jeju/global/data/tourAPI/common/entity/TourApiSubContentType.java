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

    private String latitude;

    private String longitude;

    private String updatedAt;


    @ManyToOne
    @JoinColumn(name = "id")
    private TourApiCategory3 categoryId;


    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setTourApiSpotData(TourApiSpotData tourApiSpotData) {
        this.tourApiSpotData = tourApiSpotData;
    }
}
