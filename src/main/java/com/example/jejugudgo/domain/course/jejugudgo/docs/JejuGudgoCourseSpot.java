package com.example.jejugudgo.domain.course.jejugudgo.docs;

import org.springframework.data.annotation.Id;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "jeju_gudgo_course_spot")
@Data
public class JejuGudgoCourseSpot {
    @Id
    private Long id;

    private Long courseId;

    private String title;

    private String spotType;

    private Long orderNumber;

    private String address;

    private double latitude;

    private double longitude;
}
