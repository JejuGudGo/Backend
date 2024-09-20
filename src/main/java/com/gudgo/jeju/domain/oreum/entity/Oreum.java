package com.gudgo.jeju.domain.oreum.entity;

import com.gudgo.jeju.domain.trail.entity.TrailType;
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
public class Oreum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String address;

    private double latitude;

    private double longitude;

    private String content;

    private LocalDate updatedAt;

    @Enumerated(value = EnumType.STRING)
    private TrailType type;
}
