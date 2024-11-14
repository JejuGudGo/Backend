package com.example.jejugudgo.domain.user.user.entity;

import com.example.jejugudgo.domain.user.myGudgo.profile.entity.UserProfile;
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

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    private Provider provider;

    private String oauthUserId;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    private String phoneNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userProfileId")
    private UserProfile userProfile;

    // with -> update 어떤가요?
    public User updatePassword(String password) {
        return User.builder()
                .id(this.id)
                .email(this.email)
                .password(password)
                .nickname(this.nickname)
                .name(this.name)
                .role(this.role)
                .provider(this.provider)
                .createdAt(this.createdAt)
                .phoneNumber(this.phoneNumber)
                .userProfile(this.userProfile)
                .deletedAt(this.deletedAt)
                .build();
    }

    public User updateUserStatus() {
        return User.builder()
                .id(this.id)
                .email(this.email)
                .password(password)
                .nickname(this.nickname)
                .name(this.name)
                .role(this.role)
                .provider(this.provider)
                .createdAt(this.createdAt)
                .phoneNumber(this.phoneNumber)
                .userProfile(this.userProfile)
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public User updateNickname(String nickname) {
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .nickname(nickname)
                .name(name)
                .role(role)
                .provider(provider)
                .createdAt(createdAt)
                .phoneNumber(phoneNumber)
                .userProfile(userProfile)
                .deletedAt(LocalDateTime.now())
                .build();
    }
}
