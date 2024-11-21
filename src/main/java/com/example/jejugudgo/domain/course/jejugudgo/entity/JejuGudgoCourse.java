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

    private String summary;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

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
                .distance(this.distance)
                .imageUrl(this.imageUrl)
                .summary(this.summary)
                .content(this.content)
                .viewCount(this.viewCount)
                .startSpotTitle(this.startSpotTitle)
                .startLatitude(this.startLatitude)
                .startLongitude(this.startLongitude)
                .endSpotTitle(this.endSpotTitle)
                .endLatitude(this.endLatitude)
                .endLongitude(this.endLongitude)
                .isDeleted(this.isDeleted)
                .user(this.user)
                .build();
    }
    public JejuGudgoCourse updateDetails(String newImageUrl, String newTitle, String newContent) {
        return JejuGudgoCourse.builder()
                .id(this.id)
                .title(newTitle != null ? newTitle : this.title)
                .createdAt(this.createdAt)
                .starAvg(this.starAvg)
                .time(this.time)
                .distance(this.distance)
                .imageUrl(newImageUrl != null ? newImageUrl : this.imageUrl)
                .summary(this.summary)
                .content(newContent != null ? newContent : this.content)
                .viewCount(this.viewCount)
                .startSpotTitle(this.startSpotTitle)
                .startLatitude(this.startLatitude)
                .startLongitude(this.startLongitude)
                .endSpotTitle(this.endSpotTitle)
                .endLatitude(this.endLatitude)
                .endLongitude(this.endLongitude)
                .isDeleted(this.isDeleted)
                .user(this.user)
                .build();
    }

    public JejuGudgoCourse updateDeleted(boolean isDeleted) {
        return JejuGudgoCourse.builder()
                .id(this.id)
                .title(this.title)
                .createdAt(this.createdAt)
                .starAvg(this.starAvg)
                .time(this.time)
                .distance(this.distance)
                .imageUrl(this.imageUrl)
                .summary(this.summary)
                .content(this.content)
                .viewCount(this.viewCount)
                .startSpotTitle(this.startSpotTitle)
                .startLatitude(this.startLatitude)
                .startLongitude(this.startLongitude)
                .endSpotTitle(this.endSpotTitle)
                .endLatitude(this.endLatitude)
                .endLongitude(this.endLongitude)
                .isDeleted(isDeleted)
                .user(this.user)
                .build();
    }

}
