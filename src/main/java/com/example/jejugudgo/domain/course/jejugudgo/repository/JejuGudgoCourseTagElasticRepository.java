package com.example.jejugudgo.domain.course.jejugudgo.repository;

import com.example.jejugudgo.domain.course.jejugudgo.docs.JejuGudgoCourseTag;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface JejuGudgoCourseTagElasticRepository extends ElasticsearchRepository<JejuGudgoCourseTag, Long> {
}
