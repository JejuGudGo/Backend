package com.example.jejugudgo.domain.trail.docs;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "trail")
public class Trail {
    @Id
    private String id;

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
}
