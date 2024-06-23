package com.gudgo.jeju.domain.course.entity;


import com.gudgo.jeju.domain.course.dto.request.course.CourseMediaUpdateRequestDto;
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

    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

    private String imageUrl;

    private String content;

    private double latitude;

    private double longitude;

    private boolean isDeleted;


    public CourseMedia withIsDeleted() {
        return CourseMedia.builder()
                .id(this.getId())
                .course(this.getCourse())
                .content(this.content)
                .imageUrl(this.imageUrl)
                .latitude(this.getLatitude())
                .longitude(this.getLongitude())
                .isDeleted(true)
                .build();
    }

    public CourseMedia withContent(String content) {
        return CourseMedia.builder()
                .id(this.getId())
                .course(this.getCourse())
                .content(content)
                .imageUrl(this.imageUrl)
                .latitude(this.getLatitude())
                .longitude(this.getLongitude())
                .isDeleted(this.isDeleted)
                .build();
    }

    public CourseMedia withImageUrl(String imageUrl) {
        return CourseMedia.builder()
                .id(this.getId())
                .course(this.getCourse())
                .content(this.content)
                .imageUrl(imageUrl)
                .latitude(this.getLatitude())
                .longitude(this.getLongitude())
                .isDeleted(this.isDeleted)
                .build();
    }
}
