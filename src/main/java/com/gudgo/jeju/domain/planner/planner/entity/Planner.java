package com.gudgo.jeju.domain.planner.planner.entity;

import com.gudgo.jeju.domain.planner.course.entity.Course;
import com.gudgo.jeju.domain.post.chat.entity.ChatRoom;
import com.gudgo.jeju.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Planner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startAt;

    private boolean isDeleted;

    private boolean isPrivate;

    private String summary;

    private LocalTime time;

    private boolean isCompleted;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToOne
    @JoinColumn(name = "courseId")
    private Course course;

    @OneToOne
    @JoinColumn(name = "chatRoomId")
    private ChatRoom chatRoom;

    public Planner withStartAt(LocalDate startAt) {
        return Planner.builder()
                .id(this.id)
                .startAt(startAt)
                .isDeleted(this.isDeleted)
                .isPrivate(this.isPrivate)
                .summary(this.summary)
                .time(this.time)
                .isCompleted(this.isCompleted)
                .user(this.user)
                .course(this.course)
                .build();
    }

    public Planner withIsPrivate(boolean isPrivate) {
        return Planner.builder()
                .id(this.id)
                .startAt(this.startAt)
                .isDeleted(this.isDeleted)
                .isPrivate(isPrivate)
                .summary(this.summary)
                .time(this.time)
                .isCompleted(this.isCompleted)
                .user(this.user)
                .course(this.course)
                .build();
    }

    public Planner withSummary(String summary) {
        return Planner.builder()
                .id(this.id)
                .startAt(this.startAt)
                .isDeleted(this.isDeleted)
                .isPrivate(this.isPrivate)
                .summary(summary)
                .time(this.time)
                .isCompleted(this.isCompleted)
                .user(this.user)
                .course(this.course)
                .build();
    }

    public Planner withTime(LocalTime time) {
        return Planner.builder()
                .id(this.id)
                .startAt(this.startAt)
                .isDeleted(this.isDeleted)
                .isPrivate(this.isPrivate)
                .summary(this.summary)
                .time(time)
                .isCompleted(this.isCompleted)
                .user(this.user)
                .course(this.course)
                .build();
    }

    public Planner withCompleted(boolean isCompleted) {
        return Planner.builder()
                .id(this.id)
                .startAt(this.startAt)
                .isDeleted(this.isDeleted)
                .isPrivate(this.isPrivate)
                .summary(this.summary)
                .time(this.time)
                .isCompleted(isCompleted)
                .user(this.user)
                .course(this.course)
                .build();
    }
    public Planner withIsDeleted(boolean isDeleted) {
        return Planner.builder()
                .id(this.id)
                .startAt(this.startAt)
                .isDeleted(isDeleted)
                .isPrivate(this.isPrivate)
                .summary(this.summary)
                .time(this.time)
                .isCompleted(this.isCompleted)
                .user(this.user)
                .course(this.course)
                .build();
    }
}
