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
    @JoinColumn(name = "postsId")
    private Posts posts;


    public PostImage withIsDeleted(boolean isDeleted) {
        return PostImage.builder()
                .id(this.id)
                .imageUrl(this.imageUrl)
                .isDeleted(isDeleted)
                .posts(this.posts)
                .build();
    }
}
