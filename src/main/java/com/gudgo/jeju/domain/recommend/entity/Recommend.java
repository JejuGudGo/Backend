package com.gudgo.jeju.domain.recommend.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title1;

    private String author;

    private LocalDateTime createdAt;

    private String title2;

    private String section1Title;
    private String section2Title;
    private String section3Title;
    private String section4Title;

    private String section1Content;
    private String section2Content;
    private String section3Content;
    private String section4Content;

    private String imageUrl;
}
