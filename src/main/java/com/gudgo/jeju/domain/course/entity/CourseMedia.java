package com.gudgo.jeju.domain.course.entity;


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

    @Setter
    private String imageUrl;

    @Setter
    private String content;

    @Setter
    private double latitude;

    @Setter
    private double longitude;

}
