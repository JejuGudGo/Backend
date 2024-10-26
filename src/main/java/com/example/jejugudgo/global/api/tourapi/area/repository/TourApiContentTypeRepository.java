package com.example.jejugudgo.global.api.tourapi.area.repository;

import com.example.jejugudgo.global.api.tourapi.common.entity.ContentType;
import com.example.jejugudgo.global.api.tourapi.common.entity.TourApiContentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TourApiContentTypeRepository extends JpaRepository<TourApiContentType, Long> {
    TourApiContentType findByContentType(ContentType contentType);

}
