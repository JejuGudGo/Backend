package com.example.jejugudgo.domain.course.olle.repository;

import com.example.jejugudgo.domain.course.olle.docs.JejuOlleCourse;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface JejuOlleCourseElasticRepository extends ElasticsearchRepository<JejuOlleCourse, Long> {
}
