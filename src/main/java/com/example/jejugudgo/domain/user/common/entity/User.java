package com.example.jejugudgo.domain.user.common.entity;

import com.example.jejugudgo.domain.user.common.enums.Provider;
import com.example.jejugudgo.domain.user.common.enums.Role;
import com.example.jejugudgo.domain.user.common.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_info")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Enumerated(value = EnumType.STRING)
    private Provider provider;

    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus;

    private String email;

    private String password;

    private String nickname;

    private String name;

    private String oauthUserId;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    private String phoneNumber;


    @OneToOne()
    @JoinColumn(name = "userProfileId")
    private UserProfile userProfile;


    public User updatePassword(String password) {
        return this.toBuilder()
                .password(password)
                .build();
    }

    public User updateDeletedAt() {
        return this.toBuilder()
                .userStatus(UserStatus.DELETED)
                .deletedAt(LocalDateTime.now())
                .build();
    }

    public User updateNickname(String nickname) {
        return this.toBuilder()
                .nickname(nickname)
                .build();
    }
}
