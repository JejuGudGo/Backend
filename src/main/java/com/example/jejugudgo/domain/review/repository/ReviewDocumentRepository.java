package com.example.jejugudgo.domain.review.repository;

import com.example.jejugudgo.domain.review.docs.ReviewDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ReviewDocumentRepository extends ElasticsearchRepository<ReviewDocument, Long> {
}
