package com.gudgo.jeju.domain.planner.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private String content;

    private double latitude;

    private double longitude;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "plannerId")
    private Planner planner;

    public CourseMedia withIsDeleted(boolean deleted) {
        return CourseMedia.builder()
                .id(this.getId())
                .content(this.content)
                .imageUrl(this.imageUrl)
                .latitude(this.getLatitude())
                .longitude(this.getLongitude())
                .isDeleted(deleted)
                .planner(this.planner)
                .build();
    }

    public CourseMedia withContent(String content) {
        return CourseMedia.builder()
                .id(this.getId())
                .content(content)
                .imageUrl(this.imageUrl)
                .latitude(this.getLatitude())
                .longitude(this.getLongitude())
                .isDeleted(this.isDeleted)
                .planner(this.planner)
                .build();
    }

    public CourseMedia withImageUrl(String imageUrl) {
        return CourseMedia.builder()
                .id(this.getId())
                .content(this.content)
                .imageUrl(imageUrl)
                .latitude(this.getLatitude())
                .longitude(this.getLongitude())
                .isDeleted(this.isDeleted)
                .planner(this.planner)
                .build();
    }
}
