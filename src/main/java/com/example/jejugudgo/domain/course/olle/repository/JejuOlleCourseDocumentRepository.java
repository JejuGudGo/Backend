package com.example.jejugudgo.domain.course.olle.repository;

import com.example.jejugudgo.domain.course.olle.docs.JejuOlleCourseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface JejuOlleCourseDocumentRepository extends ElasticsearchRepository<JejuOlleCourseDocument, Long> {
}
