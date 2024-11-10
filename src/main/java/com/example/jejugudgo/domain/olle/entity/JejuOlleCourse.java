package com.example.jejugudgo.domain.olle.entity;

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
public class JejuOlleCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String totalDistance;

    private String totalTime;

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
    private String summary;

    private String infoAddress;

    private String infoOpenTime;

    private String infoPhone;

    private double starAvg;

    public JejuOlleCourse updateStartSpotEndSpot(String startSpotTitle, double startLatitude, double startLongitude,
                                                 String endSpotTitle, double endLatitude, double endLongitude) {
        return JejuOlleCourse.builder()
                .id(id)
                .title(title)
                .totalDistance(totalDistance)
                .totalTime(totalTime)
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
                .build();
    }
}
