package com.example.jejugudgo.domain.course.recommend.repository;

import com.example.jejugudgo.domain.course.recommend.entity.TourApiContentType;
import com.example.jejugudgo.domain.course.recommend.enums.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourApiContentTypeRepository extends JpaRepository<TourApiContentType, Long> {
    TourApiContentType findByContentType(ContentType contentType);
}
