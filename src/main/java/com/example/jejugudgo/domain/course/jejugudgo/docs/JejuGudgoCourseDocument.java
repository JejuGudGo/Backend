package com.example.jejugudgo.domain.course.jejugudgo.docs;

import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourse;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDate;
import java.util.List;

@Document(indexName = "jeju_gudgo_course")
@Data
public class JejuGudgoCourseDocument {
    @Id
    private Long id;

    private String title;

    private String type;

    private LocalDate createdAt;

    private double starAvg;

    private String time;

    private String distance;

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
    private List<JejuGudgoCourseSpotDocument> spots;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<JejuGudgoCourseTagDocument> tags;

    @Field(type = FieldType.Nested, includeInParent = true)
    private List<Long> bookmarkUsers;


    public static JejuGudgoCourseDocument of(JejuGudgoCourse jejuGudgoCourse, List<JejuGudgoCourseSpotDocument> spots, List<JejuGudgoCourseTagDocument> tags, List<Long> bookmarkUsers) {
        JejuGudgoCourseDocument document = new JejuGudgoCourseDocument();
        document.setType("제주걷고");
        document.setId(jejuGudgoCourse.getId());
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
        document.setSpots(spots);
        document.setTags(tags);
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
