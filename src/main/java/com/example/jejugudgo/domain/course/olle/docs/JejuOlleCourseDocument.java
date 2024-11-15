package com.example.jejugudgo.domain.course.olle.docs;

import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalTime;
import java.util.List;

@Document(indexName = "jeju_olle_course")
@Data
public class JejuOlleCourseDocument {
    @Id
    private Long courseId;

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

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<JejuOlleSpotDocument> spots;


    public static JejuOlleCourseDocument of(JejuOlleCourse jejuOlleCourse, List<JejuOlleSpotDocument> jejuOlleSpotDocuments) {
        JejuOlleCourseDocument document = new JejuOlleCourseDocument();
        document.setCourseId(jejuOlleCourse.getId());
        document.setTitle(jejuOlleCourse.getTitle());
        document.setTotalTime(jejuOlleCourse.getTotalTime());
        document.setOlleType(jejuOlleCourse.getOlleType().getType());
        document.setStartSpotTitle(jejuOlleCourse.getStartSpotTitle());
        document.setStartLatitude(jejuOlleCourse.getStartLatitude());
        document.setStartLongitude(jejuOlleCourse.getStartLongitude());
        document.setEndSpotTitle(jejuOlleCourse.getEndSpotTitle());
        document.setEndLatitude(jejuOlleCourse.getEndLatitude());
        document.setEndLongitude(jejuOlleCourse.getEndLongitude());
        document.setSummary(jejuOlleCourse.getSummary());
        document.setInfoAddress(jejuOlleCourse.getInfoAddress());
        document.setInfoOpenTime(jejuOlleCourse.getInfoOpenTime());
        document.setInfoPhone(jejuOlleCourse.getInfoPhone());
        document.setStarAvg(jejuOlleCourse.getStarAvg());
        document.setCourseImageUrl(jejuOlleCourse.getCourseImageUrl());
        document.setViewCount(jejuOlleCourse.getViewCount());
        document.setSpots(jejuOlleSpotDocuments);
        return document;
    }

    public JejuOlleCourseDocument updateStarAvg(double starAvg) {
        JejuOlleCourseDocument jejuOlleCourseDocument = this;
        jejuOlleCourseDocument.setStarAvg(starAvg);
        return jejuOlleCourseDocument;
    }
}

