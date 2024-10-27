package com.example.jejugudgo.domain.course.entity;

import com.example.jejugudgo.domain.course.type.CourseType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JejuGudgoCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @Enumerated(value = EnumType.STRING)
    private CourseType courseType;

    private String title;

    private LocalDate createdAt;

    private Long originalCreatorId;

    private double starAvg;

    private Long totalTime;

    private String imageUrl;

    private String content;


}
