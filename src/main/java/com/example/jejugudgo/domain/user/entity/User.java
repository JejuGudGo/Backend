package com.example.jejugudgo.domain.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    private String email;

    private String password;

    private String nickname;

    private String name;

    private Long numberTag;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String provider;

    private LocalDateTime createdAt;

    private boolean isDeleted = false;

    private String phoneNumber;

    // TODO: profile 생성
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "profileId")
//    private Profile profile;

    // with -> update 어떤가요?
    public User updatePassword(String password) {
        return User.builder()
                .id(this.id)
                .email(this.email)
                .password(password)
                .nickname(this.nickname)
                .name(this.name)
                .numberTag(this.numberTag)
                .role(this.role)
                .provider(this.provider)
                .createdAt(this.createdAt)
                .isDeleted(this.isDeleted)
                .phoneNumber(this.phoneNumber)
//                .profile(this.profile)
                .build();
    }
}
