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
public class TourApiCategory1 {
    @Id
    private String categoryCode;

    private String categoryName;


    @ManyToOne
    @JoinColumn(name = "tourApiContentTypeId")
    private TourApiContentType tourApiContentType;
}
