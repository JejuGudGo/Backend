package com.example.jejugudgo.domain.user.entity;

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
public class UserCheckList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;

    private String content;

    private boolean isFinished;

    private Long orderNumber;

    public UserCheckList updateContent(String content) {
        return UserCheckList.builder()
                .id(id)
                .user(user)
                .content(content)
                .isFinished(isFinished)
                .orderNumber(orderNumber)
                .build();
    }
    public UserCheckList updateIsFinished(boolean isFinished) {
        return UserCheckList.builder()
                .id(id)
                .user(user)
                .content(content)
                .isFinished(isFinished)
                .orderNumber(orderNumber)
                .build();
    }

    public UserCheckList updateOrderNumber(Long orderNumber) {
        return UserCheckList.builder()
                .id(id)
                .user(user)
                .content(content)
                .isFinished(isFinished)
                .orderNumber(orderNumber)
                .build();
    }

}
