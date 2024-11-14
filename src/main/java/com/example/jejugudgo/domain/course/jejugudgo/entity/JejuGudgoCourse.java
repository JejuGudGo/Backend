package com.example.jejugudgo.domain.course.jejugudgo.entity;

import com.example.jejugudgo.domain.user.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JejuGudgoCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDate createdAt;

    private double starAvg;

    private String time;

    private String distance;

    private String imageUrl;

    private String summary;

    private Long viewCount;


    @ManyToOne
    @JoinColumn(name = "originUserId")
    private User user;
}
