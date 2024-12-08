package com.example.jejugudgo.domain.course.search.elastic.repository;

import com.example.jejugudgo.domain.course.search.elastic.doc.TrailDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TrailDocumentRepository extends ElasticsearchRepository<TrailDocument, Long> {
}
