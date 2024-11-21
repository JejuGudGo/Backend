package com.example.jejugudgo.domain.course.jejugudgo.entity;

import com.example.jejugudgo.domain.user.course.jejuGudgo.entity.UserJejuGudgoCourse;
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
public class JejuGudgoCourseOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private WalkingType walkingType;

    private String time;

    private String distance;

    @ManyToOne
    @JoinColumn(name = "jejuGudgoCourseId")
    private JejuGudgoCourse jejuGudgoCourse;

    @ManyToOne
    @JoinColumn(name = "userJejuGudgoCourseId")
    private UserJejuGudgoCourse userJejuGudgoCourse;
}
