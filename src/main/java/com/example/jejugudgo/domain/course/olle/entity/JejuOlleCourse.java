package com.example.jejugudgo.domain.course.olle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JejuOlleCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String summary;

    private String distance;

    private String time;

    private Long viewCount;

    @Enumerated(value = EnumType.STRING)
    private OlleType olleType;

    private String startSpotTitle;

    private double startLatitude;

    private double startLongitude;

    private String endSpotTitle;

    private double endLatitude;

    private double endLongitude;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    private String infoAddress;

    private String infoOpenTime;

    private String infoPhone;

    private double starAvg;

    private String courseImageUrl;

    public JejuOlleCourse updateStartSpotEndSpot(String startSpotTitle, double startLatitude, double startLongitude,
                                                 String endSpotTitle, double endLatitude, double endLongitude) {
        return JejuOlleCourse.builder()
                .id(id)
                .title(title)
                .summary(startSpotTitle + "-" + endSpotTitle)
                .distance(distance)
                .time(time)
                .olleType(olleType)
                .startSpotTitle(startSpotTitle)
                .startLatitude(startLatitude)
                .startLongitude(startLongitude)
                .endSpotTitle(endSpotTitle)
                .endLatitude(endLatitude)
                .endLongitude(endLongitude)
                .summary(summary)
                .infoAddress(infoAddress)
                .infoOpenTime(infoOpenTime)
                .infoPhone(infoPhone)
                .starAvg(starAvg)
                .content(content)
                .courseImageUrl(courseImageUrl)
                .build();
    }

    public JejuOlleCourse updateStarAvg(double starAvg) {
        return JejuOlleCourse.builder()
                .id(this.id)
                .title(this.title)
                .distance(this.distance)
                .time(this.time)
                .olleType(this.olleType)
                .startSpotTitle(this.startSpotTitle)
                .startLatitude(this.startLatitude)
                .startLongitude(this.startLongitude)
                .endSpotTitle(this.endSpotTitle)
                .endLatitude(this.endLatitude)
                .endLongitude(this.endLongitude)
                .summary(this.summary)
                .infoAddress(this.infoAddress)
                .infoOpenTime(this.infoOpenTime)
                .infoPhone(this.infoPhone)
                .starAvg(starAvg)
                .courseImageUrl(this.courseImageUrl)
                .build();
    }
}
