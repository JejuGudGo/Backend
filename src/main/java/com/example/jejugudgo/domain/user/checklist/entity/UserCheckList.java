package com.example.jejugudgo.domain.user.checklist.entity;

import com.example.jejugudgo.domain.user.common.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserCheckList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private boolean isFinished = false;

    private Long orders;


    @ManyToOne
    @JoinColumn(name="userId")
    private User user;


    public UserCheckList updateContent(String content) {
        return this.toBuilder()
                .content(content)
                .build();
    }

    public UserCheckList updateIsFinished(boolean isFinished) {
        return this.toBuilder()
                .isFinished(isFinished)
                .build();
    }

    public UserCheckList updateOrder(Long order) {
        return this.toBuilder()
                .orders(order)
                .build();
    }
}
