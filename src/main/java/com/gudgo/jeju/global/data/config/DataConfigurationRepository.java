package com.gudgo.jeju.global.data.config;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DataConfigurationRepository extends JpaRepository<DataConfiguration, Long> {
    Optional<DataConfiguration> findByConfigKey(String key);
}
