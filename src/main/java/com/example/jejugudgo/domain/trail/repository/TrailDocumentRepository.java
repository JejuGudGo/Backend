package com.example.jejugudgo.domain.trail.repository;

import com.example.jejugudgo.domain.trail.docs.TrailDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TrailDocumentRepository extends ElasticsearchRepository<TrailDocument, Long> {
}
