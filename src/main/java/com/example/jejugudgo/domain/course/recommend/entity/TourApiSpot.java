package com.example.jejugudgo.domain.course.recommend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TourApiSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String thumbnailUrl;

    private String address;

    private double latitude;

    private double longitude;

    private LocalDateTime updatedAt;

    private Long viewCount;

    @ManyToOne
    @JoinColumn(name = "contentTypeId")
    private TourApiContentType contentType;
}
