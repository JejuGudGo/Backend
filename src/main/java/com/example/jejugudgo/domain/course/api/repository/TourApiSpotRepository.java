package com.example.jejugudgo.domain.course.api.repository;

import com.example.jejugudgo.domain.course.api.entity.TourApiContentType;
import com.example.jejugudgo.domain.course.api.entity.TourApiSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TourApiSpotRepository extends JpaRepository<TourApiSpot, Long> {
    Optional<TourApiSpot> findByContentTypeAndTitle(TourApiContentType contentType, String title);
}
