package com.example.jejugudgo.domain.course.olle.repository;

import com.example.jejugudgo.domain.course.olle.docs.JejuOlleSpot;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface JejuOlleSpotElasticRepository extends ElasticsearchRepository<JejuOlleSpot, Long> {
}
