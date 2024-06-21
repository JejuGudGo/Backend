package com.gudgo.jeju.domain.todo.entity;

import com.gudgo.jeju.domain.user.entity.User;
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
    private TodoType type;

    private Long orderNumber;

    private String content;

    private boolean isFinished;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    public Todo withDeleted(boolean isDeleted) {
        return Todo.builder()
                .id(this.id)
                .type(this.type)
                .orderNumber(this.orderNumber)
                .content(this.content)
                .isFinished(this.isFinished)
                .isDeleted(isDeleted)
                .build();
    }

    public Todo withType(TodoType todoType) {
        return Todo.builder()
                .id(this.id)
                .type(todoType)
                .orderNumber(this.orderNumber)
                .content(this.content)
                .isFinished(this.isFinished)
                .isDeleted(this.isDeleted)
                .build();
    }
    public Todo withOrderNumber(Long orderNumber) {
        return Todo.builder()
                .id(this.id)
                .type(this.type)
                .orderNumber(orderNumber)
                .content(this.content)
                .isFinished(this.isFinished)
                .isDeleted(this.isDeleted)
                .build();
    }
    public Todo withContent(String content) {
        return Todo.builder()
                .id(this.id)
                .type(this.type)
                .orderNumber(this.orderNumber)
                .content(content)
                .isFinished(this.isFinished)
                .isDeleted(this.isDeleted)
                .build();
    }
    public Todo withIsFinished(boolean isFinished) {
        return Todo.builder()
                .id(this.id)
                .type(this.type)
                .orderNumber(this.orderNumber)
                .content(this.content)
                .isFinished(isFinished)
                .isDeleted(this.isDeleted)
                .build();
    }
}