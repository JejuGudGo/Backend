package com.example.jejugudgo.domain.trail.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private double latitude;

    private double longitude;

    @Lob
    @Column(columnDefinition = "TEXT")
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

    @Enumerated(value = EnumType.STRING)
    private TrailType trailType;


    public Trail updateStarAvg(double starAvg) {
        return Trail.builder()
                .id(this.id)
                .title(this.title)
                .latitude(this.latitude)
                .longitude(this.longitude)
                .content(this.content)
                .address(this.address)
                .phoneNumber(this.phoneNumber)
                .homepageUrl(this.homepageUrl)
                .businessHours(this.businessHours)
                .fee(this.fee)
                .duration(this.duration)
                .imageUrl(this.imageUrl)
                .reference(this.reference)
                .starAvg(starAvg)
                .trailType(this.trailType)
                .build();
    }
}
