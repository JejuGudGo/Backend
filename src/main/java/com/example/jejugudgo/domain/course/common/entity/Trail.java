package com.example.jejugudgo.domain.course.common.entity;

import com.example.jejugudgo.domain.course.common.enums.TrailTag;
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
public class Trail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TrailTag trailTag;

    private String title;

    private String address;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    private String tel;

    private String homepage;

    private String openTime;

    private String fee;

    private String time;

    private String thumbnailUrl = "default";

    private Double latitude;

    private Double longitude;

    private Long reviewCount;

    private Double starAvg;

    private Long likeCount;

    private Double upToDate;
}
