package com.example.jejugudgo.domain.course.api.repository;

import com.example.jejugudgo.domain.course.api.entity.TourApiContentType;
import com.example.jejugudgo.domain.course.api.enums.ContentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourApiContentTypeRepository extends JpaRepository<TourApiContentType, Long> {
    TourApiContentType findByContentType(ContentType contentType);

}
