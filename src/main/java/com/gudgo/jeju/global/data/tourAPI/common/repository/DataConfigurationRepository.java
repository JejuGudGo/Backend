package com.gudgo.jeju.global.data.tourAPI.common.repository;

import com.gudgo.jeju.global.data.tourAPI.common.entity.DataConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DataConfigurationRepository extends JpaRepository<DataConfiguration, Long> {
    Optional<DataConfiguration> findByConfigKey(String key);
}
