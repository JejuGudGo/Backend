package com.example.jejugudgo.domain.course.olle.entity;

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
public class JejuOlleCourseTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OlleTag olleTag;

    @ManyToOne
    @JoinColumn(name = "jejuOlleCourse")
    private JejuOlleCourse jejuOlleCourse;
}
