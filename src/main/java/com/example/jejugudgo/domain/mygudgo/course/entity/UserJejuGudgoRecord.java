package com.example.jejugudgo.domain.mygudgo.course.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserJejuGudgoRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String selfieImageUrl = "default";

    private String backImageUrl = "default";

    private double latitude;

    private double longitude;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;


    @ManyToOne()
    @JoinColumn(name = "userCourseId")
    private UserJejuGudgoCourse userCourse;


    public UserJejuGudgoRecord updateContent(String content) {
        return toBuilder()
                .content(content)
                .build();
    }
}
