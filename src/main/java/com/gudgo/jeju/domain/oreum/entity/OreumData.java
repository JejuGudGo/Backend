package com.gudgo.jeju.domain.oreum.entity;

import com.gudgo.jeju.domain.tourApi.entity.TourApiCategory1;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OreumData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String address;

    private double latitude;

    private double longitude;

    private String content;

    private LocalDate updatedAt;

    @ManyToOne
    @JoinColumn(name = "tourApiCategory1Id")
    private TourApiCategory1 tourApiCategory1;
}
