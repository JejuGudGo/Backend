package com.gudgo.jeju.domain.post.entity;

import com.gudgo.jeju.domain.course.entity.Course;
import com.gudgo.jeju.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private PostType postType;

    private String title;

    private String content;

    private LocalDate createdAt;

    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @OneToOne
    @JoinColumn(name = "courseId")
    private Course course;


    private void setTitle(String title) {
        this.title = title;
    }

    private void setContent(String content) {
        this.content = content;
    }

    private void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
