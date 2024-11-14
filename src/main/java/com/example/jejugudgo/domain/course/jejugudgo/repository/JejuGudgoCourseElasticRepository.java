package com.example.jejugudgo.domain.course.jejugudgo.repository;

import com.example.jejugudgo.domain.course.jejugudgo.docs.JejuGudgoCourse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface JejuGudgoCourseElasticRepository extends ElasticsearchRepository<JejuGudgoCourse, Long> {
}
