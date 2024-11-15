package com.example.jejugudgo.domain.course.jejugudgo.docs;

import com.example.jejugudgo.domain.course.jejugudgo.entity.JejuGudgoCourseSpot;
import lombok.Data;

@Data
public class JejuGudgoCourseSpotDocument {
    private Long spotId;

    private String title;

    private String spotType;

    private Long orderNumber;

    private String address;

    private double latitude;

    private double longitude;


    public static JejuGudgoCourseSpotDocument of (JejuGudgoCourseSpot jejuGudgoCourseSpot) {
        JejuGudgoCourseSpotDocument document = new JejuGudgoCourseSpotDocument();
        document.setSpotId(jejuGudgoCourseSpot.getId());
        document.setTitle(jejuGudgoCourseSpot.getTitle());
        document.setSpotType(jejuGudgoCourseSpot.getSpotType().getType());
        document.setOrderNumber(jejuGudgoCourseSpot.getOrderNumber());
        document.setAddress(jejuGudgoCourseSpot.getAddress());
        document.setLatitude(jejuGudgoCourseSpot.getLatitude());
        document.setLongitude(jejuGudgoCourseSpot.getLongitude());
        return document;
    }
}
