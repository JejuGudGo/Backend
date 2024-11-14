package com.example.jejugudgo.domain.course.olle.docs;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "jeju_olle_course")
@Data
public class JejuOlleCourse {
    @Id
    private Long id;

    private String title;

    private String totalDistance;

    private String totalTime;

    private String olleType;

    private String startSpotTitle;

    private double startLatitude;

    private double startLongitude;

    private String endSpotTitle;

    private double endLatitude;

    private double endLongitude;

    private String summary;

    private String infoAddress;

    private String infoOpenTime;

    private String infoPhone;

    private double starAvg;

    private String courseImageUrl;

    private Long viewCount;
}
