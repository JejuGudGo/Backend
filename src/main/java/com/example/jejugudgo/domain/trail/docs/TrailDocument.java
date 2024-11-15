package com.example.jejugudgo.domain.trail.docs;

import com.example.jejugudgo.domain.trail.entity.Trail;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "trail")
public class TrailDocument {
    @Id
    private Long trailId;

    private String title;

    private double latitude;

    private double longitude;

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

    private String trailType;


    public static TrailDocument of(Trail trail) {
        TrailDocument document = new TrailDocument();
        document.setTrailId(trail.getId());
        document.setTitle(trail.getTitle());
        document.setLatitude(trail.getLatitude());
        document.setLongitude(trail.getLongitude());
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
        document.setTrailType(trail.getTrailType().getCode());

        return document;
    }

    public TrailDocument updateStarAvg(double starAvg) {
        TrailDocument trailDocument = this;
        trailDocument.setStarAvg(starAvg);
        return trailDocument;
    }
}
