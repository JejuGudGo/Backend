package com.example.jejugudgo.domain.course.tamp.entity;

import com.example.jejugudgo.domain.mygudgo.course.entity.UserJejuGudgoCourse;
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
public class WalkingPath {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "userJejuGudgoCourseId")
    private UserJejuGudgoCourse userJejuGudgoCourse;


}
