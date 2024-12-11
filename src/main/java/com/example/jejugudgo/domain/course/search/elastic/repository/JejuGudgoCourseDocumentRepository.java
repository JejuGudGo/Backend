package com.example.jejugudgo.domain.course.search.elastic.repository;

import com.example.jejugudgo.domain.course.search.elastic.doc.JejuGudgoCourseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface JejuGudgoCourseDocumentRepository extends ElasticsearchRepository<JejuGudgoCourseDocument, Long> {
}
