package com.example.jejugudgo.domain.course.olle.repository;

import com.example.jejugudgo.domain.course.olle.docs.JejuOlleSpotDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface JejuOlleSpotDocumentRepository extends ElasticsearchRepository<JejuOlleSpotDocument, Long> {
}
