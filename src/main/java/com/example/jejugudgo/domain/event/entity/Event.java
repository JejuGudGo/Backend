package com.example.jejugudgo.domain.event.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String host;

    private LocalDate startDate;

    private LocalDate endDate;

    private String thumbnail;

    private String link;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;
}
