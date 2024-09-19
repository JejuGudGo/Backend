package com.gudgo.jeju.domain.planner.courseMedia.entity;


import com.gudgo.jeju.domain.planner.planner.entity.Planner;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//   private String imageUrl;

    private String selfieImageUrl;

    private String backImageUrl;

    private String content;

    private double latitude;

    private double longitude;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "plannerId")
    private Planner planner;

    public CourseMedia withIsDeleted(boolean deleted) {
        return CourseMedia.builder()
                .id(this.getId())
                .content(this.content)
//                .imageUrl(this.imageUrl)
                .selfieImageUrl(selfieImageUrl)
                .backImageUrl(backImageUrl)
                .latitude(this.getLatitude())
                .longitude(this.getLongitude())
                .isDeleted(deleted)
                .planner(this.planner)
                .build();
    }

    public CourseMedia withContent(String content) {
        return CourseMedia.builder()
                .id(this.getId())
                .content(content)
//                .imageUrl(this.imageUrl)
                .selfieImageUrl(selfieImageUrl)
                .backImageUrl(backImageUrl)
                .latitude(this.getLatitude())
                .longitude(this.getLongitude())
                .isDeleted(this.isDeleted)
                .planner(this.planner)
                .build();
    }

//    public CourseMedia withImageUrl(String imageUrl) {
//        return CourseMedia.builder()
//                .id(this.getId())
//                .content(this.content)
//                .imageUrl(imageUrl)
//                .latitude(this.getLatitude())
//                .longitude(this.getLongitude())
//                .isDeleted(this.isDeleted)
//                .planner(this.planner)
//                .build();
//    }

    public CourseMedia withSelfieImageUrl(String selfieImageUrl) {
        return CourseMedia.builder()
                .id(getId())
                .content(content)
                .selfieImageUrl(selfieImageUrl)
                .backImageUrl(backImageUrl)
                .latitude(getLatitude())
                .longitude(getLongitude())
                .isDeleted(isDeleted)
                .planner(planner)
                .build();
    }

    public CourseMedia withBackImageUrl(String backImageUrl) {
        return CourseMedia.builder()
                .id(getId())
                .content(content)
                .selfieImageUrl(selfieImageUrl)
                .backImageUrl(backImageUrl)
                .latitude(getLatitude())
                .longitude(getLongitude())
                .isDeleted(isDeleted)
                .planner(planner)
                .build();
    }
}
