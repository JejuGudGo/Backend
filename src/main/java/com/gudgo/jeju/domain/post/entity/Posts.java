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


    public Posts withTitle(String title) {
        return Posts.builder()
                .id(this.id)
                .postType(this.postType)
                .title(title != null ? title : this.title)
                .content(this.content)
                .createdAt(this.createdAt)
                .isDeleted(this.isDeleted)
                .user(this.user)
                .course(this.course)
                .build();
    }

    public Posts withContent(String content) {
        return Posts.builder()
                .id(this.id)
                .postType(this.postType)
                .title(this.title)
                .content(content != null ? content : this.content)
                .createdAt(this.createdAt)
                .isDeleted(this.isDeleted)
                .user(this.user)
                .course(this.course)
                .build();
    }

    public Posts withIsDeleted(boolean isDeleted) {
        return Posts.builder()
                .id(this.id)
                .postType(this.postType)
                .title(this.title)
                .content(this.content)
                .createdAt(this.createdAt)
                .isDeleted(isDeleted)
                .user(this.user)
                .course(this.course)
                .build();
    }
}
