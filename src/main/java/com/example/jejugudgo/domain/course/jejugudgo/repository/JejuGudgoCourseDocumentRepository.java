package com.example.jejugudgo.domain.course.jejugudgo.repository;

import com.example.jejugudgo.domain.course.jejugudgo.docs.JejuGudgoCourseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface JejuGudgoCourseDocumentRepository extends ElasticsearchRepository<JejuGudgoCourseDocument, Long> {
}
