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
public class TourApiCategory3 {
    @Id
    private String id;

    private String categoryName;


    @ManyToOne
    @JoinColumn(name = "categoryId")
    private TourApiCategory2 tourApiCategory2;
}
