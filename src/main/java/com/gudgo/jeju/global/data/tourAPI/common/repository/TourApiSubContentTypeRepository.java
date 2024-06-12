package com.gudgo.jeju.global.data.tourAPI.common.repository;

import com.gudgo.jeju.global.data.tourAPI.common.entity.TourApiSubContentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourApiSubContentTypeRepository extends JpaRepository<TourApiSubContentType, String> {
}
