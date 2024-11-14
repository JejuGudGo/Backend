package com.example.jejugudgo.domain.user.course.olle.entity;

import com.example.jejugudgo.domain.olle.entity.JejuOlleCourse;
import com.example.jejugudgo.domain.user.user.entity.User;
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
public class UserJejuOlleCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startAt;

    private LocalDate finishedAt;


    @ManyToOne()
    @JoinColumn(name = "jejuOlleCourseId")
    private JejuOlleCourse jejuOlleCourse;

    @ManyToOne()
    @JoinColumn(name = "userId")
    private User user;
}
