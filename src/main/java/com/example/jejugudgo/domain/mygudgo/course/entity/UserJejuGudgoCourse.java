package com.example.jejugudgo.domain.mygudgo.course.entity;

import com.example.jejugudgo.domain.course.common.entity.JejuGudgoCourse;
import com.example.jejugudgo.domain.user.common.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserJejuGudgoCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private boolean isPrivate;

    private String route;

    private String thumbnailUrl = "default";

    private String content;

    private LocalDate createdAt;

    private LocalDate finishedAt;

    private String time;

    private String distance;

    private String speed;

    private String kcal;

    private String steps;

    private String isImported; // 퍼온 코스 여부


    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "jejuGudgoCourseId")
    private JejuGudgoCourse jejuGudgoCourse;


    public UserJejuGudgoCourse updateThumbnailUrl(String thumbnailUrl) {
        return toBuilder()
                .thumbnailUrl(thumbnailUrl)
                .build();
    }

    public UserJejuGudgoCourse updateContent(String content) {
        return toBuilder()
                .content(content)
                .build();
    }

    // TODO : 코스 완주시 업데이트 해야하는 부분 담긴 메서드 작성
}
