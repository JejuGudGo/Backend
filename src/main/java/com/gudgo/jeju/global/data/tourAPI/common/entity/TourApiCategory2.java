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
public class TourApiCategory2 {
    @Id
    private String id;

    private String categoryName;


    @ManyToOne
    @JoinColumn(name = "tourApiCategory1Id")
    private TourApiCategory1 tourApiCategory1;
}