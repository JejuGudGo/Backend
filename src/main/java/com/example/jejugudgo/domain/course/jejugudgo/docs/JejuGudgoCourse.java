package com.example.jejugudgo.domain.course.jejugudgo.docs;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDate;

@Document(indexName = "jeju_gudgo_course")
@Data
public class JejuGudgoCourse {
    @Id
    private Long id;

    private String title;

    private LocalDate createdAt;

    private double starAvg;

    private String time;

    private String distance;

    private String imageUrl;

    private String summary;

    private Long viewCount;
}
