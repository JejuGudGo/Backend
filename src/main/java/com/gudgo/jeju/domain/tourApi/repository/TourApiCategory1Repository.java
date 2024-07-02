package com.gudgo.jeju.domain.tourApi.repository;

import com.gudgo.jeju.domain.tourApi.entity.TourApiCategory1;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TourApiCategory1Repository extends JpaRepository<TourApiCategory1, String> {

    Optional<TourApiCategory1> findByCategoryName(String categoryName);
}
