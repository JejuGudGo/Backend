package com.example.jejugudgo.domain.course.common.entity;

import com.example.jejugudgo.domain.course.common.enums.OlleTag;
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
public class OlleCourseTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OlleTag title;


    @ManyToOne
    @JoinColumn(name = "olleCourseId")
    private OlleCourse olleCourse;
}
