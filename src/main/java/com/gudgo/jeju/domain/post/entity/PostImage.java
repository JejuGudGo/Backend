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
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "postId")
    private Posts posts;


    private void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
