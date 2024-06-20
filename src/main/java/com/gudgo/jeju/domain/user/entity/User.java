package com.gudgo.jeju.domain.user.entity;

import com.gudgo.jeju.domain.course.entity.Todo;
import com.gudgo.jeju.domain.profile.entity.Profile;
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

    private String provider;

    private String nickname;

    @Column(name = "number_tag")
    private Long numberTag;

    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;


    @OneToOne
    @JoinColumn(name = "profileId")
    private Profile profile;

    @OneToOne
    @JoinColumn(name = "todoId")
    private Todo todo;
}

