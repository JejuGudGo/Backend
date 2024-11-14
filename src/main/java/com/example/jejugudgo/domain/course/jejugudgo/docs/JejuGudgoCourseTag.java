package com.example.jejugudgo.domain.course.jejugudgo.docs;

import jakarta.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "jeju_gudgo_course_tag")
public class JejuGudgoCourseTag {
    @Id
    private Long id;

    private Long courseId;

    private String tag;
}
