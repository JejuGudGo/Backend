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

    private Long order;

    private String content;

    private boolean isFinished;

    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "courseId")
    private Course course;


    private void setTodoType(TodoType todoType) {
        this.todoType = todoType;
    }

    private void setOrder(Long order) {
        this.order = order;
    }

    private void setContent(String content) {
        this.content = content;
    }

    private void setIsFinished(boolean isFinished) {
        this.isFinished =  isFinished;
    }

    private void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
