package com.gudgo.jeju.domain.course.entity;

import com.gudgo.jeju.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalTime time;

    private LocalDateTime startAt;

    private String summary;

    private LocalDate createdAt;

    private boolean isDeleted;

    private Long originalCreatorId;

    private Long originalCoursed;


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;


    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

}
