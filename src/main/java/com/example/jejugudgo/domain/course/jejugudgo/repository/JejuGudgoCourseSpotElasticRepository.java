package com.example.jejugudgo.domain.course.jejugudgo.repository;

import com.example.jejugudgo.domain.course.jejugudgo.docs.JejuGudgoCourseSpot;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface JejuGudgoCourseSpotElasticRepository extends ElasticsearchRepository<JejuGudgoCourseSpot, Long> {
}
