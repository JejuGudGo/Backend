package com.gudgo.jeju.domain.post.entity;

import com.gudgo.jeju.domain.user.entity.User;
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

    private String content;

    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "postId")
    private Posts posts;

    @ManyToOne
    @JoinColumn(name="userId")
    private User user;


    private void setContent(String content) {
        this.content = content;
    }

    private void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
