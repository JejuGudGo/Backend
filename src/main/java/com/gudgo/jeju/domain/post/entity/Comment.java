package com.gudgo.jeju.domain.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long parentCommentId;

    private String nickname;

    private Long numberTag;

    private String profileImageUrl;

    private String content;

    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "postId")
    private Posts posts;


    private void setContent(String content) {
        this.content = content;
    }

    private void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}