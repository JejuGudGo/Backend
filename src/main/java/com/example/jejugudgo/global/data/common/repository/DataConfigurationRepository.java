package com.example.jejugudgo.global.data.common.repository;

import com.example.jejugudgo.global.data.common.entity.DataCommandLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DataConfigurationRepository extends JpaRepository<DataCommandLog, Long> {
    Optional<DataCommandLog> findByConfigKey(String key);
    boolean existsByConfigKeyAndConfigValue(String configKey, boolean configValue);
}
