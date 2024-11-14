package com.example.jejugudgo.domain.trail.repository;

import com.example.jejugudgo.domain.trail.docs.Trail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TrailElasticRepository extends ElasticsearchRepository<Trail, Long> {
}
