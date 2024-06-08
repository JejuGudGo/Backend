package com.gudgo.jeju.domain.user.entity;

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

    private Long randomTag;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String provider;

    private LocalDateTime createdAt;

    private boolean isDeleted;
}

