package com.example.jejugudgo.domain.home.event.entity;

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
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String host;

    private String time;

    private String thumbnailUrl;

    private String homepage;

    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;
}