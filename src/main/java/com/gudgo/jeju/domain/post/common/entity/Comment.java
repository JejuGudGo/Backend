package com.gudgo.jeju.domain.post.common.entity;

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

    private Long userId;

    private String content;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "postsId")
    private Posts posts;


    public Comment withContent(String content) {
        return Comment.builder()
                .id(this.id)
                .parentCommentId(this.parentCommentId)
                .userId(userId)
                .content(content != null ? content : this.content)
                .isDeleted(this.isDeleted)
                .posts(this.posts)
                .build();
    }

    public Comment withIsDeleted(boolean isDeleted) {
        return Comment.builder()
                .id(this.id)
                .parentCommentId(this.parentCommentId)
                .userId(this.userId)
                .content(this.content)
                .isDeleted(isDeleted)
                .posts(this.posts)
                .build();
    }
}
