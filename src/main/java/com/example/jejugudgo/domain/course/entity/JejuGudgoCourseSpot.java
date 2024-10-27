package com.example.jejugudgo.domain.course.entity;

import com.example.jejugudgo.domain.course.type.SpotType;
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
public class JejuGudgoCourseSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name="ttourApiCategory1Id")
//    private TourApiCategory1 tourApiCategory1;

    private String title;

    @Enumerated(value = EnumType.STRING)
    private SpotType spotType;

    private Long orderNumber;

    private String address;

    private double latitude;

    private double longitude;

    private Long count;

    @ManyToOne
    @JoinColumn(name = "courseId")
    private JejuGudgoCourse jejuGudgoCourse;

}
