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


    public PostImage withImageUrl(String imageUrl) {
        return PostImage.builder()
                .id(this.id)
                .imageUrl(imageUrl != null ? imageUrl : this.imageUrl)
                .isDeleted(this.isDeleted)
                .posts(this.posts)
                .build();
    }

    public PostImage withIsDeleted(boolean isDeleted) {
        return PostImage.builder()
                .id(this.id)
                .imageUrl(this.imageUrl)
                .isDeleted(isDeleted)
                .posts(this.posts)
                .build();
    }

    public PostImage withPosts(Posts posts) {
        return PostImage.builder()
                .id(this.id)
                .imageUrl(this.imageUrl)
                .isDeleted(this.isDeleted)
                .posts(posts != null ? posts : this.posts)
                .build();
    }
}
