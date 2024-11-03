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
public class UserTerms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isAgreed;

    private String agreedAt;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public UserTerms updateUserTerms(boolean isAgreed, String agreedAt) {
        UserTerms userTerms = UserTerms.builder()
                .id(this.id)
                .isAgreed(isAgreed)
                .agreedAt(agreedAt)
                .build();

        return userTerms;
    }
}
