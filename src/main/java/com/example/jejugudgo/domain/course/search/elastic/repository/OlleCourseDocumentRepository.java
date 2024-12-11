package com.example.jejugudgo.domain.course.search.elastic.repository;

import com.example.jejugudgo.domain.course.search.elastic.doc.OlleCourseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface OlleCourseDocumentRepository extends ElasticsearchRepository<OlleCourseDocument, Long> {
}
