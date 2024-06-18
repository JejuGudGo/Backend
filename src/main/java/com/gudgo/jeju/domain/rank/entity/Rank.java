package com.gudgo.jeju.domain.rank.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.sql.Time;
import java.time.LocalTime;

public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String level;
    private LocalTime minimum;
    private LocalTime maximum;
}
