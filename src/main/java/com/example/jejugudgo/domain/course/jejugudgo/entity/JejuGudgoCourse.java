package com.example.jejugudgo.domain.course.jejugudgo.entity;

import com.example.jejugudgo.domain.user.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JejuGudgoCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDate createdAt;

    private double starAvg;

    private String time;

    private String distance;

    private String imageUrl;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content; // 유저가 작성한 설명

    private Long viewCount;

    private String startSpotTitle;

    private double startLatitude;

    private double startLongitude;

    private String endSpotTitle;

    private double endLatitude;

    private double endLongitude;

    private boolean isDeleted;


    @ManyToOne
    @JoinColumn(name = "originUserId")
    private User user;

    public JejuGudgoCourse updateStarAvg(double starAvg) {
        return JejuGudgoCourse.builder()
                .id(this.id)
                .title(this.title)
                .createdAt(this.createdAt)
                .starAvg(starAvg)
                .time(this.time)
                .content(this.content)
                .distance(this.distance)
                .imageUrl(this.imageUrl)
                .viewCount(this.viewCount)
                .isDeleted(this.isDeleted)
                .build();
    }
}
