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

    private Long companionsNum;

    private LocalDate createdAt;

    private boolean isFinished;

    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;


//    public Posts withCourse(Course course) {
//        return Posts.builder()
//                .id(this.id)
//                .postType(this.postType)
//                .title(this.title)
//                .content(this.content)
//                .companionsNum(this.companionsNum)
//                .createdAt(this.createdAt)
//                .isFinished(this.isFinished)
//                .isDeleted(this.isDeleted)
//                .user(this.user)
//                .build();
//    }

    public Posts withTitle(String title) {
        return Posts.builder()
                .id(this.id)
                .postType(this.postType)
                .title(title)
                .content(this.content)
                .companionsNum(this.companionsNum)
                .createdAt(this.createdAt)
                .isFinished(this.isFinished)
                .isDeleted(this.isDeleted)
                .user(this.user)
                .build();
    }

    public Posts withContent(String content) {
        return Posts.builder()
                .id(this.id)
                .postType(this.postType)
                .title(this.title)
                .content(content)
                .companionsNum(this.companionsNum)
                .createdAt(this.createdAt)
                .isFinished(this.isFinished)
                .isDeleted(this.isDeleted)
                .user(this.user)
                .build();
    }

    public Posts withCompanionsNum(Long companionsNum) {
        return Posts.builder()
                .id(this.id)
                .postType(this.postType)
                .title(this.title)
                .content(this.content)
                .companionsNum(companionsNum)
                .createdAt(this.createdAt)
                .isFinished(this.isFinished)
                .isDeleted(this.isDeleted)
                .user(this.user)
                .build();
    }

    public Posts withIsFinished(boolean isFinished) {
        return Posts.builder()
                .id(this.id)
                .postType(this.postType)
                .title(this.title)
                .content(this.content)
                .companionsNum(this.companionsNum)
                .createdAt(this.createdAt)
                .isFinished(isFinished)
                .isDeleted(this.isDeleted)
                .user(this.user)
                .build();
    }

    public Posts withIsDeleted(boolean isDeleted) {
        return Posts.builder()
                .id(this.id)
                .postType(this.postType)
                .title(this.title)
                .content(this.content)
                .companionsNum(this.companionsNum)
                .createdAt(this.createdAt)
                .isFinished(this.isFinished)
                .isDeleted(isDeleted)
                .user(this.user)
                .build();
    }

    public Posts withIsFinishedAndIsDeleted(boolean isFinished, boolean isDeleted) {
        return Posts.builder()
                .id(this.id)
                .postType(this.postType)
                .title(this.title)
                .content(this.content)
                .companionsNum(this.companionsNum)
                .createdAt(this.createdAt)
                .isFinished(isFinished)
                .isDeleted(isDeleted)
                .user(this.user)
                .build();
    }
}
