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

    public CourseMedia withContentAndImageUrl(CourseMediaUpdateRequestDto requestDto) {
        return CourseMedia.builder()
                .id(this.getId())
                .course(this.getCourse())
                .content(requestDto.content())
                .imageUrl(requestDto.imageUrl())
                .latitude(this.getLatitude())
                .longitude(this.getLongitude())
                .build();
    }
}