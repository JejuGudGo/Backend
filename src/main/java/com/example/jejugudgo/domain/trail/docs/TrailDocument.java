package com.example.jejugudgo.domain.trail.docs;

import com.example.jejugudgo.domain.course.jejugudgo.docs.JejuGudgoCourseTagDocument;
import com.example.jejugudgo.domain.review.enums.ReviewType;
import com.example.jejugudgo.domain.trail.entity.Trail;
import com.example.jejugudgo.domain.user.myGudgo.bookmark.entity.BookmarkType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(indexName = "trail")
public class TrailDocument {
    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Keyword)
    private String titleKeyword;

    private double startLatitude;

    private double startLongitude;

    private String content;

    private String address;

    private String phoneNumber;

    private String homepageUrl;

    private String businessHours;

    private String fee;

    private String duration;

    private String imageUrl;

    private String reference;

    private double starAvg;

    private String type;

    private String tag;


    public static TrailDocument of(Trail trail) {
        TrailDocument document = new TrailDocument();
        document.setId(trail.getId());
        document.setTitle(trail.getTitle());
        document.setTitleKeyword(trail.getTitle());
        document.setStartLatitude(trail.getLatitude());
        document.setStartLongitude(trail.getLongitude());
        document.setContent(trail.getContent());
        document.setAddress(trail.getAddress());
        document.setPhoneNumber(trail.getPhoneNumber());
        document.setHomepageUrl(trail.getHomepageUrl());
        document.setBusinessHours(trail.getBusinessHours());
        document.setFee(trail.getFee());
        document.setDuration(trail.getDuration());
        document.setImageUrl(trail.getImageUrl());
        document.setReference(trail.getReference());
        document.setStarAvg(trail.getStarAvg());
        document.setType(BookmarkType.TRAIL.getCode());
        document.setTag(trail.getTrailType().getCode());

        return document;
    }

    public TrailDocument updateStarAvg(double starAvg) {
        TrailDocument trailDocument = this;
        trailDocument.setStarAvg(starAvg);
        return trailDocument;
    }
}
