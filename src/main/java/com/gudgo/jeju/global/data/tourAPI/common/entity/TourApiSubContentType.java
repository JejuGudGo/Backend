package com.gudgo.jeju.global.data.tourAPI.common.entity;

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
    private String subContentId;


    @ManyToOne
    @JoinColumn(name = "tourApiCategory3Id")
    private TourApiCategory3 tourApiCategory3;
}
