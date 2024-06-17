package com.gudgo.jeju.domain.course.entity;

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
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private TodoType todoType;

    private Long orderNumber;

    private String content;

    private boolean isFinished;

    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;

}
