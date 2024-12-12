package com.example.jejugudgo.domain.course.common.entity;

import com.example.jejugudgo.domain.user.common.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class JejuGudgoCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long reviewCount;

    private Double starAvg;

    private Long likeCount;

    private Long clickCount;

    private Double upToDate;

    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    public JejuGudgoCourse updateIsDeleted(boolean isDeleted) {
        return toBuilder()
                .isDeleted(isDeleted)
                .build();
    }
}
