package com.example.jejugudgo.domain.course.common.entity;

import com.example.jejugudgo.domain.course.common.enums.OlleType;
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
public class OlleCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private OlleType olleType;

    private String title;

    private String route;

    private String distance;

    private String time;

    private String summary;

    private String thumbnailUrl = "default";

    @Column(columnDefinition = "TEXT")
    private String content;

    private Double latitude; // 공식 안내소

    private Double longitude; // 공식 안내소

    private String address; // 공식 안내소

    private String openTime; // 공식 안내소

    private String tel; // 공식 안내소

    private Long reviewCount;

    private Double starAvg;

    private Long likeCount;

    private Long clickCount;

    private Double upToDate;


    public OlleCourse updateRoute(String route){
        return toBuilder()
                .route(route)
                .build();
    }
}
