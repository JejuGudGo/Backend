package com.example.jejugudgo.domain.course.olle.docs;

import com.example.jejugudgo.domain.course.olle.entity.JejuOlleCourse;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Collections;
import java.util.List;

@Document(indexName = "jeju_olle_course")
@Data
public class JejuOlleCourseDocument {
    @Id
    private Long id;

    private String title;

    private String summary;

    private String distance;

    private String time;

    private String type;

    private String startSpotTitle;

    private double startLatitude;

    private double startLongitude;

    private String endSpotTitle;

    private double endLatitude;

    private double endLongitude;

    private String content;

    private String infoAddress;

    private String infoOpenTime;

    private String infoPhone;

    private double starAvg;

    private String imageUrl;

    private Long viewCount;

    @Field(type = FieldType.Keyword)
    private List<String> tags;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<JejuOlleSpotDocument> spots;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<Long> bookmarkUsers;


    public static JejuOlleCourseDocument of(JejuOlleCourse jejuOlleCourse, List<JejuOlleSpotDocument> jejuOlleSpotDocuments, List<Long> bookmarkUsers, List<String> olleTags) {
        JejuOlleCourseDocument document = new JejuOlleCourseDocument();
        document.setId(jejuOlleCourse.getId());
        document.setTitle(jejuOlleCourse.getTitle());
        document.setContent(jejuOlleCourse.getContent());
        document.setTags(olleTags != null ? olleTags : Collections.emptyList());
        document.setTime(jejuOlleCourse.getTime());
        document.setDistance(jejuOlleCourse.getDistance());
        document.setType(jejuOlleCourse.getOlleType().getType());
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
        document.setImageUrl(jejuOlleCourse.getCourseImageUrl());
        document.setViewCount(jejuOlleCourse.getViewCount());
        document.setSpots(jejuOlleSpotDocuments);
        document.setBookmarkUsers(bookmarkUsers != null ? bookmarkUsers : Collections.emptyList());
        return document;
    }

    public JejuOlleCourseDocument updateStarAvg(double starAvg) {
        JejuOlleCourseDocument jejuOlleCourseDocument = this;
        jejuOlleCourseDocument.setStarAvg(starAvg);
        return jejuOlleCourseDocument;
    }

    public JejuOlleCourseDocument updateBookmarkUsers(List<Long> bookmarkUsers) {
        JejuOlleCourseDocument jejuOlleCourseDocument = this;
        jejuOlleCourseDocument.setBookmarkUsers(bookmarkUsers);
        return jejuOlleCourseDocument;
    }
}

