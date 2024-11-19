package com.example.jejugudgo.domain.course.jejugudgo.docs;

import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Document(indexName = "jeju_gudgo_course")
@Data
public class JejuGudgoCourseDocument {
    @Id
    private Long courseId;

    private String title;

    private LocalDate createdAt;

    private double starAvg;

    private LocalTime time;

    private double distance;

    private String imageUrl;

    private String summary;

    private Long viewCount;

    private String startSpotTitle;

    private double startLatitude;

    private double startLongitude;

    private String endSpotTitle;

    private double endLatitude;

    private double endLongitude;


    @Field(type = FieldType.Nested, includeInParent = true)
    private List<JejuGudgoCourseSpotDocument> jejuGudgoCourseSpots;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<JejuGudgoCourseTagDocument> jejuGudgoCourseTags;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<Long> bookmarkUsers;


    public static JejuGudgoCourseDocument of(JejuGudgoCourse jejuGudgoCourse, List<JejuGudgoCourseSpotDocument> spots, List<JejuGudgoCourseTagDocument> tags, List<Long> bookmarkUsers) {
        JejuGudgoCourseDocument document = new JejuGudgoCourseDocument();
        document.setCourseId(jejuGudgoCourse.getId());
        document.setTitle(jejuGudgoCourse.getTitle());
        document.setCreatedAt(jejuGudgoCourse.getCreatedAt());
        document.setStarAvg(jejuGudgoCourse.getStarAvg());
        document.setTime(jejuGudgoCourse.getTime());
        document.setDistance(jejuGudgoCourse.getDistance());
        document.setImageUrl(jejuGudgoCourse.getImageUrl());
        document.setSummary(jejuGudgoCourse.getSummary());
        document.setViewCount(jejuGudgoCourse.getViewCount());
        document.setStartSpotTitle(jejuGudgoCourse.getStartSpotTitle());
        document.setStartLatitude(jejuGudgoCourse.getStartLatitude());
        document.setStartLongitude(jejuGudgoCourse.getStartLongitude());
        document.setEndSpotTitle(jejuGudgoCourse.getEndSpotTitle());
        document.setEndLatitude(jejuGudgoCourse.getEndLatitude());
        document.setEndLongitude(jejuGudgoCourse.getEndLongitude());
        document.setJejuGudgoCourseSpots(spots);
        document.setJejuGudgoCourseTags(tags);
        document.setBookmarkUsers(bookmarkUsers);

        return document;
    }

    public JejuGudgoCourseDocument updateStarAvg(double starAvg) {
        JejuGudgoCourseDocument jejuGudgoCourseDocument = this;
        jejuGudgoCourseDocument.setStarAvg(starAvg);
        return jejuGudgoCourseDocument;
    }

    public JejuGudgoCourseDocument updateBookmarkUsers(List<Long> bookmarkUsers) {
        JejuGudgoCourseDocument jejuGudgoCourseDocument = this;
        jejuGudgoCourseDocument.setBookmarkUsers(bookmarkUsers);
        return jejuGudgoCourseDocument;
    }
}
