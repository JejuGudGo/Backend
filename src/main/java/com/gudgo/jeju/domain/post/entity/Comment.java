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


    public Comment withContent(String content) {
        return Comment.builder()
                .id(this.id)
                .parentCommentId(this.parentCommentId)
                .nickname(this.nickname)
                .numberTag(this.numberTag)
                .profileImageUrl(this.profileImageUrl)
                .content(content != null ? content : this.content)
                .isDeleted(this.isDeleted)
                .posts(this.posts)
                .build();
    }

    public Comment withIsDeleted(boolean isDeleted) {
        return Comment.builder()
                .id(this.id)
                .parentCommentId(this.parentCommentId)
                .nickname(this.nickname)
                .numberTag(this.numberTag)
                .profileImageUrl(this.profileImageUrl)
                .content(this.content)
                .isDeleted(isDeleted)
                .posts(this.posts)
                .build();
    }
}
